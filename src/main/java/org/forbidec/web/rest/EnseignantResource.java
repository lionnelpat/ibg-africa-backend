package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.EnseignantRepository;
import org.forbidec.service.EnseignantQueryService;
import org.forbidec.service.EnseignantService;
import org.forbidec.service.criteria.EnseignantCriteria;
import org.forbidec.service.dto.EnseignantDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.forbidec.web.rest.util.PhotoValidator;
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
 * REST controller for managing {@link org.forbidec.domain.Enseignant}.
 */
@RestController
@RequestMapping("/api/enseignants")
public class EnseignantResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnseignantResource.class);

    private static final String ENTITY_NAME = "enseignant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnseignantService enseignantService;

    private final EnseignantRepository enseignantRepository;

    private final EnseignantQueryService enseignantQueryService;

    public EnseignantResource(
        EnseignantService enseignantService,
        EnseignantRepository enseignantRepository,
        EnseignantQueryService enseignantQueryService
    ) {
        this.enseignantService = enseignantService;
        this.enseignantRepository = enseignantRepository;
        this.enseignantQueryService = enseignantQueryService;
    }

    /**
     * {@code POST  /enseignants} : Create a new enseignant.
     *
     * @param enseignantDTO the enseignantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enseignantDTO, or with status {@code 400 (Bad Request)} if the enseignant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnseignantDTO> createEnseignant(@Valid @RequestBody EnseignantDTO enseignantDTO) throws URISyntaxException {
        LOG.debug("REST request to save Enseignant : {}", enseignantDTO);
        if (enseignantDTO.getId() != null) {
            throw new BadRequestAlertException("A new enseignant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhotoValidator.verifier(enseignantDTO.getPhoto(), enseignantDTO.getPhotoContentType(), ENTITY_NAME);
        enseignantDTO = enseignantService.save(enseignantDTO);
        return ResponseEntity.created(new URI("/api/enseignants/" + enseignantDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, enseignantDTO.getId().toString()))
            .body(enseignantDTO);
    }

    /**
     * {@code PUT  /enseignants/:id} : Updates an existing enseignant.
     *
     * @param id the id of the enseignantDTO to save.
     * @param enseignantDTO the enseignantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enseignantDTO,
     * or with status {@code 400 (Bad Request)} if the enseignantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enseignantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnseignantDTO> updateEnseignant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnseignantDTO enseignantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Enseignant : {}, {}", id, enseignantDTO);
        if (enseignantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enseignantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enseignantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        PhotoValidator.verifier(enseignantDTO.getPhoto(), enseignantDTO.getPhotoContentType(), ENTITY_NAME);

        enseignantDTO = enseignantService.update(enseignantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enseignantDTO.getId().toString()))
            .body(enseignantDTO);
    }

    /**
     * {@code PATCH  /enseignants/:id} : Partial updates given fields of an existing enseignant, field will ignore if it is null
     *
     * @param id the id of the enseignantDTO to save.
     * @param enseignantDTO the enseignantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enseignantDTO,
     * or with status {@code 400 (Bad Request)} if the enseignantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enseignantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enseignantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnseignantDTO> partialUpdateEnseignant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnseignantDTO enseignantDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Enseignant partially : {}, {}", id, enseignantDTO);
        if (enseignantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enseignantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enseignantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        PhotoValidator.verifier(enseignantDTO.getPhoto(), enseignantDTO.getPhotoContentType(), ENTITY_NAME);

        Optional<EnseignantDTO> result = enseignantService.partialUpdate(enseignantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enseignantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enseignants} : get all the enseignants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enseignants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EnseignantDTO>> getAllEnseignants(
        EnseignantCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Enseignants by criteria: {}", criteria);

        Page<EnseignantDTO> page = enseignantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enseignants/count} : count all the enseignants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEnseignants(EnseignantCriteria criteria) {
        LOG.debug("REST request to count Enseignants by criteria: {}", criteria);
        return ResponseEntity.ok().body(enseignantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /enseignants/:id} : get the "id" enseignant.
     *
     * @param id the id of the enseignantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enseignantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnseignantDTO> getEnseignant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Enseignant : {}", id);
        Optional<EnseignantDTO> enseignantDTO = enseignantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enseignantDTO);
    }

    /**
     * {@code DELETE  /enseignants/:id} : delete the "id" enseignant.
     *
     * @param id the id of the enseignantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Enseignant : {}", id);
        enseignantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
