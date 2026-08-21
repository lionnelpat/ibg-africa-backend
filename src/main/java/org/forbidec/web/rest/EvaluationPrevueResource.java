package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.EvaluationPrevueRepository;
import org.forbidec.service.EvaluationPrevueQueryService;
import org.forbidec.service.EvaluationPrevueService;
import org.forbidec.service.criteria.EvaluationPrevueCriteria;
import org.forbidec.service.dto.EvaluationPrevueDTO;
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
 * REST controller for managing {@link org.forbidec.domain.EvaluationPrevue}.
 */
@RestController
@RequestMapping("/api/evaluation-prevues")
public class EvaluationPrevueResource {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationPrevueResource.class);

    private static final String ENTITY_NAME = "evaluationPrevue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaluationPrevueService evaluationPrevueService;

    private final EvaluationPrevueRepository evaluationPrevueRepository;

    private final EvaluationPrevueQueryService evaluationPrevueQueryService;

    public EvaluationPrevueResource(
        EvaluationPrevueService evaluationPrevueService,
        EvaluationPrevueRepository evaluationPrevueRepository,
        EvaluationPrevueQueryService evaluationPrevueQueryService
    ) {
        this.evaluationPrevueService = evaluationPrevueService;
        this.evaluationPrevueRepository = evaluationPrevueRepository;
        this.evaluationPrevueQueryService = evaluationPrevueQueryService;
    }

    /**
     * {@code POST  /evaluation-prevues} : Create a new evaluationPrevue.
     *
     * @param evaluationPrevueDTO the evaluationPrevueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaluationPrevueDTO, or with status {@code 400 (Bad Request)} if the evaluationPrevue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EvaluationPrevueDTO> createEvaluationPrevue(@Valid @RequestBody EvaluationPrevueDTO evaluationPrevueDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EvaluationPrevue : {}", evaluationPrevueDTO);
        if (evaluationPrevueDTO.getId() != null) {
            throw new BadRequestAlertException("A new evaluationPrevue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        evaluationPrevueDTO = evaluationPrevueService.save(evaluationPrevueDTO);
        return ResponseEntity.created(new URI("/api/evaluation-prevues/" + evaluationPrevueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, evaluationPrevueDTO.getId().toString()))
            .body(evaluationPrevueDTO);
    }

    /**
     * {@code PUT  /evaluation-prevues/:id} : Updates an existing evaluationPrevue.
     *
     * @param id the id of the evaluationPrevueDTO to save.
     * @param evaluationPrevueDTO the evaluationPrevueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluationPrevueDTO,
     * or with status {@code 400 (Bad Request)} if the evaluationPrevueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaluationPrevueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationPrevueDTO> updateEvaluationPrevue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EvaluationPrevueDTO evaluationPrevueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EvaluationPrevue : {}, {}", id, evaluationPrevueDTO);
        if (evaluationPrevueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluationPrevueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluationPrevueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        evaluationPrevueDTO = evaluationPrevueService.update(evaluationPrevueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluationPrevueDTO.getId().toString()))
            .body(evaluationPrevueDTO);
    }

    /**
     * {@code PATCH  /evaluation-prevues/:id} : Partial updates given fields of an existing evaluationPrevue, field will ignore if it is null
     *
     * @param id the id of the evaluationPrevueDTO to save.
     * @param evaluationPrevueDTO the evaluationPrevueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluationPrevueDTO,
     * or with status {@code 400 (Bad Request)} if the evaluationPrevueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the evaluationPrevueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaluationPrevueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvaluationPrevueDTO> partialUpdateEvaluationPrevue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EvaluationPrevueDTO evaluationPrevueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EvaluationPrevue partially : {}, {}", id, evaluationPrevueDTO);
        if (evaluationPrevueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluationPrevueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluationPrevueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvaluationPrevueDTO> result = evaluationPrevueService.partialUpdate(evaluationPrevueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluationPrevueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /evaluation-prevues} : get all the evaluationPrevues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaluationPrevues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EvaluationPrevueDTO>> getAllEvaluationPrevues(
        EvaluationPrevueCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EvaluationPrevues by criteria: {}", criteria);

        Page<EvaluationPrevueDTO> page = evaluationPrevueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evaluation-prevues/count} : count all the evaluationPrevues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEvaluationPrevues(EvaluationPrevueCriteria criteria) {
        LOG.debug("REST request to count EvaluationPrevues by criteria: {}", criteria);
        return ResponseEntity.ok().body(evaluationPrevueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evaluation-prevues/:id} : get the "id" evaluationPrevue.
     *
     * @param id the id of the evaluationPrevueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaluationPrevueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationPrevueDTO> getEvaluationPrevue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EvaluationPrevue : {}", id);
        Optional<EvaluationPrevueDTO> evaluationPrevueDTO = evaluationPrevueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evaluationPrevueDTO);
    }

    /**
     * {@code DELETE  /evaluation-prevues/:id} : delete the "id" evaluationPrevue.
     *
     * @param id the id of the evaluationPrevueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluationPrevue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EvaluationPrevue : {}", id);
        evaluationPrevueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
