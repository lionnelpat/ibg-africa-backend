package org.forbidec.service;

import java.util.List;
import org.forbidec.domain.Cycle;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.repository.CycleRepository;
import org.forbidec.repository.bulletin.CycleDetailQueryRepository;
import org.forbidec.service.dto.bulletin.CycleDetailDTO;
import org.forbidec.service.dto.bulletin.EtudiantResumeDTO;
import org.forbidec.service.dto.bulletin.MatiereDispenseeDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Assemble la vue détaillée d'un cycle : contexte, matières dispensées avec
 * leur enseignant, étudiants inscrits.
 */
@Service
@Transactional(readOnly = true)
public class CycleDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(CycleDetailService.class);

    private final CycleRepository cycleRepository;
    private final CycleDetailQueryRepository cycleDetailQueryRepository;

    public CycleDetailService(CycleRepository cycleRepository, CycleDetailQueryRepository cycleDetailQueryRepository) {
        this.cycleRepository = cycleRepository;
        this.cycleDetailQueryRepository = cycleDetailQueryRepository;
    }

    public CycleDetailDTO getDetail(Long cycleId) {
        LOG.debug("Request to get Cycle detail : {}", cycleId);

        Cycle cycle = cycleRepository.findById(cycleId).orElseThrow(() ->
            new BadRequestAlertException("Cycle introuvable", "cycle", "idnotfound")
        );

        CycleDetailDTO dto = new CycleDetailDTO();
        dto.setId(cycle.getId());
        dto.setAnnee(cycle.getAnnee());
        dto.setLibelle(cycle.getLibelle());
        dto.setDateDebut(cycle.getDateDebut());
        dto.setDateFin(cycle.getDateFin());
        dto.setCloture(cycle.getCloture());

        if (cycle.getCentre() != null) {
            dto.setCentreId(cycle.getCentre().getId());
            dto.setCentreCode(cycle.getCentre().getCode());
            dto.setCentreNom(cycle.getCentre().getNom());
            dto.setCentreVille(cycle.getCentre().getVille());
            if (cycle.getCentre().getPays() != null) {
                dto.setPaysNom(cycle.getCentre().getPays().getNom());
            }
        }

        List<EvaluationPrevue> evaluations = cycleDetailQueryRepository.findMatieresDispensees(cycleId);
        dto.setMatieresDispensees(
            evaluations
                .stream()
                .map(ep -> {
                    MatiereDispenseeDTO m = new MatiereDispenseeDTO();
                    if (ep.getCours() != null) {
                        m.setCoursId(ep.getCours().getId());
                        m.setCoursIntitule(ep.getCours().getIntitule());
                    }
                    if (ep.getMatiere() != null) {
                        m.setMatiereIntitule(ep.getMatiere().getIntitule());
                    }
                    if (ep.getSousMatiere() != null) {
                        m.setSousMatiereIntitule(ep.getSousMatiere().getIntitule());
                    }
                    if (ep.getEnseignant() != null) {
                        m.setEnseignantId(ep.getEnseignant().getId());
                        m.setEnseignantNom(ep.getEnseignant().getNom());
                        m.setEnseignantPrenom(ep.getEnseignant().getPrenom());
                    }
                    return m;
                })
                .toList()
        );

        List<InscriptionCycle> inscriptions = cycleDetailQueryRepository.findInscriptionsForCycle(cycleId);
        dto.setEtudiants(
            inscriptions
                .stream()
                .map(InscriptionCycle::getEtudiant)
                .filter(java.util.Objects::nonNull)
                .map(etudiant -> {
                    EtudiantResumeDTO e = new EtudiantResumeDTO();
                    e.setId(etudiant.getId());
                    e.setMatricule(etudiant.getMatricule());
                    e.setNom(etudiant.getNom());
                    e.setPrenom(etudiant.getPrenom());
                    e.setActif(etudiant.getActif());
                    return e;
                })
                .toList()
        );

        return dto;
    }
}
