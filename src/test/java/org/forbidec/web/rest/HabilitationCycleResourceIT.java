package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.HabilitationCycleAsserts.*;
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
import org.forbidec.domain.HabilitationCycle;
import org.forbidec.domain.enumeration.RoleFonctionnel;
import org.forbidec.repository.HabilitationCycleRepository;
import org.forbidec.service.HabilitationCycleService;
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
 * Integration tests for the {@link HabilitationCycleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HabilitationCycleResourceIT {

    private static final String DEFAULT_KEYCLOAK_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_USER_ID = "BBBBBBBBBB";

    private static final RoleFonctionnel DEFAULT_ROLE_FONCTIONNEL = RoleFonctionnel.ADMIN;
    private static final RoleFonctionnel UPDATED_ROLE_FONCTIONNEL = RoleFonctionnel.SCOLARITE;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/habilitation-cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HabilitationCycleRepository habilitationCycleRepository;

    @Mock
    private HabilitationCycleRepository habilitationCycleRepositoryMock;

    @Mock
    private HabilitationCycleService habilitationCycleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHabilitationCycleMockMvc;

    private HabilitationCycle habilitationCycle;

    private HabilitationCycle insertedHabilitationCycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HabilitationCycle createEntity() {
        return new HabilitationCycle()
            .keycloakUserId(DEFAULT_KEYCLOAK_USER_ID)
            .roleFonctionnel(DEFAULT_ROLE_FONCTIONNEL)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HabilitationCycle createUpdatedEntity() {
        return new HabilitationCycle()
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .roleFonctionnel(UPDATED_ROLE_FONCTIONNEL)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
    }

    @BeforeEach
    void initTest() {
        habilitationCycle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHabilitationCycle != null) {
            habilitationCycleRepository.delete(insertedHabilitationCycle);
            insertedHabilitationCycle = null;
        }
    }

    @Test
    @Transactional
    void createHabilitationCycle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HabilitationCycle
        var returnedHabilitationCycle = om.readValue(
            restHabilitationCycleMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(habilitationCycle))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HabilitationCycle.class
        );

        // Validate the HabilitationCycle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertHabilitationCycleUpdatableFieldsEquals(returnedHabilitationCycle, getPersistedHabilitationCycle(returnedHabilitationCycle));

        insertedHabilitationCycle = returnedHabilitationCycle;
    }

    @Test
    @Transactional
    void createHabilitationCycleWithExistingId() throws Exception {
        // Create the HabilitationCycle with an existing ID
        habilitationCycle.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHabilitationCycleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeycloakUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        habilitationCycle.setKeycloakUserId(null);

        // Create the HabilitationCycle, which fails.

        restHabilitationCycleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleFonctionnelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        habilitationCycle.setRoleFonctionnel(null);

        // Create the HabilitationCycle, which fails.

        restHabilitationCycleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHabilitationCycles() throws Exception {
        // Initialize the database
        insertedHabilitationCycle = habilitationCycleRepository.saveAndFlush(habilitationCycle);

        // Get all the habilitationCycleList
        restHabilitationCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(habilitationCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID)))
            .andExpect(jsonPath("$.[*].roleFonctionnel").value(hasItem(DEFAULT_ROLE_FONCTIONNEL.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHabilitationCyclesWithEagerRelationshipsIsEnabled() throws Exception {
        when(habilitationCycleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHabilitationCycleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(habilitationCycleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHabilitationCyclesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(habilitationCycleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHabilitationCycleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(habilitationCycleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHabilitationCycle() throws Exception {
        // Initialize the database
        insertedHabilitationCycle = habilitationCycleRepository.saveAndFlush(habilitationCycle);

        // Get the habilitationCycle
        restHabilitationCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, habilitationCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(habilitationCycle.getId().intValue()))
            .andExpect(jsonPath("$.keycloakUserId").value(DEFAULT_KEYCLOAK_USER_ID))
            .andExpect(jsonPath("$.roleFonctionnel").value(DEFAULT_ROLE_FONCTIONNEL.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHabilitationCycle() throws Exception {
        // Get the habilitationCycle
        restHabilitationCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHabilitationCycle() throws Exception {
        // Initialize the database
        insertedHabilitationCycle = habilitationCycleRepository.saveAndFlush(habilitationCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the habilitationCycle
        HabilitationCycle updatedHabilitationCycle = habilitationCycleRepository.findById(habilitationCycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHabilitationCycle are not directly saved in db
        em.detach(updatedHabilitationCycle);
        updatedHabilitationCycle
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .roleFonctionnel(UPDATED_ROLE_FONCTIONNEL)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restHabilitationCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHabilitationCycle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedHabilitationCycle))
            )
            .andExpect(status().isOk());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHabilitationCycleToMatchAllProperties(updatedHabilitationCycle);
    }

    @Test
    @Transactional
    void putNonExistingHabilitationCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        habilitationCycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilitationCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, habilitationCycle.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHabilitationCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        habilitationCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHabilitationCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        habilitationCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationCycleMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHabilitationCycleWithPatch() throws Exception {
        // Initialize the database
        insertedHabilitationCycle = habilitationCycleRepository.saveAndFlush(habilitationCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the habilitationCycle using partial update
        HabilitationCycle partialUpdatedHabilitationCycle = new HabilitationCycle();
        partialUpdatedHabilitationCycle.setId(habilitationCycle.getId());

        partialUpdatedHabilitationCycle.dateFin(UPDATED_DATE_FIN);

        restHabilitationCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHabilitationCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHabilitationCycle))
            )
            .andExpect(status().isOk());

        // Validate the HabilitationCycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHabilitationCycleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHabilitationCycle, habilitationCycle),
            getPersistedHabilitationCycle(habilitationCycle)
        );
    }

    @Test
    @Transactional
    void fullUpdateHabilitationCycleWithPatch() throws Exception {
        // Initialize the database
        insertedHabilitationCycle = habilitationCycleRepository.saveAndFlush(habilitationCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the habilitationCycle using partial update
        HabilitationCycle partialUpdatedHabilitationCycle = new HabilitationCycle();
        partialUpdatedHabilitationCycle.setId(habilitationCycle.getId());

        partialUpdatedHabilitationCycle
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .roleFonctionnel(UPDATED_ROLE_FONCTIONNEL)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restHabilitationCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHabilitationCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHabilitationCycle))
            )
            .andExpect(status().isOk());

        // Validate the HabilitationCycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHabilitationCycleUpdatableFieldsEquals(
            partialUpdatedHabilitationCycle,
            getPersistedHabilitationCycle(partialUpdatedHabilitationCycle)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHabilitationCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        habilitationCycle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHabilitationCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, habilitationCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHabilitationCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        habilitationCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isBadRequest());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHabilitationCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        habilitationCycle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHabilitationCycleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(habilitationCycle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HabilitationCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHabilitationCycle() throws Exception {
        // Initialize the database
        insertedHabilitationCycle = habilitationCycleRepository.saveAndFlush(habilitationCycle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the habilitationCycle
        restHabilitationCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, habilitationCycle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return habilitationCycleRepository.count();
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

    protected HabilitationCycle getPersistedHabilitationCycle(HabilitationCycle habilitationCycle) {
        return habilitationCycleRepository.findById(habilitationCycle.getId()).orElseThrow();
    }

    protected void assertPersistedHabilitationCycleToMatchAllProperties(HabilitationCycle expectedHabilitationCycle) {
        assertHabilitationCycleAllPropertiesEquals(expectedHabilitationCycle, getPersistedHabilitationCycle(expectedHabilitationCycle));
    }

    protected void assertPersistedHabilitationCycleToMatchUpdatableProperties(HabilitationCycle expectedHabilitationCycle) {
        assertHabilitationCycleAllUpdatablePropertiesEquals(
            expectedHabilitationCycle,
            getPersistedHabilitationCycle(expectedHabilitationCycle)
        );
    }
}
