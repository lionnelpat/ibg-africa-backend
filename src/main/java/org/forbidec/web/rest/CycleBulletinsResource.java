package org.forbidec.web.rest;

import org.forbidec.service.CycleBulletinsService;
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
 * Génération en masse des bulletins d'un cycle, regroupés dans une archive ZIP.
 */
@RestController
@RequestMapping("/api")
public class CycleBulletinsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CycleBulletinsResource.class);

    private final CycleBulletinsService cycleBulletinsService;

    public CycleBulletinsResource(CycleBulletinsService cycleBulletinsService) {
        this.cycleBulletinsService = cycleBulletinsService;
    }

    @GetMapping("/cycles/{id}/bulletins/zip")
    public ResponseEntity<byte[]> getBulletinsZip(@PathVariable("id") Long id) {
        LOG.debug("REST request to generate all bulletins for Cycle : {}", id);
        CycleBulletinsService.ZipBulletins resultat = cycleBulletinsService.genererZip(id);
        if (!resultat.erreurs().isEmpty()) {
            LOG.warn("Bulletins non générés pour le cycle {} : {}", id, resultat.erreurs());
        }
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resultat.nomFichier() + "\"")
            .header("X-Bulletins-Erreurs", String.valueOf(resultat.erreurs().size()))
            .body(resultat.contenu());
    }
}
