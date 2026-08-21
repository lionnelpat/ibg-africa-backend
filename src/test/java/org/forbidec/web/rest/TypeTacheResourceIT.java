package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.TypeTacheAsserts.*;
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
import org.forbidec.domain.TypeTache;
import org.forbidec.repository.TypeTacheRepository;
import org.forbidec.service.dto.TypeTacheDTO;
import org.forbidec.service.mapper.TypeTacheMapper;
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
 * Integration tests for the {@link TypeTacheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeTacheResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_LONG = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_COURT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_COURT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENTRE_DANS_MOYENNE = false;
    private static final Boolean UPDATED_ENTRE_DANS_MOYENNE = true;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/type-taches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeTacheRepository typeTacheRepository;

    @Autowired
    private TypeTacheMapper typeTacheMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeTacheMockMvc;

    private TypeTache typeTache;

    private TypeTache insertedTypeTache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeTache createEntity() {
        return new TypeTache()
            .code(DEFAULT_CODE)
            .intitule(DEFAULT_INTITULE)
            .libelleLong(DEFAULT_LIBELLE_LONG)
            .libelleCourt(DEFAULT_LIBELLE_COURT)
            .entreDansMoyenne(DEFAULT_ENTRE_DANS_MOYENNE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeTache createUpdatedEntity() {
        return new TypeTache()
            .code(UPDATED_CODE)
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .entreDansMoyenne(UPDATED_ENTRE_DANS_MOYENNE)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        typeTache = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTypeTache != null) {
            typeTacheRepository.delete(insertedTypeTache);
            insertedTypeTache = null;
        }
    }

    @Test
    @Transactional
    void createTypeTache() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);
        var returnedTypeTacheDTO = om.readValue(
            restTypeTacheMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeTacheDTO.class
        );

        // Validate the TypeTache in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeTache = typeTacheMapper.toEntity(returnedTypeTacheDTO);
        assertTypeTacheUpdatableFieldsEquals(returnedTypeTache, getPersistedTypeTache(returnedTypeTache));

        insertedTypeTache = returnedTypeTache;
    }

    @Test
    @Transactional
    void createTypeTacheWithExistingId() throws Exception {
        // Create the TypeTache with an existing ID
        typeTache.setId(1L);
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeTacheMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeTache.setCode(null);

        // Create the TypeTache, which fails.
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        restTypeTacheMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeTache.setIntitule(null);

        // Create the TypeTache, which fails.
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        restTypeTacheMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntreDansMoyenneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeTache.setEntreDansMoyenne(null);

        // Create the TypeTache, which fails.
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        restTypeTacheMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeTache.setActif(null);

        // Create the TypeTache, which fails.
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        restTypeTacheMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeTaches() throws Exception {
        // Initialize the database
        insertedTypeTache = typeTacheRepository.saveAndFlush(typeTache);

        // Get all the typeTacheList
        restTypeTacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeTache.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].entreDansMoyenne").value(hasItem(DEFAULT_ENTRE_DANS_MOYENNE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getTypeTache() throws Exception {
        // Initialize the database
        insertedTypeTache = typeTacheRepository.saveAndFlush(typeTache);

        // Get the typeTache
        restTypeTacheMockMvc
            .perform(get(ENTITY_API_URL_ID, typeTache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeTache.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.libelleLong").value(DEFAULT_LIBELLE_LONG))
            .andExpect(jsonPath("$.libelleCourt").value(DEFAULT_LIBELLE_COURT))
            .andExpect(jsonPath("$.entreDansMoyenne").value(DEFAULT_ENTRE_DANS_MOYENNE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNonExistingTypeTache() throws Exception {
        // Get the typeTache
        restTypeTacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeTache() throws Exception {
        // Initialize the database
        insertedTypeTache = typeTacheRepository.saveAndFlush(typeTache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeTache
        TypeTache updatedTypeTache = typeTacheRepository.findById(typeTache.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeTache are not directly saved in db
        em.detach(updatedTypeTache);
        updatedTypeTache
            .code(UPDATED_CODE)
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .entreDansMoyenne(UPDATED_ENTRE_DANS_MOYENNE)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(updatedTypeTache);

        restTypeTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeTacheDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeTacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeTacheToMatchAllProperties(updatedTypeTache);
    }

    @Test
    @Transactional
    void putNonExistingTypeTache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeTache.setId(longCount.incrementAndGet());

        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeTacheDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeTacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeTache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeTache.setId(longCount.incrementAndGet());

        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeTacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeTache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeTache.setId(longCount.incrementAndGet());

        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeTacheMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeTacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeTacheWithPatch() throws Exception {
        // Initialize the database
        insertedTypeTache = typeTacheRepository.saveAndFlush(typeTache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeTache using partial update
        TypeTache partialUpdatedTypeTache = new TypeTache();
        partialUpdatedTypeTache.setId(typeTache.getId());

        partialUpdatedTypeTache.intitule(UPDATED_INTITULE).libelleCourt(UPDATED_LIBELLE_COURT).commentaire(UPDATED_COMMENTAIRE);

        restTypeTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeTache.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeTache))
            )
            .andExpect(status().isOk());

        // Validate the TypeTache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeTacheUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeTache, typeTache),
            getPersistedTypeTache(typeTache)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeTacheWithPatch() throws Exception {
        // Initialize the database
        insertedTypeTache = typeTacheRepository.saveAndFlush(typeTache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeTache using partial update
        TypeTache partialUpdatedTypeTache = new TypeTache();
        partialUpdatedTypeTache.setId(typeTache.getId());

        partialUpdatedTypeTache
            .code(UPDATED_CODE)
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .entreDansMoyenne(UPDATED_ENTRE_DANS_MOYENNE)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);

        restTypeTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeTache.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeTache))
            )
            .andExpect(status().isOk());

        // Validate the TypeTache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeTacheUpdatableFieldsEquals(partialUpdatedTypeTache, getPersistedTypeTache(partialUpdatedTypeTache));
    }

    @Test
    @Transactional
    void patchNonExistingTypeTache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeTache.setId(longCount.incrementAndGet());

        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeTacheDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeTacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeTache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeTache.setId(longCount.incrementAndGet());

        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeTacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeTache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeTache.setId(longCount.incrementAndGet());

        // Create the TypeTache
        TypeTacheDTO typeTacheDTO = typeTacheMapper.toDto(typeTache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeTacheMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeTacheDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeTache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeTache() throws Exception {
        // Initialize the database
        insertedTypeTache = typeTacheRepository.saveAndFlush(typeTache);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeTache
        restTypeTacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeTache.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeTacheRepository.count();
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

    protected TypeTache getPersistedTypeTache(TypeTache typeTache) {
        return typeTacheRepository.findById(typeTache.getId()).orElseThrow();
    }

    protected void assertPersistedTypeTacheToMatchAllProperties(TypeTache expectedTypeTache) {
        assertTypeTacheAllPropertiesEquals(expectedTypeTache, getPersistedTypeTache(expectedTypeTache));
    }

    protected void assertPersistedTypeTacheToMatchUpdatableProperties(TypeTache expectedTypeTache) {
        assertTypeTacheAllUpdatablePropertiesEquals(expectedTypeTache, getPersistedTypeTache(expectedTypeTache));
    }
}
