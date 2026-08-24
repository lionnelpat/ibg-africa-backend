package org.forbidec.web.rest;

import java.util.List;
import org.forbidec.service.SaisieNotesService;
import org.forbidec.service.dto.saisie.SaisieMatiereDTO;
import org.forbidec.service.dto.saisie.SaisieNoteRequestDTO;
import org.forbidec.service.dto.saisie.SaisieResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
