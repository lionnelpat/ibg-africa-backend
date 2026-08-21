package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.CycleRepository;
import org.forbidec.service.CycleQueryService;
import org.forbidec.service.CycleService;
import org.forbidec.service.criteria.CycleCriteria;
import org.forbidec.service.dto.CycleDTO;
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
 * REST controller for managing {@link org.forbidec.domain.Cycle}.
 */
@RestController
@RequestMapping("/api/cycles")
public class CycleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CycleResource.class);

    private static final String ENTITY_NAME = "cycle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CycleService cycleService;

    private final CycleRepository cycleRepository;

    private final CycleQueryService cycleQueryService;

    public CycleResource(CycleService cycleService, CycleRepository cycleRepository, CycleQueryService cycleQueryService) {
        this.cycleService = cycleService;
        this.cycleRepository = cycleRepository;
        this.cycleQueryService = cycleQueryService;
    }

    /**
     * {@code POST  /cycles} : Create a new cycle.
     *
     * @param cycleDTO the cycleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cycleDTO, or with status {@code 400 (Bad Request)} if the cycle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CycleDTO> createCycle(@Valid @RequestBody CycleDTO cycleDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cycle : {}", cycleDTO);
        if (cycleDTO.getId() != null) {
            throw new BadRequestAlertException("A new cycle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cycleDTO = cycleService.save(cycleDTO);
        return ResponseEntity.created(new URI("/api/cycles/" + cycleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cycleDTO.getId().toString()))
            .body(cycleDTO);
    }

    /**
     * {@code PUT  /cycles/:id} : Updates an existing cycle.
     *
     * @param id the id of the cycleDTO to save.
     * @param cycleDTO the cycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDTO,
     * or with status {@code 400 (Bad Request)} if the cycleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CycleDTO> updateCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CycleDTO cycleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cycle : {}, {}", id, cycleDTO);
        if (cycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cycleDTO = cycleService.update(cycleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycleDTO.getId().toString()))
            .body(cycleDTO);
    }

    /**
     * {@code PATCH  /cycles/:id} : Partial updates given fields of an existing cycle, field will ignore if it is null
     *
     * @param id the id of the cycleDTO to save.
     * @param cycleDTO the cycleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDTO,
     * or with status {@code 400 (Bad Request)} if the cycleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cycleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cycleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CycleDTO> partialUpdateCycle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CycleDTO cycleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cycle partially : {}, {}", id, cycleDTO);
        if (cycleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CycleDTO> result = cycleService.partialUpdate(cycleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cycles} : get all the cycles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cycles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CycleDTO>> getAllCycles(
        CycleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cycles by criteria: {}", criteria);

        Page<CycleDTO> page = cycleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cycles/count} : count all the cycles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCycles(CycleCriteria criteria) {
        LOG.debug("REST request to count Cycles by criteria: {}", criteria);
        return ResponseEntity.ok().body(cycleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cycles/:id} : get the "id" cycle.
     *
     * @param id the id of the cycleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cycleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CycleDTO> getCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cycle : {}", id);
        Optional<CycleDTO> cycleDTO = cycleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cycleDTO);
    }

    /**
     * {@code DELETE  /cycles/:id} : delete the "id" cycle.
     *
     * @param id the id of the cycleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCycle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cycle : {}", id);
        cycleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
