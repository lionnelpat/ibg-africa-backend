package org.forbidec.web.rest;

import java.util.List;
import org.forbidec.service.SaisieNotesService;
import org.forbidec.service.dto.saisie.SaisieMatiereDTO;
import org.forbidec.service.dto.saisie.SaisieNoteRequestDTO;
import org.forbidec.service.dto.saisie.SaisieResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Saisie des notes : grille étudiants inscrits × une matière (EvaluationPrevue).
 */
@RestController
@RequestMapping("/api")
public class SaisieNotesResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaisieNotesResource.class);

    private final SaisieNotesService saisieNotesService;

    public SaisieNotesResource(SaisieNotesService saisieNotesService) {
        this.saisieNotesService = saisieNotesService;
    }

    @GetMapping("/evaluation-prevues/{id}/saisie")
    public SaisieMatiereDTO getGrille(@PathVariable("id") Long id) {
        LOG.debug("REST request to get the saisie grid for EvaluationPrevue : {}", id);
        return saisieNotesService.getGrille(id);
    }

    @PutMapping("/evaluation-prevues/{id}/saisie")
    public SaisieResultDTO enregistrer(@PathVariable("id") Long id, @RequestBody List<SaisieNoteRequestDTO> lignes) {
        LOG.debug("REST request to save {} saisie line(s) for EvaluationPrevue : {}", lignes.size(), id);
        return saisieNotesService.enregistrer(id, lignes);
    }

    @PostMapping("/evaluation-prevues/{id}/saisie/import")
    public SaisieResultDTO importer(@PathVariable("id") Long id, @RequestParam("fichier") MultipartFile fichier) {
        LOG.debug("REST request to import an Excel file of notes for EvaluationPrevue : {}", id);
        return saisieNotesService.importerExcel(id, fichier);
    }

    @GetMapping("/evaluation-prevues/{id}/saisie/template")
    public ResponseEntity<byte[]> getTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get the notes import template for EvaluationPrevue : {}", id);
        byte[] xlsx = saisieNotesService.genererTemplateExcel(id);
        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"modele-notes-" + id + ".xlsx\"")
            .body(xlsx);
    }
}
