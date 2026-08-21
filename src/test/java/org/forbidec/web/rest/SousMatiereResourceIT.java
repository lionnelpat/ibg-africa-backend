package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.SousMatiereAsserts.*;
import static org.forbidec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.SousMatiere;
import org.forbidec.repository.SousMatiereRepository;
import org.forbidec.service.dto.SousMatiereDTO;
import org.forbidec.service.mapper.SousMatiereMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SousMatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousMatiereResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_LONG = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_COURT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_COURT = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/sous-matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SousMatiereRepository sousMatiereRepository;

    @Autowired
    private SousMatiereMapper sousMatiereMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousMatiereMockMvc;

    private SousMatiere sousMatiere;

    private SousMatiere insertedSousMatiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousMatiere createEntity() {
        return new SousMatiere()
            .intitule(DEFAULT_INTITULE)
            .libelleLong(DEFAULT_LIBELLE_LONG)
            .libelleCourt(DEFAULT_LIBELLE_COURT)
            .commentaire(DEFAULT_COMMENTAIRE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousMatiere createUpdatedEntity() {
        return new SousMatiere()
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        sousMatiere = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSousMatiere != null) {
            sousMatiereRepository.delete(insertedSousMatiere);
            insertedSousMatiere = null;
        }
    }

    @Test
    @Transactional
    void createSousMatiere() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);
        var returnedSousMatiereDTO = om.readValue(
            restSousMatiereMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sousMatiereDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SousMatiereDTO.class
        );

        // Validate the SousMatiere in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSousMatiere = sousMatiereMapper.toEntity(returnedSousMatiereDTO);
        assertSousMatiereUpdatableFieldsEquals(returnedSousMatiere, getPersistedSousMatiere(returnedSousMatiere));

        insertedSousMatiere = returnedSousMatiere;
    }

    @Test
    @Transactional
    void createSousMatiereWithExistingId() throws Exception {
        // Create the SousMatiere with an existing ID
        sousMatiere.setId(1L);
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousMatiereMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sousMatiere.setIntitule(null);

        // Create the SousMatiere, which fails.
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        restSousMatiereMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sousMatiere.setActif(null);

        // Create the SousMatiere, which fails.
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        restSousMatiereMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousMatieres() throws Exception {
        // Initialize the database
        insertedSousMatiere = sousMatiereRepository.saveAndFlush(sousMatiere);

        // Get all the sousMatiereList
        restSousMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousMatiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getSousMatiere() throws Exception {
        // Initialize the database
        insertedSousMatiere = sousMatiereRepository.saveAndFlush(sousMatiere);

        // Get the sousMatiere
        restSousMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, sousMatiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousMatiere.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.libelleLong").value(DEFAULT_LIBELLE_LONG))
            .andExpect(jsonPath("$.libelleCourt").value(DEFAULT_LIBELLE_COURT))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNonExistingSousMatiere() throws Exception {
        // Get the sousMatiere
        restSousMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSousMatiere() throws Exception {
        // Initialize the database
        insertedSousMatiere = sousMatiereRepository.saveAndFlush(sousMatiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sousMatiere
        SousMatiere updatedSousMatiere = sousMatiereRepository.findById(sousMatiere.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSousMatiere are not directly saved in db
        em.detach(updatedSousMatiere);
        updatedSousMatiere
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(updatedSousMatiere);

        restSousMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousMatiereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isOk());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSousMatiereToMatchAllProperties(updatedSousMatiere);
    }

    @Test
    @Transactional
    void putNonExistingSousMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sousMatiere.setId(longCount.incrementAndGet());

        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousMatiereDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sousMatiere.setId(longCount.incrementAndGet());

        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sousMatiere.setId(longCount.incrementAndGet());

        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousMatiereMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sousMatiereDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousMatiereWithPatch() throws Exception {
        // Initialize the database
        insertedSousMatiere = sousMatiereRepository.saveAndFlush(sousMatiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sousMatiere using partial update
        SousMatiere partialUpdatedSousMatiere = new SousMatiere();
        partialUpdatedSousMatiere.setId(sousMatiere.getId());

        partialUpdatedSousMatiere.libelleLong(UPDATED_LIBELLE_LONG).libelleCourt(UPDATED_LIBELLE_COURT).actif(UPDATED_ACTIF);

        restSousMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSousMatiere))
            )
            .andExpect(status().isOk());

        // Validate the SousMatiere in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSousMatiereUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSousMatiere, sousMatiere),
            getPersistedSousMatiere(sousMatiere)
        );
    }

    @Test
    @Transactional
    void fullUpdateSousMatiereWithPatch() throws Exception {
        // Initialize the database
        insertedSousMatiere = sousMatiereRepository.saveAndFlush(sousMatiere);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sousMatiere using partial update
        SousMatiere partialUpdatedSousMatiere = new SousMatiere();
        partialUpdatedSousMatiere.setId(sousMatiere.getId());

        partialUpdatedSousMatiere
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);

        restSousMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousMatiere.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSousMatiere))
            )
            .andExpect(status().isOk());

        // Validate the SousMatiere in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSousMatiereUpdatableFieldsEquals(partialUpdatedSousMatiere, getPersistedSousMatiere(partialUpdatedSousMatiere));
    }

    @Test
    @Transactional
    void patchNonExistingSousMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sousMatiere.setId(longCount.incrementAndGet());

        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousMatiereDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sousMatiere.setId(longCount.incrementAndGet());

        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousMatiere() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sousMatiere.setId(longCount.incrementAndGet());

        // Create the SousMatiere
        SousMatiereDTO sousMatiereDTO = sousMatiereMapper.toDto(sousMatiere);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sousMatiereDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousMatiere in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousMatiere() throws Exception {
        // Initialize the database
        insertedSousMatiere = sousMatiereRepository.saveAndFlush(sousMatiere);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sousMatiere
        restSousMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousMatiere.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sousMatiereRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SousMatiere getPersistedSousMatiere(SousMatiere sousMatiere) {
        return sousMatiereRepository.findById(sousMatiere.getId()).orElseThrow();
    }

    protected void assertPersistedSousMatiereToMatchAllProperties(SousMatiere expectedSousMatiere) {
        assertSousMatiereAllPropertiesEquals(expectedSousMatiere, getPersistedSousMatiere(expectedSousMatiere));
    }

    protected void assertPersistedSousMatiereToMatchUpdatableProperties(SousMatiere expectedSousMatiere) {
        assertSousMatiereAllUpdatablePropertiesEquals(expectedSousMatiere, getPersistedSousMatiere(expectedSousMatiere));
    }
}
