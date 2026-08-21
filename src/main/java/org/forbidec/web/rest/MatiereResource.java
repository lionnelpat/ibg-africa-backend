package org.forbidec.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.repository.MatiereRepository;
import org.forbidec.service.MatiereService;
import org.forbidec.service.dto.MatiereDTO;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.forbidec.domain.Matiere}.
 */
@RestController
@RequestMapping("/api/matieres")
public class MatiereResource {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereResource.class);

    private static final String ENTITY_NAME = "matiere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MatiereService matiereService;

    private final MatiereRepository matiereRepository;

    public MatiereResource(MatiereService matiereService, MatiereRepository matiereRepository) {
        this.matiereService = matiereService;
        this.matiereRepository = matiereRepository;
    }

    /**
     * {@code POST  /matieres} : Create a new matiere.
     *
     * @param matiereDTO the matiereDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new matiereDTO, or with status {@code 400 (Bad Request)} if the matiere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MatiereDTO> createMatiere(@Valid @RequestBody MatiereDTO matiereDTO) throws URISyntaxException {
        LOG.debug("REST request to save Matiere : {}", matiereDTO);
        if (matiereDTO.getId() != null) {
            throw new BadRequestAlertException("A new matiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        matiereDTO = matiereService.save(matiereDTO);
        return ResponseEntity.created(new URI("/api/matieres/" + matiereDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, matiereDTO.getId().toString()))
            .body(matiereDTO);
    }

    /**
     * {@code PUT  /matieres/:id} : Updates an existing matiere.
     *
     * @param id the id of the matiereDTO to save.
     * @param matiereDTO the matiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matiereDTO,
     * or with status {@code 400 (Bad Request)} if the matiereDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the matiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MatiereDTO> updateMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MatiereDTO matiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Matiere : {}, {}", id, matiereDTO);
        if (matiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        matiereDTO = matiereService.update(matiereDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matiereDTO.getId().toString()))
            .body(matiereDTO);
    }

    /**
     * {@code PATCH  /matieres/:id} : Partial updates given fields of an existing matiere, field will ignore if it is null
     *
     * @param id the id of the matiereDTO to save.
     * @param matiereDTO the matiereDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated matiereDTO,
     * or with status {@code 400 (Bad Request)} if the matiereDTO is not valid,
     * or with status {@code 404 (Not Found)} if the matiereDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the matiereDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MatiereDTO> partialUpdateMatiere(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MatiereDTO matiereDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Matiere partially : {}, {}", id, matiereDTO);
        if (matiereDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, matiereDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!matiereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MatiereDTO> result = matiereService.partialUpdate(matiereDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, matiereDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /matieres} : get all the matieres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of matieres in body.
     */
    @GetMapping("")
    public List<MatiereDTO> getAllMatieres() {
        LOG.debug("REST request to get all Matieres");
        return matiereService.findAll();
    }

    /**
     * {@code GET  /matieres/:id} : get the "id" matiere.
     *
     * @param id the id of the matiereDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the matiereDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatiereDTO> getMatiere(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Matiere : {}", id);
        Optional<MatiereDTO> matiereDTO = matiereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matiereDTO);
    }

    /**
     * {@code DELETE  /matieres/:id} : delete the "id" matiere.
     *
     * @param id the id of the matiereDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Matiere : {}", id);
        matiereService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
