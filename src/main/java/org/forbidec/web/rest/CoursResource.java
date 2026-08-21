package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.CoursRepository;
import org.forbidec.service.CoursQueryService;
import org.forbidec.service.CoursService;
import org.forbidec.service.criteria.CoursCriteria;
import org.forbidec.service.dto.CoursDTO;
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
 * REST controller for managing {@link org.forbidec.domain.Cours}.
 */
@RestController
@RequestMapping("/api/cours")
public class CoursResource {

    private static final Logger LOG = LoggerFactory.getLogger(CoursResource.class);

    private static final String ENTITY_NAME = "cours";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoursService coursService;

    private final CoursRepository coursRepository;

    private final CoursQueryService coursQueryService;

    public CoursResource(CoursService coursService, CoursRepository coursRepository, CoursQueryService coursQueryService) {
        this.coursService = coursService;
        this.coursRepository = coursRepository;
        this.coursQueryService = coursQueryService;
    }

    /**
     * {@code POST  /cours} : Create a new cours.
     *
     * @param coursDTO the coursDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coursDTO, or with status {@code 400 (Bad Request)} if the cours has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CoursDTO> createCours(@Valid @RequestBody CoursDTO coursDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cours : {}", coursDTO);
        if (coursDTO.getId() != null) {
            throw new BadRequestAlertException("A new cours cannot already have an ID", ENTITY_NAME, "idexists");
        }
        coursDTO = coursService.save(coursDTO);
        return ResponseEntity.created(new URI("/api/cours/" + coursDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, coursDTO.getId().toString()))
            .body(coursDTO);
    }

    /**
     * {@code PUT  /cours/:id} : Updates an existing cours.
     *
     * @param id the id of the coursDTO to save.
     * @param coursDTO the coursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coursDTO,
     * or with status {@code 400 (Bad Request)} if the coursDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CoursDTO> updateCours(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CoursDTO coursDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cours : {}, {}", id, coursDTO);
        if (coursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        coursDTO = coursService.update(coursDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coursDTO.getId().toString()))
            .body(coursDTO);
    }

    /**
     * {@code PATCH  /cours/:id} : Partial updates given fields of an existing cours, field will ignore if it is null
     *
     * @param id the id of the coursDTO to save.
     * @param coursDTO the coursDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coursDTO,
     * or with status {@code 400 (Bad Request)} if the coursDTO is not valid,
     * or with status {@code 404 (Not Found)} if the coursDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the coursDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CoursDTO> partialUpdateCours(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CoursDTO coursDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cours partially : {}, {}", id, coursDTO);
        if (coursDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coursDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoursDTO> result = coursService.partialUpdate(coursDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coursDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cours} : get all the cours.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cours in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CoursDTO>> getAllCours(
        CoursCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cours by criteria: {}", criteria);

        Page<CoursDTO> page = coursQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cours/count} : count all the cours.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCours(CoursCriteria criteria) {
        LOG.debug("REST request to count Cours by criteria: {}", criteria);
        return ResponseEntity.ok().body(coursQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cours/:id} : get the "id" cours.
     *
     * @param id the id of the coursDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coursDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CoursDTO> getCours(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cours : {}", id);
        Optional<CoursDTO> coursDTO = coursService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coursDTO);
    }

    /**
     * {@code DELETE  /cours/:id} : delete the "id" cours.
     *
     * @param id the id of the coursDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cours : {}", id);
        coursService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
