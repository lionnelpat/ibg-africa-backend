package org.forbidec.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        java.util.Set<Long> inscritIds = cycleDetailQueryRepository
            .findInscriptionsForCycle(ep.getCycle().getId())
            .stream()
            .filter(ic -> ic.getEtudiant() != null)
            .map(ic -> ic.getEtudiant().getId())
            .collect(java.util.stream.Collectors.toSet());

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
}
