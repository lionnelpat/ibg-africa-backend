package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.SousMatiereRepository;
import org.forbidec.service.SousMatiereService;
import org.forbidec.service.dto.SousMatiereDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.SousMatiere}.
 */
@RestController
@RequestMapping("/api/sous-matieres")
public class SousMatiereResource {

    private static final Logger LOG = LoggerFactory.getLogger(SousMatiereResource.class);

    private static final String ENTITY_NAME = "sousMatiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousMatiereService sousMatiereService;

    private final SousMatiereRepository sousMatiereRepository;

    public SousMatiereResource(SousMatiereService sousMatiereService, SousMatiereRepository sousMatiereRepository) {
        this.sousMatiereService = sousMatiereService;
        this.sousMatiereRepository = sousMatiereRepository;
    }

    /**
     * {@code POST  /sous-matieres} : Create a new sousMatiere.
     *
     * @param sousMatiereDTO the sousMatiereDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousMatiereDTO, or with status {@code 400 (Bad Request)} if the sousMatiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SousMatiereDTO> createSousMatiere(@Valid @RequestBody SousMatiereDTO sousMatiereDTO) throws URISyntaxException {
        LOG.debug("REST request to save SousMatiere : {}", sousMatiereDTO);
        if (sousMatiereDTO.getId() != null) {
            throw new BadRequestAlertException("A new sousMatiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sousMatiereDTO = sousMatiereService.save(sousMatiereDTO);
        return ResponseEntity.created(new URI("/api/sous-matieres/" + sousMatiereDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sousMatiereDTO.getId().toString()))
            .body(sousMatiereDTO);
    }

    /**
     * {@code PUT  /sous-matieres/:id} : Updates an existing sousMatiere.
     *
     * @param id the id of the sousMatiereDTO to save.
     * @param sousMatiereDTO the sousMatiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousMatiereDTO,
     * or with status {@code 400 (Bad Request)} if the sousMatiereDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousMatiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SousMatiereDTO> updateSousMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousMatiereDTO sousMatiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SousMatiere : {}, {}", id, sousMatiereDTO);
        if (sousMatiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousMatiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sousMatiereDTO = sousMatiereService.update(sousMatiereDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousMatiereDTO.getId().toString()))
            .body(sousMatiereDTO);
    }

    /**
     * {@code PATCH  /sous-matieres/:id} : Partial updates given fields of an existing sousMatiere, field will ignore if it is null
     *
     * @param id the id of the sousMatiereDTO to save.
     * @param sousMatiereDTO the sousMatiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousMatiereDTO,
     * or with status {@code 400 (Bad Request)} if the sousMatiereDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sousMatiereDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousMatiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousMatiereDTO> partialUpdateSousMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousMatiereDTO sousMatiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SousMatiere partially : {}, {}", id, sousMatiereDTO);
        if (sousMatiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousMatiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousMatiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousMatiereDTO> result = sousMatiereService.partialUpdate(sousMatiereDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sousMatiereDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-matieres} : get all the sousMatieres.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousMatieres in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SousMatiereDTO>> getAllSousMatieres(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SousMatieres");
        Page<SousMatiereDTO> page = sousMatiereService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-matieres/:id} : get the "id" sousMatiere.
     *
     * @param id the id of the sousMatiereDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousMatiereDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SousMatiereDTO> getSousMatiere(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SousMatiere : {}", id);
        Optional<SousMatiereDTO> sousMatiereDTO = sousMatiereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousMatiereDTO);
    }

    /**
     * {@code DELETE  /sous-matieres/:id} : delete the "id" sousMatiere.
     *
     * @param id the id of the sousMatiereDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSousMatiere(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SousMatiere : {}", id);
        sousMatiereService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
