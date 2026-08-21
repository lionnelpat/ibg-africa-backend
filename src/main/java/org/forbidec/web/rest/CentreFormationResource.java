package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.CentreFormationRepository;
import org.forbidec.service.CentreFormationService;
import org.forbidec.service.dto.CentreFormationDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.CentreFormation}.
 */
@RestController
@RequestMapping("/api/centre-formations")
public class CentreFormationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CentreFormationResource.class);

    private static final String ENTITY_NAME = "centreFormation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CentreFormationService centreFormationService;

    private final CentreFormationRepository centreFormationRepository;

    public CentreFormationResource(CentreFormationService centreFormationService, CentreFormationRepository centreFormationRepository) {
        this.centreFormationService = centreFormationService;
        this.centreFormationRepository = centreFormationRepository;
    }

    /**
     * {@code POST  /centre-formations} : Create a new centreFormation.
     *
     * @param centreFormationDTO the centreFormationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new centreFormationDTO, or with status {@code 400 (Bad Request)} if the centreFormation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CentreFormationDTO> createCentreFormation(@Valid @RequestBody CentreFormationDTO centreFormationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CentreFormation : {}", centreFormationDTO);
        if (centreFormationDTO.getId() != null) {
            throw new BadRequestAlertException("A new centreFormation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        centreFormationDTO = centreFormationService.save(centreFormationDTO);
        return ResponseEntity.created(new URI("/api/centre-formations/" + centreFormationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, centreFormationDTO.getId().toString()))
            .body(centreFormationDTO);
    }

    /**
     * {@code PUT  /centre-formations/:id} : Updates an existing centreFormation.
     *
     * @param id the id of the centreFormationDTO to save.
     * @param centreFormationDTO the centreFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centreFormationDTO,
     * or with status {@code 400 (Bad Request)} if the centreFormationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the centreFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CentreFormationDTO> updateCentreFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CentreFormationDTO centreFormationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CentreFormation : {}, {}", id, centreFormationDTO);
        if (centreFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centreFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centreFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        centreFormationDTO = centreFormationService.update(centreFormationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centreFormationDTO.getId().toString()))
            .body(centreFormationDTO);
    }

    /**
     * {@code PATCH  /centre-formations/:id} : Partial updates given fields of an existing centreFormation, field will ignore if it is null
     *
     * @param id the id of the centreFormationDTO to save.
     * @param centreFormationDTO the centreFormationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centreFormationDTO,
     * or with status {@code 400 (Bad Request)} if the centreFormationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the centreFormationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the centreFormationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CentreFormationDTO> partialUpdateCentreFormation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CentreFormationDTO centreFormationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CentreFormation partially : {}, {}", id, centreFormationDTO);
        if (centreFormationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, centreFormationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!centreFormationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CentreFormationDTO> result = centreFormationService.partialUpdate(centreFormationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centreFormationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /centre-formations} : get all the centreFormations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of centreFormations in body.
     */
    @GetMapping("")
    public List<CentreFormationDTO> getAllCentreFormations(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all CentreFormations");
        return centreFormationService.findAll();
    }

    /**
     * {@code GET  /centre-formations/:id} : get the "id" centreFormation.
     *
     * @param id the id of the centreFormationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the centreFormationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CentreFormationDTO> getCentreFormation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CentreFormation : {}", id);
        Optional<CentreFormationDTO> centreFormationDTO = centreFormationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(centreFormationDTO);
    }

    /**
     * {@code DELETE  /centre-formations/:id} : delete the "id" centreFormation.
     *
     * @param id the id of the centreFormationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCentreFormation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CentreFormation : {}", id);
        centreFormationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
