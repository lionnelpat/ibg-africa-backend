package org.forbidec.web.rest;

import java.util.List;
import java.util.Optional;
import org.forbidec.service.HistoriqueNoteQueryService;
import org.forbidec.service.HistoriqueNoteService;
import org.forbidec.service.criteria.HistoriqueNoteCriteria;
import org.forbidec.service.dto.HistoriqueNoteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.HistoriqueNote}.
 */
@RestController
@RequestMapping("/api/historique-notes")
public class HistoriqueNoteResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueNoteResource.class);

    private final HistoriqueNoteService historiqueNoteService;

    private final HistoriqueNoteQueryService historiqueNoteQueryService;

    public HistoriqueNoteResource(HistoriqueNoteService historiqueNoteService, HistoriqueNoteQueryService historiqueNoteQueryService) {
        this.historiqueNoteService = historiqueNoteService;
        this.historiqueNoteQueryService = historiqueNoteQueryService;
    }

    /**
     * {@code GET  /historique-notes} : get all the historiqueNotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueNotes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoriqueNoteDTO>> getAllHistoriqueNotes(
        HistoriqueNoteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HistoriqueNotes by criteria: {}", criteria);

        Page<HistoriqueNoteDTO> page = historiqueNoteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-notes/count} : count all the historiqueNotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHistoriqueNotes(HistoriqueNoteCriteria criteria) {
        LOG.debug("REST request to count HistoriqueNotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueNoteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-notes/:id} : get the "id" historiqueNote.
     *
     * @param id the id of the historiqueNoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueNoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueNoteDTO> getHistoriqueNote(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HistoriqueNote : {}", id);
        Optional<HistoriqueNoteDTO> historiqueNoteDTO = historiqueNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueNoteDTO);
    }
}
