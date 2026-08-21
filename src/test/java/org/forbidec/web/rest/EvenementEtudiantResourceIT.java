package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvenementEtudiantAsserts.*;
import static org.forbidec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.EvenementEtudiant;
import org.forbidec.repository.EvenementEtudiantRepository;
import org.forbidec.service.EvenementEtudiantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EvenementEtudiantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvenementEtudiantResourceIT {

    private static final LocalDate DEFAULT_DATE_EVENEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EVENEMENT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/evenement-etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EvenementEtudiantRepository evenementEtudiantRepository;

    @Mock
    private EvenementEtudiantRepository evenementEtudiantRepositoryMock;

    @Mock
    private EvenementEtudiantService evenementEtudiantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvenementEtudiantMockMvc;

    private EvenementEtudiant evenementEtudiant;

    private EvenementEtudiant insertedEvenementEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvenementEtudiant createEntity(EntityManager em) {
        EvenementEtudiant evenementEtudiant = new EvenementEtudiant()
            .dateEvenement(DEFAULT_DATE_EVENEMENT)
            .intitule(DEFAULT_INTITULE)
            .commentaire(DEFAULT_COMMENTAIRE);
        // Add required entity
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            etudiant = EtudiantResourceIT.createEntity();
            em.persist(etudiant);
            em.flush();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        evenementEtudiant.setEtudiant(etudiant);
        return evenementEtudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvenementEtudiant createUpdatedEntity(EntityManager em) {
        EvenementEtudiant updatedEvenementEtudiant = new EvenementEtudiant()
            .dateEvenement(UPDATED_DATE_EVENEMENT)
            .intitule(UPDATED_INTITULE)
            .commentaire(UPDATED_COMMENTAIRE);
        // Add required entity
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            etudiant = EtudiantResourceIT.createUpdatedEntity();
            em.persist(etudiant);
            em.flush();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        updatedEvenementEtudiant.setEtudiant(etudiant);
        return updatedEvenementEtudiant;
    }

    @BeforeEach
    void initTest() {
        evenementEtudiant = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEvenementEtudiant != null) {
            evenementEtudiantRepository.delete(insertedEvenementEtudiant);
            insertedEvenementEtudiant = null;
        }
    }

    @Test
    @Transactional
    void createEvenementEtudiant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EvenementEtudiant
        var returnedEvenementEtudiant = om.readValue(
            restEvenementEtudiantMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(evenementEtudiant))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EvenementEtudiant.class
        );

        // Validate the EvenementEtudiant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEvenementEtudiantUpdatableFieldsEquals(returnedEvenementEtudiant, getPersistedEvenementEtudiant(returnedEvenementEtudiant));

        insertedEvenementEtudiant = returnedEvenementEtudiant;
    }

    @Test
    @Transactional
    void createEvenementEtudiantWithExistingId() throws Exception {
        // Create the EvenementEtudiant with an existing ID
        evenementEtudiant.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvenementEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evenementEtudiant.setIntitule(null);

        // Create the EvenementEtudiant, which fails.

        restEvenementEtudiantMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvenementEtudiants() throws Exception {
        // Initialize the database
        insertedEvenementEtudiant = evenementEtudiantRepository.saveAndFlush(evenementEtudiant);

        // Get all the evenementEtudiantList
        restEvenementEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenementEtudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateEvenement").value(hasItem(DEFAULT_DATE_EVENEMENT.toString())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvenementEtudiantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(evenementEtudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvenementEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evenementEtudiantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvenementEtudiantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evenementEtudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvenementEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evenementEtudiantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvenementEtudiant() throws Exception {
        // Initialize the database
        insertedEvenementEtudiant = evenementEtudiantRepository.saveAndFlush(evenementEtudiant);

        // Get the evenementEtudiant
        restEvenementEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, evenementEtudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evenementEtudiant.getId().intValue()))
            .andExpect(jsonPath("$.dateEvenement").value(DEFAULT_DATE_EVENEMENT.toString()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getNonExistingEvenementEtudiant() throws Exception {
        // Get the evenementEtudiant
        restEvenementEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvenementEtudiant() throws Exception {
        // Initialize the database
        insertedEvenementEtudiant = evenementEtudiantRepository.saveAndFlush(evenementEtudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evenementEtudiant
        EvenementEtudiant updatedEvenementEtudiant = evenementEtudiantRepository.findById(evenementEtudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvenementEtudiant are not directly saved in db
        em.detach(updatedEvenementEtudiant);
        updatedEvenementEtudiant.dateEvenement(UPDATED_DATE_EVENEMENT).intitule(UPDATED_INTITULE).commentaire(UPDATED_COMMENTAIRE);

        restEvenementEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvenementEtudiant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEvenementEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEvenementEtudiantToMatchAllProperties(updatedEvenementEtudiant);
    }

    @Test
    @Transactional
    void putNonExistingEvenementEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evenementEtudiant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evenementEtudiant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvenementEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evenementEtudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvenementEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evenementEtudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvenementEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedEvenementEtudiant = evenementEtudiantRepository.saveAndFlush(evenementEtudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evenementEtudiant using partial update
        EvenementEtudiant partialUpdatedEvenementEtudiant = new EvenementEtudiant();
        partialUpdatedEvenementEtudiant.setId(evenementEtudiant.getId());

        partialUpdatedEvenementEtudiant.dateEvenement(UPDATED_DATE_EVENEMENT).commentaire(UPDATED_COMMENTAIRE);

        restEvenementEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvenementEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvenementEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the EvenementEtudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvenementEtudiantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEvenementEtudiant, evenementEtudiant),
            getPersistedEvenementEtudiant(evenementEtudiant)
        );
    }

    @Test
    @Transactional
    void fullUpdateEvenementEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedEvenementEtudiant = evenementEtudiantRepository.saveAndFlush(evenementEtudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evenementEtudiant using partial update
        EvenementEtudiant partialUpdatedEvenementEtudiant = new EvenementEtudiant();
        partialUpdatedEvenementEtudiant.setId(evenementEtudiant.getId());

        partialUpdatedEvenementEtudiant.dateEvenement(UPDATED_DATE_EVENEMENT).intitule(UPDATED_INTITULE).commentaire(UPDATED_COMMENTAIRE);

        restEvenementEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvenementEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvenementEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the EvenementEtudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvenementEtudiantUpdatableFieldsEquals(
            partialUpdatedEvenementEtudiant,
            getPersistedEvenementEtudiant(partialUpdatedEvenementEtudiant)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEvenementEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evenementEtudiant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evenementEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvenementEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evenementEtudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvenementEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evenementEtudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evenementEtudiant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvenementEtudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvenementEtudiant() throws Exception {
        // Initialize the database
        insertedEvenementEtudiant = evenementEtudiantRepository.saveAndFlush(evenementEtudiant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evenementEtudiant
        restEvenementEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, evenementEtudiant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return evenementEtudiantRepository.count();
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

    protected EvenementEtudiant getPersistedEvenementEtudiant(EvenementEtudiant evenementEtudiant) {
        return evenementEtudiantRepository.findById(evenementEtudiant.getId()).orElseThrow();
    }

    protected void assertPersistedEvenementEtudiantToMatchAllProperties(EvenementEtudiant expectedEvenementEtudiant) {
        assertEvenementEtudiantAllPropertiesEquals(expectedEvenementEtudiant, getPersistedEvenementEtudiant(expectedEvenementEtudiant));
    }

    protected void assertPersistedEvenementEtudiantToMatchUpdatableProperties(EvenementEtudiant expectedEvenementEtudiant) {
        assertEvenementEtudiantAllUpdatablePropertiesEquals(
            expectedEvenementEtudiant,
            getPersistedEvenementEtudiant(expectedEvenementEtudiant)
        );
    }
}
