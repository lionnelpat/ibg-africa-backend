package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.BaremeMentionAsserts.*;
import static org.forbidec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.forbidec.web.rest.TestUtil.sameNumber;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.BaremeMention;
import org.forbidec.repository.BaremeMentionRepository;
import org.forbidec.service.BaremeMentionService;
import org.forbidec.service.dto.BaremeMentionDTO;
import org.forbidec.service.mapper.BaremeMentionMapper;
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
 * Integration tests for the {@link BaremeMentionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BaremeMentionResourceIT {

    private static final String DEFAULT_LIBELLE_LONG = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_COURT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_COURT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_BORNE_MIN = new BigDecimal(0);
    private static final BigDecimal UPDATED_BORNE_MIN = new BigDecimal(1);

    private static final Boolean DEFAULT_MIN_INCLUS = false;
    private static final Boolean UPDATED_MIN_INCLUS = true;

    private static final BigDecimal DEFAULT_BORNE_MAX = new BigDecimal(0);
    private static final BigDecimal UPDATED_BORNE_MAX = new BigDecimal(1);

    private static final Boolean DEFAULT_MAX_INCLUS = false;
    private static final Boolean UPDATED_MAX_INCLUS = true;

    private static final Integer DEFAULT_ORDRE_AFFICHAGE = 1;
    private static final Integer UPDATED_ORDRE_AFFICHAGE = 2;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/bareme-mentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BaremeMentionRepository baremeMentionRepository;

    @Mock
    private BaremeMentionRepository baremeMentionRepositoryMock;

    @Autowired
    private BaremeMentionMapper baremeMentionMapper;

    @Mock
    private BaremeMentionService baremeMentionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaremeMentionMockMvc;

    private BaremeMention baremeMention;

    private BaremeMention insertedBaremeMention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaremeMention createEntity() {
        return new BaremeMention()
            .libelleLong(DEFAULT_LIBELLE_LONG)
            .libelleCourt(DEFAULT_LIBELLE_COURT)
            .borneMin(DEFAULT_BORNE_MIN)
            .minInclus(DEFAULT_MIN_INCLUS)
            .borneMax(DEFAULT_BORNE_MAX)
            .maxInclus(DEFAULT_MAX_INCLUS)
            .ordreAffichage(DEFAULT_ORDRE_AFFICHAGE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaremeMention createUpdatedEntity() {
        return new BaremeMention()
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .borneMin(UPDATED_BORNE_MIN)
            .minInclus(UPDATED_MIN_INCLUS)
            .borneMax(UPDATED_BORNE_MAX)
            .maxInclus(UPDATED_MAX_INCLUS)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        baremeMention = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBaremeMention != null) {
            baremeMentionRepository.delete(insertedBaremeMention);
            insertedBaremeMention = null;
        }
    }

    @Test
    @Transactional
    void createBaremeMention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);
        var returnedBaremeMentionDTO = om.readValue(
            restBaremeMentionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(baremeMentionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BaremeMentionDTO.class
        );

        // Validate the BaremeMention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBaremeMention = baremeMentionMapper.toEntity(returnedBaremeMentionDTO);
        assertBaremeMentionUpdatableFieldsEquals(returnedBaremeMention, getPersistedBaremeMention(returnedBaremeMention));

        insertedBaremeMention = returnedBaremeMention;
    }

    @Test
    @Transactional
    void createBaremeMentionWithExistingId() throws Exception {
        // Create the BaremeMention with an existing ID
        baremeMention.setId(1L);
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleLongIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        baremeMention.setLibelleLong(null);

        // Create the BaremeMention, which fails.
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleCourtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        baremeMention.setLibelleCourt(null);

        // Create the BaremeMention, which fails.
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinInclusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        baremeMention.setMinInclus(null);

        // Create the BaremeMention, which fails.
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxInclusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        baremeMention.setMaxInclus(null);

        // Create the BaremeMention, which fails.
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdreAffichageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        baremeMention.setOrdreAffichage(null);

        // Create the BaremeMention, which fails.
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        baremeMention.setActif(null);

        // Create the BaremeMention, which fails.
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        restBaremeMentionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBaremeMentions() throws Exception {
        // Initialize the database
        insertedBaremeMention = baremeMentionRepository.saveAndFlush(baremeMention);

        // Get all the baremeMentionList
        restBaremeMentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baremeMention.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].borneMin").value(hasItem(sameNumber(DEFAULT_BORNE_MIN))))
            .andExpect(jsonPath("$.[*].minInclus").value(hasItem(DEFAULT_MIN_INCLUS)))
            .andExpect(jsonPath("$.[*].borneMax").value(hasItem(sameNumber(DEFAULT_BORNE_MAX))))
            .andExpect(jsonPath("$.[*].maxInclus").value(hasItem(DEFAULT_MAX_INCLUS)))
            .andExpect(jsonPath("$.[*].ordreAffichage").value(hasItem(DEFAULT_ORDRE_AFFICHAGE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBaremeMentionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(baremeMentionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBaremeMentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(baremeMentionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBaremeMentionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(baremeMentionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBaremeMentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(baremeMentionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBaremeMention() throws Exception {
        // Initialize the database
        insertedBaremeMention = baremeMentionRepository.saveAndFlush(baremeMention);

        // Get the baremeMention
        restBaremeMentionMockMvc
            .perform(get(ENTITY_API_URL_ID, baremeMention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baremeMention.getId().intValue()))
            .andExpect(jsonPath("$.libelleLong").value(DEFAULT_LIBELLE_LONG))
            .andExpect(jsonPath("$.libelleCourt").value(DEFAULT_LIBELLE_COURT))
            .andExpect(jsonPath("$.borneMin").value(sameNumber(DEFAULT_BORNE_MIN)))
            .andExpect(jsonPath("$.minInclus").value(DEFAULT_MIN_INCLUS))
            .andExpect(jsonPath("$.borneMax").value(sameNumber(DEFAULT_BORNE_MAX)))
            .andExpect(jsonPath("$.maxInclus").value(DEFAULT_MAX_INCLUS))
            .andExpect(jsonPath("$.ordreAffichage").value(DEFAULT_ORDRE_AFFICHAGE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNonExistingBaremeMention() throws Exception {
        // Get the baremeMention
        restBaremeMentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBaremeMention() throws Exception {
        // Initialize the database
        insertedBaremeMention = baremeMentionRepository.saveAndFlush(baremeMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baremeMention
        BaremeMention updatedBaremeMention = baremeMentionRepository.findById(baremeMention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBaremeMention are not directly saved in db
        em.detach(updatedBaremeMention);
        updatedBaremeMention
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .borneMin(UPDATED_BORNE_MIN)
            .minInclus(UPDATED_MIN_INCLUS)
            .borneMax(UPDATED_BORNE_MAX)
            .maxInclus(UPDATED_MAX_INCLUS)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(updatedBaremeMention);

        restBaremeMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baremeMentionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBaremeMentionToMatchAllProperties(updatedBaremeMention);
    }

    @Test
    @Transactional
    void putNonExistingBaremeMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baremeMention.setId(longCount.incrementAndGet());

        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaremeMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baremeMentionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaremeMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baremeMention.setId(longCount.incrementAndGet());

        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaremeMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baremeMention.setId(longCount.incrementAndGet());

        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMentionMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaremeMentionWithPatch() throws Exception {
        // Initialize the database
        insertedBaremeMention = baremeMentionRepository.saveAndFlush(baremeMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baremeMention using partial update
        BaremeMention partialUpdatedBaremeMention = new BaremeMention();
        partialUpdatedBaremeMention.setId(baremeMention.getId());

        partialUpdatedBaremeMention
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .borneMin(UPDATED_BORNE_MIN)
            .maxInclus(UPDATED_MAX_INCLUS)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .commentaire(UPDATED_COMMENTAIRE);

        restBaremeMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaremeMention.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBaremeMention))
            )
            .andExpect(status().isOk());

        // Validate the BaremeMention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaremeMentionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBaremeMention, baremeMention),
            getPersistedBaremeMention(baremeMention)
        );
    }

    @Test
    @Transactional
    void fullUpdateBaremeMentionWithPatch() throws Exception {
        // Initialize the database
        insertedBaremeMention = baremeMentionRepository.saveAndFlush(baremeMention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the baremeMention using partial update
        BaremeMention partialUpdatedBaremeMention = new BaremeMention();
        partialUpdatedBaremeMention.setId(baremeMention.getId());

        partialUpdatedBaremeMention
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .borneMin(UPDATED_BORNE_MIN)
            .minInclus(UPDATED_MIN_INCLUS)
            .borneMax(UPDATED_BORNE_MAX)
            .maxInclus(UPDATED_MAX_INCLUS)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);

        restBaremeMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaremeMention.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBaremeMention))
            )
            .andExpect(status().isOk());

        // Validate the BaremeMention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBaremeMentionUpdatableFieldsEquals(partialUpdatedBaremeMention, getPersistedBaremeMention(partialUpdatedBaremeMention));
    }

    @Test
    @Transactional
    void patchNonExistingBaremeMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baremeMention.setId(longCount.incrementAndGet());

        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaremeMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baremeMentionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaremeMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baremeMention.setId(longCount.incrementAndGet());

        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaremeMention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        baremeMention.setId(longCount.incrementAndGet());

        // Create the BaremeMention
        BaremeMentionDTO baremeMentionDTO = baremeMentionMapper.toDto(baremeMention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaremeMentionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(baremeMentionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaremeMention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaremeMention() throws Exception {
        // Initialize the database
        insertedBaremeMention = baremeMentionRepository.saveAndFlush(baremeMention);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the baremeMention
        restBaremeMentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, baremeMention.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return baremeMentionRepository.count();
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

    protected BaremeMention getPersistedBaremeMention(BaremeMention baremeMention) {
        return baremeMentionRepository.findById(baremeMention.getId()).orElseThrow();
    }

    protected void assertPersistedBaremeMentionToMatchAllProperties(BaremeMention expectedBaremeMention) {
        assertBaremeMentionAllPropertiesEquals(expectedBaremeMention, getPersistedBaremeMention(expectedBaremeMention));
    }

    protected void assertPersistedBaremeMentionToMatchUpdatableProperties(BaremeMention expectedBaremeMention) {
        assertBaremeMentionAllUpdatablePropertiesEquals(expectedBaremeMention, getPersistedBaremeMention(expectedBaremeMention));
    }
}
