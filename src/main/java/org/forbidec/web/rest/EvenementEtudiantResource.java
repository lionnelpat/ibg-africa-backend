package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.domain.EvenementEtudiant;
import org.forbidec.repository.EvenementEtudiantRepository;
import org.forbidec.service.EvenementEtudiantService;
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
 * REST controller for managing {@link org.forbidec.domain.EvenementEtudiant}.
 */
@RestController
@RequestMapping("/api/evenement-etudiants")
public class EvenementEtudiantResource {

    private static final Logger LOG = LoggerFactory.getLogger(EvenementEtudiantResource.class);

    private static final String ENTITY_NAME = "evenementEtudiant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvenementEtudiantService evenementEtudiantService;

    private final EvenementEtudiantRepository evenementEtudiantRepository;

    public EvenementEtudiantResource(
        EvenementEtudiantService evenementEtudiantService,
        EvenementEtudiantRepository evenementEtudiantRepository
    ) {
        this.evenementEtudiantService = evenementEtudiantService;
        this.evenementEtudiantRepository = evenementEtudiantRepository;
    }

    /**
     * {@code POST  /evenement-etudiants} : Create a new evenementEtudiant.
     *
     * @param evenementEtudiant the evenementEtudiant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evenementEtudiant, or with status {@code 400 (Bad Request)} if the evenementEtudiant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EvenementEtudiant> createEvenementEtudiant(@Valid @RequestBody EvenementEtudiant evenementEtudiant)
        throws URISyntaxException {
        LOG.debug("REST request to save EvenementEtudiant : {}", evenementEtudiant);
        if (evenementEtudiant.getId() != null) {
            throw new BadRequestAlertException("A new evenementEtudiant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        evenementEtudiant = evenementEtudiantService.save(evenementEtudiant);
        return ResponseEntity.created(new URI("/api/evenement-etudiants/" + evenementEtudiant.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, evenementEtudiant.getId().toString()))
            .body(evenementEtudiant);
    }

    /**
     * {@code PUT  /evenement-etudiants/:id} : Updates an existing evenementEtudiant.
     *
     * @param id the id of the evenementEtudiant to save.
     * @param evenementEtudiant the evenementEtudiant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evenementEtudiant,
     * or with status {@code 400 (Bad Request)} if the evenementEtudiant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evenementEtudiant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvenementEtudiant> updateEvenementEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EvenementEtudiant evenementEtudiant
    ) throws URISyntaxException {
        LOG.debug("REST request to update EvenementEtudiant : {}, {}", id, evenementEtudiant);
        if (evenementEtudiant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evenementEtudiant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evenementEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        evenementEtudiant = evenementEtudiantService.update(evenementEtudiant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evenementEtudiant.getId().toString()))
            .body(evenementEtudiant);
    }

    /**
     * {@code PATCH  /evenement-etudiants/:id} : Partial updates given fields of an existing evenementEtudiant, field will ignore if it is null
     *
     * @param id the id of the evenementEtudiant to save.
     * @param evenementEtudiant the evenementEtudiant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evenementEtudiant,
     * or with status {@code 400 (Bad Request)} if the evenementEtudiant is not valid,
     * or with status {@code 404 (Not Found)} if the evenementEtudiant is not found,
     * or with status {@code 500 (Internal Server Error)} if the evenementEtudiant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvenementEtudiant> partialUpdateEvenementEtudiant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EvenementEtudiant evenementEtudiant
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EvenementEtudiant partially : {}, {}", id, evenementEtudiant);
        if (evenementEtudiant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evenementEtudiant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evenementEtudiantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvenementEtudiant> result = evenementEtudiantService.partialUpdate(evenementEtudiant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evenementEtudiant.getId().toString())
        );
    }

    /**
     * {@code GET  /evenement-etudiants} : get all the evenementEtudiants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evenementEtudiants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EvenementEtudiant>> getAllEvenementEtudiants(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of EvenementEtudiants");
        Page<EvenementEtudiant> page;
        if (eagerload) {
            page = evenementEtudiantService.findAllWithEagerRelationships(pageable);
        } else {
            page = evenementEtudiantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evenement-etudiants/:id} : get the "id" evenementEtudiant.
     *
     * @param id the id of the evenementEtudiant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evenementEtudiant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvenementEtudiant> getEvenementEtudiant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EvenementEtudiant : {}", id);
        Optional<EvenementEtudiant> evenementEtudiant = evenementEtudiantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evenementEtudiant);
    }

    /**
     * {@code DELETE  /evenement-etudiants/:id} : delete the "id" evenementEtudiant.
     *
     * @param id the id of the evenementEtudiant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvenementEtudiant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EvenementEtudiant : {}", id);
        evenementEtudiantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
