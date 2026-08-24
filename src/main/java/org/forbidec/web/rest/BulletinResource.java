package org.forbidec.web.rest;

import org.forbidec.service.BulletinPdfService;
import org.forbidec.service.BulletinService;
import org.forbidec.service.dto.bulletin.BulletinDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bulletin cumulé d'un étudiant (historique complet + synthèse), pour
 * affichage et export PDF.
 */
@RestController
@RequestMapping("/api")
public class BulletinResource {

    private static final Logger LOG = LoggerFactory.getLogger(BulletinResource.class);

    private final BulletinService bulletinService;
    private final BulletinPdfService bulletinPdfService;

    public BulletinResource(BulletinService bulletinService, BulletinPdfService bulletinPdfService) {
        this.bulletinService = bulletinService;
        this.bulletinPdfService = bulletinPdfService;
    }

    @GetMapping("/etudiants/{id}/bulletin")
    public BulletinDTO getBulletin(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bulletin for Etudiant : {}", id);
        return bulletinService.getBulletin(id);
    }

    @GetMapping("/etudiants/{id}/bulletin/pdf")
    public ResponseEntity<byte[]> getBulletinPdf(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bulletin PDF for Etudiant : {}", id);
        byte[] pdf = bulletinPdfService.genererPdf(id);
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"bulletin-" + id + ".pdf\"")
            .body(pdf);
    }
}
