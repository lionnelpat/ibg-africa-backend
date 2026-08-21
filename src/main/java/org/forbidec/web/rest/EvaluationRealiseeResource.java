package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.EvaluationRealiseeRepository;
import org.forbidec.service.EvaluationRealiseeQueryService;
import org.forbidec.service.EvaluationRealiseeService;
import org.forbidec.service.criteria.EvaluationRealiseeCriteria;
import org.forbidec.service.dto.EvaluationRealiseeDTO;
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
 * REST controller for managing {@link org.forbidec.domain.EvaluationRealisee}.
 */
@RestController
@RequestMapping("/api/evaluation-realisees")
public class EvaluationRealiseeResource {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationRealiseeResource.class);

    private static final String ENTITY_NAME = "evaluationRealisee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaluationRealiseeService evaluationRealiseeService;

    private final EvaluationRealiseeRepository evaluationRealiseeRepository;

    private final EvaluationRealiseeQueryService evaluationRealiseeQueryService;

    public EvaluationRealiseeResource(
        EvaluationRealiseeService evaluationRealiseeService,
        EvaluationRealiseeRepository evaluationRealiseeRepository,
        EvaluationRealiseeQueryService evaluationRealiseeQueryService
    ) {
        this.evaluationRealiseeService = evaluationRealiseeService;
        this.evaluationRealiseeRepository = evaluationRealiseeRepository;
        this.evaluationRealiseeQueryService = evaluationRealiseeQueryService;
    }

    /**
     * {@code POST  /evaluation-realisees} : Create a new evaluationRealisee.
     *
     * @param evaluationRealiseeDTO the evaluationRealiseeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaluationRealiseeDTO, or with status {@code 400 (Bad Request)} if the evaluationRealisee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EvaluationRealiseeDTO> createEvaluationRealisee(@Valid @RequestBody EvaluationRealiseeDTO evaluationRealiseeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EvaluationRealisee : {}", evaluationRealiseeDTO);
        if (evaluationRealiseeDTO.getId() != null) {
            throw new BadRequestAlertException("A new evaluationRealisee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        evaluationRealiseeDTO = evaluationRealiseeService.save(evaluationRealiseeDTO);
        return ResponseEntity.created(new URI("/api/evaluation-realisees/" + evaluationRealiseeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, evaluationRealiseeDTO.getId().toString()))
            .body(evaluationRealiseeDTO);
    }

    /**
     * {@code PUT  /evaluation-realisees/:id} : Updates an existing evaluationRealisee.
     *
     * @param id the id of the evaluationRealiseeDTO to save.
     * @param evaluationRealiseeDTO the evaluationRealiseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluationRealiseeDTO,
     * or with status {@code 400 (Bad Request)} if the evaluationRealiseeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaluationRealiseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationRealiseeDTO> updateEvaluationRealisee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EvaluationRealiseeDTO evaluationRealiseeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EvaluationRealisee : {}, {}", id, evaluationRealiseeDTO);
        if (evaluationRealiseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluationRealiseeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluationRealiseeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        evaluationRealiseeDTO = evaluationRealiseeService.update(evaluationRealiseeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluationRealiseeDTO.getId().toString()))
            .body(evaluationRealiseeDTO);
    }

    /**
     * {@code PATCH  /evaluation-realisees/:id} : Partial updates given fields of an existing evaluationRealisee, field will ignore if it is null
     *
     * @param id the id of the evaluationRealiseeDTO to save.
     * @param evaluationRealiseeDTO the evaluationRealiseeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluationRealiseeDTO,
     * or with status {@code 400 (Bad Request)} if the evaluationRealiseeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the evaluationRealiseeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaluationRealiseeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvaluationRealiseeDTO> partialUpdateEvaluationRealisee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EvaluationRealiseeDTO evaluationRealiseeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EvaluationRealisee partially : {}, {}", id, evaluationRealiseeDTO);
        if (evaluationRealiseeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluationRealiseeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluationRealiseeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvaluationRealiseeDTO> result = evaluationRealiseeService.partialUpdate(evaluationRealiseeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluationRealiseeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /evaluation-realisees} : get all the evaluationRealisees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaluationRealisees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EvaluationRealiseeDTO>> getAllEvaluationRealisees(
        EvaluationRealiseeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EvaluationRealisees by criteria: {}", criteria);

        Page<EvaluationRealiseeDTO> page = evaluationRealiseeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evaluation-realisees/count} : count all the evaluationRealisees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEvaluationRealisees(EvaluationRealiseeCriteria criteria) {
        LOG.debug("REST request to count EvaluationRealisees by criteria: {}", criteria);
        return ResponseEntity.ok().body(evaluationRealiseeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evaluation-realisees/:id} : get the "id" evaluationRealisee.
     *
     * @param id the id of the evaluationRealiseeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaluationRealiseeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationRealiseeDTO> getEvaluationRealisee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EvaluationRealisee : {}", id);
        Optional<EvaluationRealiseeDTO> evaluationRealiseeDTO = evaluationRealiseeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evaluationRealiseeDTO);
    }

    /**
     * {@code DELETE  /evaluation-realisees/:id} : delete the "id" evaluationRealisee.
     *
     * @param id the id of the evaluationRealiseeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationRealisee(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EvaluationRealisee : {}", id);
        evaluationRealiseeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
