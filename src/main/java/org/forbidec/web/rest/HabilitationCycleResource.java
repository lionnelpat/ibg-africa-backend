package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.domain.HabilitationCycle;
import org.forbidec.repository.HabilitationCycleRepository;
import org.forbidec.service.HabilitationCycleService;
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
 * REST controller for managing {@link org.forbidec.domain.HabilitationCycle}.
 */
@RestController
@RequestMapping("/api/habilitation-cycles")
public class HabilitationCycleResource {

    private static final Logger LOG = LoggerFactory.getLogger(HabilitationCycleResource.class);

    private static final String ENTITY_NAME = "habilitationCycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HabilitationCycleService habilitationCycleService;

    private final HabilitationCycleRepository habilitationCycleRepository;

    public HabilitationCycleResource(
        HabilitationCycleService habilitationCycleService,
        HabilitationCycleRepository habilitationCycleRepository
    ) {
        this.habilitationCycleService = habilitationCycleService;
        this.habilitationCycleRepository = habilitationCycleRepository;
    }

    /**
     * {@code POST  /habilitation-cycles} : Create a new habilitationCycle.
     *
     * @param habilitationCycle the habilitationCycle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new habilitationCycle, or with status {@code 400 (Bad Request)} if the habilitationCycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HabilitationCycle> createHabilitationCycle(@Valid @RequestBody HabilitationCycle habilitationCycle)
        throws URISyntaxException {
        LOG.debug("REST request to save HabilitationCycle : {}", habilitationCycle);
        if (habilitationCycle.getId() != null) {
            throw new BadRequestAlertException("A new habilitationCycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        habilitationCycle = habilitationCycleService.save(habilitationCycle);
        return ResponseEntity.created(new URI("/api/habilitation-cycles/" + habilitationCycle.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, habilitationCycle.getId().toString()))
            .body(habilitationCycle);
    }

    /**
     * {@code PUT  /habilitation-cycles/:id} : Updates an existing habilitationCycle.
     *
     * @param id the id of the habilitationCycle to save.
     * @param habilitationCycle the habilitationCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habilitationCycle,
     * or with status {@code 400 (Bad Request)} if the habilitationCycle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the habilitationCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HabilitationCycle> updateHabilitationCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HabilitationCycle habilitationCycle
    ) throws URISyntaxException {
        LOG.debug("REST request to update HabilitationCycle : {}, {}", id, habilitationCycle);
        if (habilitationCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, habilitationCycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!habilitationCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        habilitationCycle = habilitationCycleService.update(habilitationCycle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, habilitationCycle.getId().toString()))
            .body(habilitationCycle);
    }

    /**
     * {@code PATCH  /habilitation-cycles/:id} : Partial updates given fields of an existing habilitationCycle, field will ignore if it is null
     *
     * @param id the id of the habilitationCycle to save.
     * @param habilitationCycle the habilitationCycle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated habilitationCycle,
     * or with status {@code 400 (Bad Request)} if the habilitationCycle is not valid,
     * or with status {@code 404 (Not Found)} if the habilitationCycle is not found,
     * or with status {@code 500 (Internal Server Error)} if the habilitationCycle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HabilitationCycle> partialUpdateHabilitationCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HabilitationCycle habilitationCycle
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HabilitationCycle partially : {}, {}", id, habilitationCycle);
        if (habilitationCycle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, habilitationCycle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!habilitationCycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HabilitationCycle> result = habilitationCycleService.partialUpdate(habilitationCycle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, habilitationCycle.getId().toString())
        );
    }

    /**
     * {@code GET  /habilitation-cycles} : get all the habilitationCycles.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of habilitationCycles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HabilitationCycle>> getAllHabilitationCycles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of HabilitationCycles");
        Page<HabilitationCycle> page;
        if (eagerload) {
            page = habilitationCycleService.findAllWithEagerRelationships(pageable);
        } else {
            page = habilitationCycleService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /habilitation-cycles/:id} : get the "id" habilitationCycle.
     *
     * @param id the id of the habilitationCycle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the habilitationCycle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HabilitationCycle> getHabilitationCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HabilitationCycle : {}", id);
        Optional<HabilitationCycle> habilitationCycle = habilitationCycleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(habilitationCycle);
    }

    /**
     * {@code DELETE  /habilitation-cycles/:id} : delete the "id" habilitationCycle.
     *
     * @param id the id of the habilitationCycle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabilitationCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HabilitationCycle : {}", id);
        habilitationCycleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
