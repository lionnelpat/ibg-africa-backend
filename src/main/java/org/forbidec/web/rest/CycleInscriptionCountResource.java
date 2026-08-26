package org.forbidec.web.rest;

import java.util.List;
import org.forbidec.repository.cycle.CycleInscriptionCountQueryRepository;
import org.forbidec.service.dto.cycle.CycleInscriptionCountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nombre d'étudiants inscrits par cycle, pour la colonne dédiée de la
 * liste des cycles.
 */
@RestController
@RequestMapping("/api")
public class CycleInscriptionCountResource {

    private static final Logger LOG = LoggerFactory.getLogger(CycleInscriptionCountResource.class);

    private final CycleInscriptionCountQueryRepository cycleInscriptionCountQueryRepository;

    public CycleInscriptionCountResource(CycleInscriptionCountQueryRepository cycleInscriptionCountQueryRepository) {
        this.cycleInscriptionCountQueryRepository = cycleInscriptionCountQueryRepository;
    }

    @GetMapping("/cycles/nombre-inscrits")
    public List<CycleInscriptionCountDTO> getNombreInscrits(@RequestParam("cycleIds") List<Long> cycleIds) {
        LOG.debug("REST request to get inscription counts for cycles : {}", cycleIds);
        if (cycleIds.isEmpty()) {
            return List.of();
        }
        return cycleInscriptionCountQueryRepository.countByCycleIds(cycleIds);
    }
}
