package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.domain.Parametre;
import org.forbidec.repository.ParametreRepository;
import org.forbidec.service.ParametreService;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.Parametre}.
 */
@RestController
@RequestMapping("/api/parametres")
public class ParametreResource {

    private static final Logger LOG = LoggerFactory.getLogger(ParametreResource.class);

    private static final String ENTITY_NAME = "parametre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParametreService parametreService;

    private final ParametreRepository parametreRepository;

    public ParametreResource(ParametreService parametreService, ParametreRepository parametreRepository) {
        this.parametreService = parametreService;
        this.parametreRepository = parametreRepository;
    }

    /**
     * {@code POST  /parametres} : Create a new parametre.
     *
     * @param parametre the parametre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parametre, or with status {@code 400 (Bad Request)} if the parametre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Parametre> createParametre(@Valid @RequestBody Parametre parametre) throws URISyntaxException {
        LOG.debug("REST request to save Parametre : {}", parametre);
        if (parametre.getId() != null) {
            throw new BadRequestAlertException("A new parametre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        parametre = parametreService.save(parametre);
        return ResponseEntity.created(new URI("/api/parametres/" + parametre.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, parametre.getId().toString()))
            .body(parametre);
    }

    /**
     * {@code PUT  /parametres/:id} : Updates an existing parametre.
     *
     * @param id the id of the parametre to save.
     * @param parametre the parametre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametre,
     * or with status {@code 400 (Bad Request)} if the parametre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parametre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Parametre> updateParametre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Parametre parametre
    ) throws URISyntaxException {
        LOG.debug("REST request to update Parametre : {}, {}", id, parametre);
        if (parametre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        parametre = parametreService.update(parametre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametre.getId().toString()))
            .body(parametre);
    }

    /**
     * {@code PATCH  /parametres/:id} : Partial updates given fields of an existing parametre, field will ignore if it is null
     *
     * @param id the id of the parametre to save.
     * @param parametre the parametre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parametre,
     * or with status {@code 400 (Bad Request)} if the parametre is not valid,
     * or with status {@code 404 (Not Found)} if the parametre is not found,
     * or with status {@code 500 (Internal Server Error)} if the parametre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Parametre> partialUpdateParametre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Parametre parametre
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Parametre partially : {}, {}", id, parametre);
        if (parametre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parametre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parametreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parametre> result = parametreService.partialUpdate(parametre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parametre.getId().toString())
        );
    }

    /**
     * {@code GET  /parametres} : get all the parametres.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parametres in body.
     */
    @GetMapping("")
    public List<Parametre> getAllParametres(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Parametres");
        return parametreService.findAll();
    }

    /**
     * {@code GET  /parametres/:id} : get the "id" parametre.
     *
     * @param id the id of the parametre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parametre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Parametre> getParametre(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Parametre : {}", id);
        Optional<Parametre> parametre = parametreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parametre);
    }

    /**
     * {@code DELETE  /parametres/:id} : delete the "id" parametre.
     *
     * @param id the id of the parametre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParametre(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Parametre : {}", id);
        parametreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
