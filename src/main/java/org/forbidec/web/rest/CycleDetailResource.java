package org.forbidec.web.rest;

import org.forbidec.service.CycleDetailService;
import org.forbidec.service.dto.bulletin.CycleDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Vue détaillée d'un cycle : contexte, matières dispensées, étudiants inscrits.
 */
@RestController
@RequestMapping("/api")
public class CycleDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(CycleDetailResource.class);

    private final CycleDetailService cycleDetailService;

    public CycleDetailResource(CycleDetailService cycleDetailService) {
        this.cycleDetailService = cycleDetailService;
    }

    @GetMapping("/cycles/{id}/detail")
    public CycleDetailDTO getCycleDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cycle detail : {}", id);
        return cycleDetailService.getDetail(id);
    }
}
