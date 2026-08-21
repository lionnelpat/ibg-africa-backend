package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.BaremeMentionRepository;
import org.forbidec.service.BaremeMentionService;
import org.forbidec.service.dto.BaremeMentionDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.BaremeMention}.
 */
@RestController
@RequestMapping("/api/bareme-mentions")
public class BaremeMentionResource {

    private static final Logger LOG = LoggerFactory.getLogger(BaremeMentionResource.class);

    private static final String ENTITY_NAME = "baremeMention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BaremeMentionService baremeMentionService;

    private final BaremeMentionRepository baremeMentionRepository;

    public BaremeMentionResource(BaremeMentionService baremeMentionService, BaremeMentionRepository baremeMentionRepository) {
        this.baremeMentionService = baremeMentionService;
        this.baremeMentionRepository = baremeMentionRepository;
    }

    /**
     * {@code POST  /bareme-mentions} : Create a new baremeMention.
     *
     * @param baremeMentionDTO the baremeMentionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new baremeMentionDTO, or with status {@code 400 (Bad Request)} if the baremeMention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BaremeMentionDTO> createBaremeMention(@Valid @RequestBody BaremeMentionDTO baremeMentionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BaremeMention : {}", baremeMentionDTO);
        if (baremeMentionDTO.getId() != null) {
            throw new BadRequestAlertException("A new baremeMention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        baremeMentionDTO = baremeMentionService.save(baremeMentionDTO);
        return ResponseEntity.created(new URI("/api/bareme-mentions/" + baremeMentionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, baremeMentionDTO.getId().toString()))
            .body(baremeMentionDTO);
    }

    /**
     * {@code PUT  /bareme-mentions/:id} : Updates an existing baremeMention.
     *
     * @param id the id of the baremeMentionDTO to save.
     * @param baremeMentionDTO the baremeMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baremeMentionDTO,
     * or with status {@code 400 (Bad Request)} if the baremeMentionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the baremeMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaremeMentionDTO> updateBaremeMention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BaremeMentionDTO baremeMentionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BaremeMention : {}, {}", id, baremeMentionDTO);
        if (baremeMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baremeMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baremeMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        baremeMentionDTO = baremeMentionService.update(baremeMentionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baremeMentionDTO.getId().toString()))
            .body(baremeMentionDTO);
    }

    /**
     * {@code PATCH  /bareme-mentions/:id} : Partial updates given fields of an existing baremeMention, field will ignore if it is null
     *
     * @param id the id of the baremeMentionDTO to save.
     * @param baremeMentionDTO the baremeMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated baremeMentionDTO,
     * or with status {@code 400 (Bad Request)} if the baremeMentionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the baremeMentionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the baremeMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BaremeMentionDTO> partialUpdateBaremeMention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BaremeMentionDTO baremeMentionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BaremeMention partially : {}, {}", id, baremeMentionDTO);
        if (baremeMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, baremeMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!baremeMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BaremeMentionDTO> result = baremeMentionService.partialUpdate(baremeMentionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, baremeMentionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bareme-mentions} : get all the baremeMentions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baremeMentions in body.
     */
    @GetMapping("")
    public List<BaremeMentionDTO> getAllBaremeMentions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all BaremeMentions");
        return baremeMentionService.findAll();
    }

    /**
     * {@code GET  /bareme-mentions/:id} : get the "id" baremeMention.
     *
     * @param id the id of the baremeMentionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the baremeMentionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaremeMentionDTO> getBaremeMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BaremeMention : {}", id);
        Optional<BaremeMentionDTO> baremeMentionDTO = baremeMentionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(baremeMentionDTO);
    }

    /**
     * {@code DELETE  /bareme-mentions/:id} : delete the "id" baremeMention.
     *
     * @param id the id of the baremeMentionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBaremeMention(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BaremeMention : {}", id);
        baremeMentionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
