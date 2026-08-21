package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.TypeTacheRepository;
import org.forbidec.service.TypeTacheService;
import org.forbidec.service.dto.TypeTacheDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.TypeTache}.
 */
@RestController
@RequestMapping("/api/type-taches")
public class TypeTacheResource {

    private static final Logger LOG = LoggerFactory.getLogger(TypeTacheResource.class);

    private static final String ENTITY_NAME = "typeTache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeTacheService typeTacheService;

    private final TypeTacheRepository typeTacheRepository;

    public TypeTacheResource(TypeTacheService typeTacheService, TypeTacheRepository typeTacheRepository) {
        this.typeTacheService = typeTacheService;
        this.typeTacheRepository = typeTacheRepository;
    }

    /**
     * {@code POST  /type-taches} : Create a new typeTache.
     *
     * @param typeTacheDTO the typeTacheDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeTacheDTO, or with status {@code 400 (Bad Request)} if the typeTache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeTacheDTO> createTypeTache(@Valid @RequestBody TypeTacheDTO typeTacheDTO) throws URISyntaxException {
        LOG.debug("REST request to save TypeTache : {}", typeTacheDTO);
        if (typeTacheDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeTache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeTacheDTO = typeTacheService.save(typeTacheDTO);
        return ResponseEntity.created(new URI("/api/type-taches/" + typeTacheDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, typeTacheDTO.getId().toString()))
            .body(typeTacheDTO);
    }

    /**
     * {@code PUT  /type-taches/:id} : Updates an existing typeTache.
     *
     * @param id the id of the typeTacheDTO to save.
     * @param typeTacheDTO the typeTacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeTacheDTO,
     * or with status {@code 400 (Bad Request)} if the typeTacheDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeTacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeTacheDTO> updateTypeTache(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeTacheDTO typeTacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TypeTache : {}, {}", id, typeTacheDTO);
        if (typeTacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeTacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeTacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeTacheDTO = typeTacheService.update(typeTacheDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeTacheDTO.getId().toString()))
            .body(typeTacheDTO);
    }

    /**
     * {@code PATCH  /type-taches/:id} : Partial updates given fields of an existing typeTache, field will ignore if it is null
     *
     * @param id the id of the typeTacheDTO to save.
     * @param typeTacheDTO the typeTacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeTacheDTO,
     * or with status {@code 400 (Bad Request)} if the typeTacheDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeTacheDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeTacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeTacheDTO> partialUpdateTypeTache(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeTacheDTO typeTacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TypeTache partially : {}, {}", id, typeTacheDTO);
        if (typeTacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeTacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeTacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeTacheDTO> result = typeTacheService.partialUpdate(typeTacheDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeTacheDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-taches} : get all the typeTaches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeTaches in body.
     */
    @GetMapping("")
    public List<TypeTacheDTO> getAllTypeTaches() {
        LOG.debug("REST request to get all TypeTaches");
        return typeTacheService.findAll();
    }

    /**
     * {@code GET  /type-taches/:id} : get the "id" typeTache.
     *
     * @param id the id of the typeTacheDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeTacheDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeTacheDTO> getTypeTache(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TypeTache : {}", id);
        Optional<TypeTacheDTO> typeTacheDTO = typeTacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeTacheDTO);
    }

    /**
     * {@code DELETE  /type-taches/:id} : delete the "id" typeTache.
     *
     * @param id the id of the typeTacheDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeTache(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TypeTache : {}", id);
        typeTacheService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
