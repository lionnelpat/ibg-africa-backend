package org.forbidec.web.rest;

import java.util.List;
import org.forbidec.repository.etudiant.EtudiantCycleCountQueryRepository;
import org.forbidec.service.dto.etudiant.EtudiantCycleCountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nombre de cycles distincts par étudiant, pour la colonne "Année" de la
 * liste des étudiants.
 */
@RestController
@RequestMapping("/api")
public class EtudiantCycleCountResource {

    private static final Logger LOG = LoggerFactory.getLogger(EtudiantCycleCountResource.class);

    private final EtudiantCycleCountQueryRepository etudiantCycleCountQueryRepository;

    public EtudiantCycleCountResource(EtudiantCycleCountQueryRepository etudiantCycleCountQueryRepository) {
        this.etudiantCycleCountQueryRepository = etudiantCycleCountQueryRepository;
    }

    @GetMapping("/etudiants/nombre-cycles")
    public List<EtudiantCycleCountDTO> getNombreCycles(@RequestParam("etudiantIds") List<Long> etudiantIds) {
        LOG.debug("REST request to get cycle counts for etudiants : {}", etudiantIds);
        if (etudiantIds.isEmpty()) {
            return List.of();
        }
        return etudiantCycleCountQueryRepository.countByEtudiantIds(etudiantIds);
    }
}
