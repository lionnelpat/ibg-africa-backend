package org.forbidec.web.rest;

import org.forbidec.service.BulletinService;
import org.forbidec.service.dto.bulletin.BulletinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bulletin cumulé d'un étudiant (historique complet + synthèse), pour
 * affichage et impression.
 */
@RestController
@RequestMapping("/api")
public class BulletinResource {

    private static final Logger LOG = LoggerFactory.getLogger(BulletinResource.class);

    private final BulletinService bulletinService;

    public BulletinResource(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    @GetMapping("/etudiants/{id}/bulletin")
    public BulletinDTO getBulletin(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bulletin for Etudiant : {}", id);
        return bulletinService.getBulletin(id);
    }
}
