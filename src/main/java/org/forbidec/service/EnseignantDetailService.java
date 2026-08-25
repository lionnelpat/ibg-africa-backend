package org.forbidec.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.Enseignant;
import org.forbidec.repository.EnseignantRepository;
import org.forbidec.repository.enseignant.EnseignantDetailQueryRepository;
import org.forbidec.service.dto.bulletin.MatiereDispenseeDTO;
import org.forbidec.service.dto.enseignant.CycleEnseignementDTO;
import org.forbidec.service.dto.enseignant.EnseignantDetailDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Assemble la fiche détail d'un enseignant : identité et matières
 * dispensées groupées par cycle.
 */
@Service
@Transactional(readOnly = true)
public class EnseignantDetailService {

    private static final Logger LOG = LoggerFactory.getLogger(EnseignantDetailService.class);

    private final EnseignantRepository enseignantRepository;
    private final EnseignantDetailQueryRepository enseignantDetailQueryRepository;

    public EnseignantDetailService(
        EnseignantRepository enseignantRepository,
        EnseignantDetailQueryRepository enseignantDetailQueryRepository
    ) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantDetailQueryRepository = enseignantDetailQueryRepository;
    }

    public EnseignantDetailDTO getDetail(Long enseignantId) {
        LOG.debug("Request to get Enseignant detail : {}", enseignantId);

        Enseignant enseignant = enseignantRepository
            .findById(enseignantId)
            .orElseThrow(() -> new BadRequestAlertException("Enseignant introuvable", "enseignant", "idnotfound"));

        EnseignantDetailDTO dto = new EnseignantDetailDTO();
        dto.setId(enseignant.getId());
        dto.setNom(enseignant.getNom());
        dto.setPrenom(enseignant.getPrenom());
        dto.setLibelleLong(enseignant.getLibelleLong());
        dto.setLibelleCourt(enseignant.getLibelleCourt());
        dto.setEmail(enseignant.getEmail());
        dto.setTelephone(enseignant.getTelephone());
        dto.setCommentaire(enseignant.getCommentaire());
        dto.setActif(enseignant.getActif());
        dto.setPhoto(enseignant.getPhoto());
        dto.setPhotoContentType(enseignant.getPhotoContentType());

        List<EvaluationPrevue> evaluations = enseignantDetailQueryRepository.findByEnseignantId(enseignantId);

        Map<Long, CycleEnseignementDTO> parCycle = new LinkedHashMap<>();
        for (EvaluationPrevue ep : evaluations) {
            Long cycleId = ep.getCycle().getId();
            CycleEnseignementDTO cycleDto = parCycle.computeIfAbsent(cycleId, id -> {
                CycleEnseignementDTO c = new CycleEnseignementDTO();
                c.setCycleId(ep.getCycle().getId());
                c.setCycleAnnee(ep.getCycle().getAnnee());
                c.setCycleLibelle(ep.getCycle().getLibelle());
                c.setMatieres(new ArrayList<>());
                return c;
            });

            MatiereDispenseeDTO matiere = new MatiereDispenseeDTO();
            matiere.setEvaluationPrevueId(ep.getId());
            if (ep.getCours() != null) {
                matiere.setCoursId(ep.getCours().getId());
                matiere.setCoursIntitule(ep.getCours().getIntitule());
            }
            if (ep.getMatiere() != null) {
                matiere.setMatiereIntitule(ep.getMatiere().getIntitule());
            }
            if (ep.getSousMatiere() != null) {
                matiere.setSousMatiereIntitule(ep.getSousMatiere().getIntitule());
            }
            matiere.setEnseignantId(enseignant.getId());
            matiere.setEnseignantNom(enseignant.getNom());
            matiere.setEnseignantPrenom(enseignant.getPrenom());
            cycleDto.getMatieres().add(matiere);
        }

        dto.setCoursParCycle(new ArrayList<>(parCycle.values()));
        return dto;
    }
}
