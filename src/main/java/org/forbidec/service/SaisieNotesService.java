package org.forbidec.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.domain.HistoriqueNote;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.domain.enumeration.StatutNote;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.repository.EvaluationPrevueRepository;
import org.forbidec.repository.EvaluationRealiseeRepository;
import org.forbidec.repository.HistoriqueNoteRepository;
import org.forbidec.repository.bulletin.CycleDetailQueryRepository;
import org.forbidec.repository.saisie.SaisieQueryRepository;
import org.forbidec.security.SecurityUtils;
import org.forbidec.service.dto.saisie.SaisieLigneDTO;
import org.forbidec.service.dto.saisie.SaisieMatiereDTO;
import org.forbidec.service.dto.saisie.SaisieNoteRequestDTO;
import org.forbidec.service.dto.saisie.SaisieResultDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Grille de saisie des notes : un étudiant inscrit au cycle par ligne, une
 * note pour la matière (EvaluationPrevue) sélectionnée. Réutilisée telle
 * quelle par la saisie manuelle et par l'import Excel en masse.
 */
@Service
@Transactional
public class SaisieNotesService {

    private static final Logger LOG = LoggerFactory.getLogger(SaisieNotesService.class);

    private final EvaluationPrevueRepository evaluationPrevueRepository;
    private final EvaluationRealiseeRepository evaluationRealiseeRepository;
    private final HistoriqueNoteRepository historiqueNoteRepository;
    private final CycleDetailQueryRepository cycleDetailQueryRepository;
    private final SaisieQueryRepository saisieQueryRepository;
    private final EtudiantRepository etudiantRepository;

    public SaisieNotesService(
        EvaluationPrevueRepository evaluationPrevueRepository,
        EvaluationRealiseeRepository evaluationRealiseeRepository,
        HistoriqueNoteRepository historiqueNoteRepository,
        CycleDetailQueryRepository cycleDetailQueryRepository,
        SaisieQueryRepository saisieQueryRepository,
        EtudiantRepository etudiantRepository
    ) {
        this.evaluationPrevueRepository = evaluationPrevueRepository;
        this.evaluationRealiseeRepository = evaluationRealiseeRepository;
        this.historiqueNoteRepository = historiqueNoteRepository;
        this.cycleDetailQueryRepository = cycleDetailQueryRepository;
        this.saisieQueryRepository = saisieQueryRepository;
        this.etudiantRepository = etudiantRepository;
    }

    @Transactional(readOnly = true)
    public SaisieMatiereDTO getGrille(Long evaluationPrevueId) {
        LOG.debug("Request to get the saisie grid for EvaluationPrevue : {}", evaluationPrevueId);

        EvaluationPrevue ep = evaluationPrevueRepository
            .findOneWithEagerRelationships(evaluationPrevueId)
            .orElseThrow(() -> new BadRequestAlertException("Matière planifiée introuvable", "evaluationPrevue", "idnotfound"));

        List<InscriptionCycle> inscriptions = cycleDetailQueryRepository.findInscriptionsForCycle(ep.getCycle().getId());
        Map<Long, EvaluationRealisee> existantes = new HashMap<>();
        for (EvaluationRealisee er : saisieQueryRepository.findByEvaluationPrevueId(evaluationPrevueId)) {
            existantes.put(er.getEtudiant().getId(), er);
        }

        List<SaisieLigneDTO> lignes = new ArrayList<>();
        for (InscriptionCycle inscription : inscriptions) {
            if (inscription.getEtudiant() == null) {
                continue;
            }
            SaisieLigneDTO ligne = new SaisieLigneDTO();
            ligne.setEtudiantId(inscription.getEtudiant().getId());
            ligne.setMatricule(inscription.getEtudiant().getMatricule());
            ligne.setNom(inscription.getEtudiant().getNom());
            ligne.setPrenom(inscription.getEtudiant().getPrenom());
            EvaluationRealisee existante = existantes.get(inscription.getEtudiant().getId());
            if (existante != null) {
                ligne.setEvaluationRealiseeId(existante.getId());
                ligne.setNote(existante.getNote());
                ligne.setStatut(existante.getStatut());
            } else {
                ligne.setStatut(StatutNote.NON_SAISIE);
            }
            lignes.add(ligne);
        }

        SaisieMatiereDTO dto = new SaisieMatiereDTO();
        dto.setEvaluationPrevueId(ep.getId());
        dto.setIntitule(ep.getIntitule());
        dto.setCoefficient(ep.getCoefficient());
        dto.setNoteMaximale(ep.getNoteMaximale());
        if (ep.getCours() != null) {
            dto.setCoursIntitule(ep.getCours().getIntitule());
        }
        if (ep.getCycle() != null) {
            dto.setCycleId(ep.getCycle().getId());
            dto.setCycleAnnee(ep.getCycle().getAnnee());
            dto.setCycleCloture(ep.getCycle().getCloture());
        }
        dto.setLignes(lignes);
        return dto;
    }

    /**
     * Enregistre (crée ou met à jour) une note par étudiant pour une matière.
     * Un {@link HistoriqueNote} est tracé à chaque changement effectif.
     * Les étudiants non inscrits au cycle de cette matière sont rejetés
     * (utile pour l'import en masse, où une ligne peut cibler le mauvais
     * cycle par erreur).
     */
    public SaisieResultDTO enregistrer(Long evaluationPrevueId, List<SaisieNoteRequestDTO> demandes) {
        EvaluationPrevue ep = evaluationPrevueRepository
            .findOneWithEagerRelationships(evaluationPrevueId)
            .orElseThrow(() -> new BadRequestAlertException("Matière planifiée introuvable", "evaluationPrevue", "idnotfound"));

        if (Boolean.TRUE.equals(ep.getCycle().getCloture())) {
            throw new BadRequestAlertException("Le cycle est clôturé : la saisie des notes n'est plus possible.", "evaluationPrevue", "cyclecloture");
        }

        Set<Long> inscritIds = cycleDetailQueryRepository
            .findInscriptionsForCycle(ep.getCycle().getId())
            .stream()
            .filter(ic -> ic.getEtudiant() != null)
            .map(ic -> ic.getEtudiant().getId())
            .collect(Collectors.toSet());

        String utilisateur = SecurityUtils.getCurrentUserLogin().orElse("system");
        List<String> erreurs = new ArrayList<>();
        int enregistrees = 0;

        for (SaisieNoteRequestDTO demande : demandes) {
            if (demande.getEtudiantId() == null) {
                erreurs.add("Ligne sans étudiant ignorée");
                continue;
            }
            if (!inscritIds.contains(demande.getEtudiantId())) {
                erreurs.add("Étudiant " + demande.getEtudiantId() + " non inscrit à ce cycle, ignoré");
                continue;
            }

            StatutNote nouveauStatut = demande.getStatut() != null ? demande.getStatut() : StatutNote.NON_SAISIE;
            EvaluationRealisee existante = saisieQueryRepository.findOne(evaluationPrevueId, demande.getEtudiantId()).orElse(null);

            if (existante == null && nouveauStatut == StatutNote.NON_SAISIE && demande.getNote() == null) {
                // rien à saisir et rien à effacer
                continue;
            }

            var noteAvant = existante != null ? existante.getNote() : null;
            var statutAvant = existante != null ? existante.getStatut() : null;
            boolean modifie = existante == null || !Objects.equals(noteAvant, demande.getNote()) || statutAvant != nouveauStatut;

            EvaluationRealisee er = existante;
            if (er == null) {
                Etudiant etudiant = etudiantRepository
                    .findById(demande.getEtudiantId())
                    .orElseThrow(() -> new BadRequestAlertException("Étudiant introuvable", "etudiant", "idnotfound"));
                er = new EvaluationRealisee();
                er.setEvaluationPrevue(ep);
                er.setEtudiant(etudiant);
                er.setCompteDansMoyenne(Boolean.TRUE.equals(ep.getCompteDansMoyenne()));
            }
            er.setNote(demande.getNote());
            er.setStatut(nouveauStatut);
            er.setSaisiePar(utilisateur);
            er.setSaisieLe(Instant.now());
            er = evaluationRealiseeRepository.save(er);
            enregistrees++;

            if (modifie) {
                HistoriqueNote historique = new HistoriqueNote();
                historique.setNoteAvant(noteAvant);
                historique.setNoteApres(demande.getNote());
                historique.setStatutAvant(statutAvant);
                historique.setStatutApres(nouveauStatut);
                historique.setModifiePar(utilisateur);
                historique.setModifieLe(Instant.now());
                historique.setEvaluationRealisee(er);
                historiqueNoteRepository.save(historique);
            }
        }

        SaisieResultDTO result = new SaisieResultDTO();
        result.setEnregistrees(enregistrees);
        result.setErreurs(erreurs);
        return result;
    }

    /**
     * Saisie en masse depuis un fichier Excel généré par
     * {@link #genererTemplateExcel} : 5 premières lignes ignorées (Matière,
     * vide, Enseignant, vide, en-tête du tableau), puis colonne A =
     * matricule, colonne B = nom prénom (informatif, non exploité),
     * colonne C = note. Les matricules inconnus ou les notes non
     * numériques sont rapportés en erreur sans bloquer le reste du
     * fichier ; les lignes valides passent ensuite par le même
     * {@link #enregistrer} que la saisie manuelle (donc le même contrôle
     * d'inscription au cycle, et la même traçabilité HistoriqueNote).
     */
    public SaisieResultDTO importerExcel(Long evaluationPrevueId, MultipartFile fichier) {
        List<String> erreursImport = new ArrayList<>();
        List<SaisieNoteRequestDTO> demandes = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream in = fichier.getInputStream(); Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                int numeroLigne = row.getRowNum() + 1;
                if (row.getRowNum() < 5) {
                    // lignes 1-4 : Matière / vide / Enseignant / vide, ligne 5 : en-tête
                    continue;
                }
                Cell celluleMatricule = row.getCell(0);
                Cell celluleNote = row.getCell(2);
                String matricule = celluleMatricule != null ? formatter.formatCellValue(celluleMatricule).trim() : "";
                String noteTexte = celluleNote != null ? formatter.formatCellValue(celluleNote).trim() : "";
                if (matricule.isEmpty() && noteTexte.isEmpty()) {
                    continue;
                }
                if (matricule.isEmpty()) {
                    erreursImport.add("Ligne " + numeroLigne + " : matricule manquant, ignorée");
                    continue;
                }

                Etudiant etudiant = saisieQueryRepository.findEtudiantByMatricule(matricule).orElse(null);
                if (etudiant == null) {
                    erreursImport.add("Ligne " + numeroLigne + " : matricule '" + matricule + "' introuvable");
                    continue;
                }

                BigDecimal note = null;
                if (!noteTexte.isEmpty()) {
                    try {
                        note = new BigDecimal(noteTexte.replace(',', '.'));
                    } catch (NumberFormatException e) {
                        erreursImport.add("Ligne " + numeroLigne + " : note '" + noteTexte + "' invalide, ignorée");
                        continue;
                    }
                }

                SaisieNoteRequestDTO demande = new SaisieNoteRequestDTO();
                demande.setEtudiantId(etudiant.getId());
                demande.setNote(note);
                demande.setStatut(note != null ? StatutNote.SAISIE : StatutNote.NON_SAISIE);
                demandes.add(demande);
            }
        } catch (IOException e) {
            throw new BadRequestAlertException("Fichier illisible : " + e.getMessage(), "evaluationRealisee", "fichierillisible");
        }

        SaisieResultDTO result = enregistrer(evaluationPrevueId, demandes);
        List<String> toutesErreurs = new ArrayList<>(erreursImport);
        toutesErreurs.addAll(result.getErreurs());
        result.setErreurs(toutesErreurs);
        return result;
    }

    /**
     * Modèle Excel vierge pour la saisie hors-ligne : 4 lignes d'information
     * (Matière, ligne vide, Enseignant, ligne vide), puis l'en-tête du
     * tableau (Matricule, Nom Prénom, Note) à la ligne 5, puis une ligne par
     * étudiant inscrit au cycle de la matière, prête à être complétée par
     * l'enseignant et réimportée via {@link #importerExcel} (qui ignore les
     * 5 premières lignes en conséquence).
     */
    @Transactional(readOnly = true)
    public byte[] genererTemplateExcel(Long evaluationPrevueId) {
        EvaluationPrevue ep = evaluationPrevueRepository
            .findOneWithEagerRelationships(evaluationPrevueId)
            .orElseThrow(() -> new BadRequestAlertException("Matière planifiée introuvable", "evaluationPrevue", "idnotfound"));

        List<InscriptionCycle> inscriptions = cycleDetailQueryRepository.findInscriptionsForCycle(ep.getCycle().getId());

        String matiereLibelle = ep.getMatiere() != null ? ep.getCours().getLibelleLong() : "";
        String enseignantLibelle = ep.getEnseignant() != null ? ep.getEnseignant().getNom() + " " + ep.getEnseignant().getPrenom() : "";

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Notes");

            sheet.createRow(0).createCell(0).setCellValue("Matière : " + matiereLibelle);
            sheet.createRow(1);
            sheet.createRow(2).createCell(0).setCellValue("Enseignant : " + enseignantLibelle);
            sheet.createRow(3);

            Row header = sheet.createRow(4);
            header.createCell(0).setCellValue("Matricule");
            header.createCell(1).setCellValue("Nom Prénom");
            header.createCell(2).setCellValue("Note");

            int numeroLigne = 5;
            for (InscriptionCycle inscription : inscriptions) {
                Etudiant etudiant = inscription.getEtudiant();
                if (etudiant == null) {
                    continue;
                }
                Row row = sheet.createRow(numeroLigne++);
                row.createCell(0).setCellValue(etudiant.getMatricule() != null ? etudiant.getMatricule() : "");
                row.createCell(1).setCellValue(etudiant.getNom() + " " + etudiant.getPrenom());
                row.createCell(2);
            }

            ByteArrayOutputStream sortie = new ByteArrayOutputStream();
            workbook.write(sortie);
            return sortie.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Erreur lors de la génération du modèle Excel", e);
        }
    }
}
