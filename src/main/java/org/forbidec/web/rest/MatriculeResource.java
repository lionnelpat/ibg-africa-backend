package org.forbidec.web.rest;

import org.forbidec.service.MatriculeService;
import org.forbidec.service.dto.matricule.MatriculeGenerationResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MatriculeResource {

    private static final Logger LOG = LoggerFactory.getLogger(MatriculeResource.class);

    private final MatriculeService matriculeService;

    public MatriculeResource(MatriculeService matriculeService) {
        this.matriculeService = matriculeService;
    }

    @PostMapping("/etudiants/generer-matricules")
    public MatriculeGenerationResultDTO genererMatricules() {
        LOG.debug("REST request to generate missing matricules");
        return matriculeService.genererMatriculesManquants();
    }
}
