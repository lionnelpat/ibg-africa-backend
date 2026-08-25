package org.forbidec.web.rest;

import org.forbidec.service.EnseignantDetailService;
import org.forbidec.service.dto.enseignant.EnseignantDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fiche détail d'un enseignant : identité et matières dispensées par cycle.
 */
@RestController
@RequestMapping("/api")
public class EnseignantDetailResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnseignantDetailResource.class);

    private final EnseignantDetailService enseignantDetailService;

    public EnseignantDetailResource(EnseignantDetailService enseignantDetailService) {
        this.enseignantDetailService = enseignantDetailService;
    }

    @GetMapping("/enseignants/{id}/detail")
    public EnseignantDetailDTO getDetail(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Enseignant detail : {}", id);
        return enseignantDetailService.getDetail(id);
    }
}
