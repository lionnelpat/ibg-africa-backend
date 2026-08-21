package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.ParametreAsserts.*;
import static org.forbidec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.Parametre;
import org.forbidec.domain.enumeration.TypeValeur;
import org.forbidec.repository.ParametreRepository;
import org.forbidec.service.ParametreService;
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
 * Integration tests for the {@link ParametreResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ParametreResourceIT {

    private static final String DEFAULT_CLE = "AAAAAAAAAA";
    private static final String UPDATED_CLE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final TypeValeur DEFAULT_TYPE_VALEUR = TypeValeur.TEXTE;
    private static final TypeValeur UPDATED_TYPE_VALEUR = TypeValeur.NOMBRE;

    private static final Boolean DEFAULT_MODIFIABLE_UI = false;
    private static final Boolean UPDATED_MODIFIABLE_UI = true;

    private static final String ENTITY_API_URL = "/api/parametres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ParametreRepository parametreRepository;

    @Mock
    private ParametreRepository parametreRepositoryMock;

    @Mock
    private ParametreService parametreServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParametreMockMvc;

    private Parametre parametre;

    private Parametre insertedParametre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametre createEntity() {
        return new Parametre()
            .cle(DEFAULT_CLE)
            .libelle(DEFAULT_LIBELLE)
            .valeur(DEFAULT_VALEUR)
            .typeValeur(DEFAULT_TYPE_VALEUR)
            .modifiableUi(DEFAULT_MODIFIABLE_UI);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parametre createUpdatedEntity() {
        return new Parametre()
            .cle(UPDATED_CLE)
            .libelle(UPDATED_LIBELLE)
            .valeur(UPDATED_VALEUR)
            .typeValeur(UPDATED_TYPE_VALEUR)
            .modifiableUi(UPDATED_MODIFIABLE_UI);
    }

    @BeforeEach
    void initTest() {
        parametre = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedParametre != null) {
            parametreRepository.delete(insertedParametre);
            insertedParametre = null;
        }
    }

    @Test
    @Transactional
    void createParametre() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Parametre
        var returnedParametre = om.readValue(
            restParametreMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parametre)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Parametre.class
        );

        // Validate the Parametre in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertParametreUpdatableFieldsEquals(returnedParametre, getPersistedParametre(returnedParametre));

        insertedParametre = returnedParametre;
    }

    @Test
    @Transactional
    void createParametreWithExistingId() throws Exception {
        // Create the Parametre with an existing ID
        parametre.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParametreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parametre)))
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        parametre.setCle(null);

        // Create the Parametre, which fails.

        restParametreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parametre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeValeurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        parametre.setTypeValeur(null);

        // Create the Parametre, which fails.

        restParametreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parametre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModifiableUiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        parametre.setModifiableUi(null);

        // Create the Parametre, which fails.

        restParametreMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parametre)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParametres() throws Exception {
        // Initialize the database
        insertedParametre = parametreRepository.saveAndFlush(parametre);

        // Get all the parametreList
        restParametreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parametre.getId().intValue())))
            .andExpect(jsonPath("$.[*].cle").value(hasItem(DEFAULT_CLE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].typeValeur").value(hasItem(DEFAULT_TYPE_VALEUR.toString())))
            .andExpect(jsonPath("$.[*].modifiableUi").value(hasItem(DEFAULT_MODIFIABLE_UI)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllParametresWithEagerRelationshipsIsEnabled() throws Exception {
        when(parametreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restParametreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(parametreServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllParametresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(parametreServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restParametreMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(parametreRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getParametre() throws Exception {
        // Initialize the database
        insertedParametre = parametreRepository.saveAndFlush(parametre);

        // Get the parametre
        restParametreMockMvc
            .perform(get(ENTITY_API_URL_ID, parametre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parametre.getId().intValue()))
            .andExpect(jsonPath("$.cle").value(DEFAULT_CLE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.typeValeur").value(DEFAULT_TYPE_VALEUR.toString()))
            .andExpect(jsonPath("$.modifiableUi").value(DEFAULT_MODIFIABLE_UI));
    }

    @Test
    @Transactional
    void getNonExistingParametre() throws Exception {
        // Get the parametre
        restParametreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingParametre() throws Exception {
        // Initialize the database
        insertedParametre = parametreRepository.saveAndFlush(parametre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parametre
        Parametre updatedParametre = parametreRepository.findById(parametre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedParametre are not directly saved in db
        em.detach(updatedParametre);
        updatedParametre
            .cle(UPDATED_CLE)
            .libelle(UPDATED_LIBELLE)
            .valeur(UPDATED_VALEUR)
            .typeValeur(UPDATED_TYPE_VALEUR)
            .modifiableUi(UPDATED_MODIFIABLE_UI);

        restParametreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParametre.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedParametre))
            )
            .andExpect(status().isOk());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedParametreToMatchAllProperties(updatedParametre);
    }

    @Test
    @Transactional
    void putNonExistingParametre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parametre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parametre.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parametre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParametre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parametre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(parametre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParametre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parametre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(parametre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParametreWithPatch() throws Exception {
        // Initialize the database
        insertedParametre = parametreRepository.saveAndFlush(parametre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parametre using partial update
        Parametre partialUpdatedParametre = new Parametre();
        partialUpdatedParametre.setId(parametre.getId());

        partialUpdatedParametre.cle(UPDATED_CLE).libelle(UPDATED_LIBELLE);

        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParametre))
            )
            .andExpect(status().isOk());

        // Validate the Parametre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParametreUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedParametre, parametre),
            getPersistedParametre(parametre)
        );
    }

    @Test
    @Transactional
    void fullUpdateParametreWithPatch() throws Exception {
        // Initialize the database
        insertedParametre = parametreRepository.saveAndFlush(parametre);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the parametre using partial update
        Parametre partialUpdatedParametre = new Parametre();
        partialUpdatedParametre.setId(parametre.getId());

        partialUpdatedParametre
            .cle(UPDATED_CLE)
            .libelle(UPDATED_LIBELLE)
            .valeur(UPDATED_VALEUR)
            .typeValeur(UPDATED_TYPE_VALEUR)
            .modifiableUi(UPDATED_MODIFIABLE_UI);

        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParametre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedParametre))
            )
            .andExpect(status().isOk());

        // Validate the Parametre in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertParametreUpdatableFieldsEquals(partialUpdatedParametre, getPersistedParametre(partialUpdatedParametre));
    }

    @Test
    @Transactional
    void patchNonExistingParametre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parametre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parametre.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parametre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParametre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parametre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(parametre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParametre() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        parametre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParametreMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(parametre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parametre in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParametre() throws Exception {
        // Initialize the database
        insertedParametre = parametreRepository.saveAndFlush(parametre);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the parametre
        restParametreMockMvc
            .perform(delete(ENTITY_API_URL_ID, parametre.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return parametreRepository.count();
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

    protected Parametre getPersistedParametre(Parametre parametre) {
        return parametreRepository.findById(parametre.getId()).orElseThrow();
    }

    protected void assertPersistedParametreToMatchAllProperties(Parametre expectedParametre) {
        assertParametreAllPropertiesEquals(expectedParametre, getPersistedParametre(expectedParametre));
    }

    protected void assertPersistedParametreToMatchUpdatableProperties(Parametre expectedParametre) {
        assertParametreAllUpdatablePropertiesEquals(expectedParametre, getPersistedParametre(expectedParametre));
    }
}
