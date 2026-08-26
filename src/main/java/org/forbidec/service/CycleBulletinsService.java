package org.forbidec.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.forbidec.domain.Cycle;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.repository.CycleRepository;
import org.forbidec.repository.bulletin.CycleDetailQueryRepository;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Génère l'archive ZIP des bulletins de tous les étudiants inscrits sur un
 * cycle. Rendu synchrone mais parallélisé (un thread par bulletin, borné) :
 * suffisant pour des cycles de quelques dizaines d'étudiants ; à revoir en
 * traitement asynchrone si des cycles beaucoup plus grands apparaissent.
 */
@Service
@Transactional(readOnly = true)
public class CycleBulletinsService {

    private static final Logger LOG = LoggerFactory.getLogger(CycleBulletinsService.class);
    private static final int MAX_THREADS = 8;

    private final CycleRepository cycleRepository;
    private final CycleDetailQueryRepository cycleDetailQueryRepository;
    private final BulletinPdfService bulletinPdfService;

    public CycleBulletinsService(
        CycleRepository cycleRepository,
        CycleDetailQueryRepository cycleDetailQueryRepository,
        BulletinPdfService bulletinPdfService
    ) {
        this.cycleRepository = cycleRepository;
        this.cycleDetailQueryRepository = cycleDetailQueryRepository;
        this.bulletinPdfService = bulletinPdfService;
    }

    /** Résultat de la génération : le contenu du ZIP, son nom de fichier suggéré, et les échecs éventuels. */
    public record ZipBulletins(byte[] contenu, String nomFichier, List<String> erreurs) {}

    public ZipBulletins genererZip(Long cycleId) {
        Cycle cycle = cycleRepository.findById(cycleId).orElseThrow(() ->
            new BadRequestAlertException("Cycle introuvable", "cycle", "idnotfound")
        );

        List<Etudiant> etudiants = cycleDetailQueryRepository
            .findInscriptionsForCycle(cycleId)
            .stream()
            .map(InscriptionCycle::getEtudiant)
            .filter(Objects::nonNull)
            .toList();

        if (etudiants.isEmpty()) {
            throw new BadRequestAlertException("Aucun étudiant inscrit sur ce cycle", "cycle", "aucuninscrit");
        }

        record Resultat(String nomFichier, byte[] pdf, String erreur) {}

        ExecutorService executor = Executors.newFixedThreadPool(Math.min(MAX_THREADS, etudiants.size()));
        List<Future<Resultat>> futures = new ArrayList<>();
        try {
            for (Etudiant etudiant : etudiants) {
                futures.add(
                    executor.submit(() -> {
                        try {
                            byte[] pdf = bulletinPdfService.genererPdf(etudiant.getId());
                            return new Resultat(nomFichierBulletin(etudiant), pdf, null);
                        } catch (Exception e) {
                            LOG.warn("Échec de génération du bulletin pour l'étudiant {}", etudiant.getId(), e);
                            return new Resultat(
                                null,
                                null,
                                etudiant.getNom() + " " + etudiant.getPrenom() + " (" + etudiant.getMatricule() + ")"
                            );
                        }
                    })
                );
            }

            List<String> erreurs = new ArrayList<>();
            Set<String> nomsUtilises = new HashSet<>();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos, StandardCharsets.UTF_8)) {
                for (Future<Resultat> future : futures) {
                    Resultat resultat = future.get();
                    if (resultat.erreur() != null) {
                        erreurs.add(resultat.erreur());
                        continue;
                    }
                    String nom = resultat.nomFichier();
                    while (!nomsUtilises.add(nom)) {
                        nom = nom.replace(".pdf", "_2.pdf");
                    }
                    zos.putNextEntry(new ZipEntry(nom));
                    zos.write(resultat.pdf());
                    zos.closeEntry();
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                throw new IllegalStateException("Erreur lors de la génération de l'archive des bulletins", e);
            }

            String nomFichierZip =
                "bulletins_cycle_" + cycle.getAnnee() + (cycle.getLibelle() != null ? "_" + slugify(cycle.getLibelle()) : "") + ".zip";
            return new ZipBulletins(baos.toByteArray(), nomFichierZip, erreurs);
        } finally {
            executor.shutdown();
        }
    }

    private String nomFichierBulletin(Etudiant etudiant) {
        String matricule = etudiant.getMatricule() != null ? etudiant.getMatricule() : String.valueOf(etudiant.getId());
        return slugify(etudiant.getNom() + "_" + etudiant.getPrenom() + "_" + matricule) + ".pdf";
    }

    private String slugify(String value) {
        String sansAccents = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        String nettoye = sansAccents.toUpperCase(Locale.FRENCH).replaceAll("[^A-Z0-9]+", "_");
        return nettoye.replaceAll("^_+", "").replaceAll("_+$", "");
    }
}
