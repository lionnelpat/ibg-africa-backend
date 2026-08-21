package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.InscriptionCycleRepository;
import org.forbidec.service.InscriptionCycleQueryService;
import org.forbidec.service.InscriptionCycleService;
import org.forbidec.service.criteria.InscriptionCycleCriteria;
import org.forbidec.service.dto.InscriptionCycleDTO;
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
 * REST controller for managing {@link org.forbidec.domain.InscriptionCycle}.
 */
@RestController
@RequestMapping("/api/inscription-cycles")
public class InscriptionCycleResource {

    private static final Logger LOG = LoggerFactory.getLogger(InscriptionCycleResource.class);

    private static final String ENTITY_NAME = "inscriptionCycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionCycleService inscriptionCycleService;

    private final InscriptionCycleRepository inscriptionCycleRepository;

    private final InscriptionCycleQueryService inscriptionCycleQueryService;

    public InscriptionCycleResource(
        InscriptionCycleService inscriptionCycleService,
        InscriptionCycleRepository inscriptionCycleRepository,
        InscriptionCycleQueryService inscriptionCycleQueryService
    ) {
        this.inscriptionCycleService = inscriptionCycleService;
        this.inscriptionCycleRepository = inscriptionCycleRepository;
        this.inscriptionCycleQueryService = inscriptionCycleQueryService;
    }

    /**
     * {@code POST  /inscription-cycles} : Create a new inscriptionCycle.
     *
     * @param inscriptionCycleDTO the inscriptionCycleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inscriptionCycleDTO, or with status {@code 400 (Bad Request)} if the inscriptionCycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InscriptionCycleDTO> createInscriptionCycle(@Valid @RequestBody InscriptionCycleDTO inscriptionCycleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InscriptionCycle : {}", inscriptionCycleDTO);
        if (inscriptionCycleDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscriptionCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inscriptionCycleDTO = inscriptionCycleService.save(inscriptionCycleDTO);
        return ResponseEntity.created(new URI("/api/inscription-cycles/" + inscriptionCycleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inscriptionCycleDTO.getId().toString()))
            .body(inscriptionCycleDTO);
    }

    /**
     * {@code PUT  /inscription-cycles/:id} : Updates an existing inscriptionCycle.
     *
     * @param id the id of the inscriptionCycleDTO to save.
     * @param inscriptionCycleDTO the inscriptionCycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionCycleDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionCycleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionCycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InscriptionCycleDTO> updateInscriptionCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InscriptionCycleDTO inscriptionCycleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InscriptionCycle : {}, {}", id, inscriptionCycleDTO);
        if (inscriptionCycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionCycleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inscriptionCycleDTO = inscriptionCycleService.update(inscriptionCycleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionCycleDTO.getId().toString()))
            .body(inscriptionCycleDTO);
    }

    /**
     * {@code PATCH  /inscription-cycles/:id} : Partial updates given fields of an existing inscriptionCycle, field will ignore if it is null
     *
     * @param id the id of the inscriptionCycleDTO to save.
     * @param inscriptionCycleDTO the inscriptionCycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inscriptionCycleDTO,
     * or with status {@code 400 (Bad Request)} if the inscriptionCycleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inscriptionCycleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inscriptionCycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InscriptionCycleDTO> partialUpdateInscriptionCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InscriptionCycleDTO inscriptionCycleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InscriptionCycle partially : {}, {}", id, inscriptionCycleDTO);
        if (inscriptionCycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inscriptionCycleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inscriptionCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InscriptionCycleDTO> result = inscriptionCycleService.partialUpdate(inscriptionCycleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inscriptionCycleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inscription-cycles} : get all the inscriptionCycles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inscriptionCycles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InscriptionCycleDTO>> getAllInscriptionCycles(
        InscriptionCycleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InscriptionCycles by criteria: {}", criteria);

        Page<InscriptionCycleDTO> page = inscriptionCycleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inscription-cycles/count} : count all the inscriptionCycles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInscriptionCycles(InscriptionCycleCriteria criteria) {
        LOG.debug("REST request to count InscriptionCycles by criteria: {}", criteria);
        return ResponseEntity.ok().body(inscriptionCycleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inscription-cycles/:id} : get the "id" inscriptionCycle.
     *
     * @param id the id of the inscriptionCycleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inscriptionCycleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionCycleDTO> getInscriptionCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InscriptionCycle : {}", id);
        Optional<InscriptionCycleDTO> inscriptionCycleDTO = inscriptionCycleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionCycleDTO);
    }

    /**
     * {@code DELETE  /inscription-cycles/:id} : delete the "id" inscriptionCycle.
     *
     * @param id the id of the inscriptionCycleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscriptionCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InscriptionCycle : {}", id);
        inscriptionCycleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
