package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CentreFormationAsserts.*;
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
import org.forbidec.domain.CentreFormation;
import org.forbidec.domain.Pays;
import org.forbidec.repository.CentreFormationRepository;
import org.forbidec.service.CentreFormationService;
import org.forbidec.service.dto.CentreFormationDTO;
import org.forbidec.service.mapper.CentreFormationMapper;
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
 * Integration tests for the {@link CentreFormationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CentreFormationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTETE_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_ENTETE_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNATAIRE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_NB_CYCLES_CURSUS = 1;
    private static final Integer UPDATED_NB_CYCLES_CURSUS = 2;

    private static final BigDecimal DEFAULT_NOTE_MAXIMALE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOTE_MAXIMALE = new BigDecimal(2);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/centre-formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CentreFormationRepository centreFormationRepository;

    @Mock
    private CentreFormationRepository centreFormationRepositoryMock;

    @Autowired
    private CentreFormationMapper centreFormationMapper;

    @Mock
    private CentreFormationService centreFormationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCentreFormationMockMvc;

    private CentreFormation centreFormation;

    private CentreFormation insertedCentreFormation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CentreFormation createEntity(EntityManager em) {
        CentreFormation centreFormation = new CentreFormation()
            .code(DEFAULT_CODE)
            .nom(DEFAULT_NOM)
            .ville(DEFAULT_VILLE)
            .adresse(DEFAULT_ADRESSE)
            .enteteDocument(DEFAULT_ENTETE_DOCUMENT)
            .signataire(DEFAULT_SIGNATAIRE)
            .logoUrl(DEFAULT_LOGO_URL)
            .nbCyclesCursus(DEFAULT_NB_CYCLES_CURSUS)
            .noteMaximale(DEFAULT_NOTE_MAXIMALE)
            .actif(DEFAULT_ACTIF);
        // Add required entity
        Pays pays;
        if (TestUtil.findAll(em, Pays.class).isEmpty()) {
            pays = PaysResourceIT.createEntity();
            em.persist(pays);
            em.flush();
        } else {
            pays = TestUtil.findAll(em, Pays.class).get(0);
        }
        centreFormation.setPays(pays);
        return centreFormation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CentreFormation createUpdatedEntity(EntityManager em) {
        CentreFormation updatedCentreFormation = new CentreFormation()
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .enteteDocument(UPDATED_ENTETE_DOCUMENT)
            .signataire(UPDATED_SIGNATAIRE)
            .logoUrl(UPDATED_LOGO_URL)
            .nbCyclesCursus(UPDATED_NB_CYCLES_CURSUS)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .actif(UPDATED_ACTIF);
        // Add required entity
        Pays pays;
        if (TestUtil.findAll(em, Pays.class).isEmpty()) {
            pays = PaysResourceIT.createUpdatedEntity();
            em.persist(pays);
            em.flush();
        } else {
            pays = TestUtil.findAll(em, Pays.class).get(0);
        }
        updatedCentreFormation.setPays(pays);
        return updatedCentreFormation;
    }

    @BeforeEach
    void initTest() {
        centreFormation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCentreFormation != null) {
            centreFormationRepository.delete(insertedCentreFormation);
            insertedCentreFormation = null;
        }
    }

    @Test
    @Transactional
    void createCentreFormation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);
        var returnedCentreFormationDTO = om.readValue(
            restCentreFormationMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(centreFormationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CentreFormationDTO.class
        );

        // Validate the CentreFormation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCentreFormation = centreFormationMapper.toEntity(returnedCentreFormationDTO);
        assertCentreFormationUpdatableFieldsEquals(returnedCentreFormation, getPersistedCentreFormation(returnedCentreFormation));

        insertedCentreFormation = returnedCentreFormation;
    }

    @Test
    @Transactional
    void createCentreFormationWithExistingId() throws Exception {
        // Create the CentreFormation with an existing ID
        centreFormation.setId(1L);
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setCode(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setNom(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVilleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setVille(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSignataireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setSignataire(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNbCyclesCursusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setNbCyclesCursus(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteMaximaleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setNoteMaximale(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        centreFormation.setActif(null);

        // Create the CentreFormation, which fails.
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        restCentreFormationMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCentreFormations() throws Exception {
        // Initialize the database
        insertedCentreFormation = centreFormationRepository.saveAndFlush(centreFormation);

        // Get all the centreFormationList
        restCentreFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centreFormation.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].enteteDocument").value(hasItem(DEFAULT_ENTETE_DOCUMENT)))
            .andExpect(jsonPath("$.[*].signataire").value(hasItem(DEFAULT_SIGNATAIRE)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)))
            .andExpect(jsonPath("$.[*].nbCyclesCursus").value(hasItem(DEFAULT_NB_CYCLES_CURSUS)))
            .andExpect(jsonPath("$.[*].noteMaximale").value(hasItem(sameNumber(DEFAULT_NOTE_MAXIMALE))))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCentreFormationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(centreFormationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCentreFormationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(centreFormationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCentreFormationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(centreFormationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCentreFormationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(centreFormationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCentreFormation() throws Exception {
        // Initialize the database
        insertedCentreFormation = centreFormationRepository.saveAndFlush(centreFormation);

        // Get the centreFormation
        restCentreFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, centreFormation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(centreFormation.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.enteteDocument").value(DEFAULT_ENTETE_DOCUMENT))
            .andExpect(jsonPath("$.signataire").value(DEFAULT_SIGNATAIRE))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL))
            .andExpect(jsonPath("$.nbCyclesCursus").value(DEFAULT_NB_CYCLES_CURSUS))
            .andExpect(jsonPath("$.noteMaximale").value(sameNumber(DEFAULT_NOTE_MAXIMALE)))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getNonExistingCentreFormation() throws Exception {
        // Get the centreFormation
        restCentreFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCentreFormation() throws Exception {
        // Initialize the database
        insertedCentreFormation = centreFormationRepository.saveAndFlush(centreFormation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the centreFormation
        CentreFormation updatedCentreFormation = centreFormationRepository.findById(centreFormation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCentreFormation are not directly saved in db
        em.detach(updatedCentreFormation);
        updatedCentreFormation
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .enteteDocument(UPDATED_ENTETE_DOCUMENT)
            .signataire(UPDATED_SIGNATAIRE)
            .logoUrl(UPDATED_LOGO_URL)
            .nbCyclesCursus(UPDATED_NB_CYCLES_CURSUS)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .actif(UPDATED_ACTIF);
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(updatedCentreFormation);

        restCentreFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centreFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCentreFormationToMatchAllProperties(updatedCentreFormation);
    }

    @Test
    @Transactional
    void putNonExistingCentreFormation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centreFormation.setId(longCount.incrementAndGet());

        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, centreFormationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCentreFormation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centreFormation.setId(longCount.incrementAndGet());

        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCentreFormation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centreFormation.setId(longCount.incrementAndGet());

        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreFormationMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCentreFormationWithPatch() throws Exception {
        // Initialize the database
        insertedCentreFormation = centreFormationRepository.saveAndFlush(centreFormation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the centreFormation using partial update
        CentreFormation partialUpdatedCentreFormation = new CentreFormation();
        partialUpdatedCentreFormation.setId(centreFormation.getId());

        partialUpdatedCentreFormation.nom(UPDATED_NOM).noteMaximale(UPDATED_NOTE_MAXIMALE).actif(UPDATED_ACTIF);

        restCentreFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentreFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCentreFormation))
            )
            .andExpect(status().isOk());

        // Validate the CentreFormation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCentreFormationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCentreFormation, centreFormation),
            getPersistedCentreFormation(centreFormation)
        );
    }

    @Test
    @Transactional
    void fullUpdateCentreFormationWithPatch() throws Exception {
        // Initialize the database
        insertedCentreFormation = centreFormationRepository.saveAndFlush(centreFormation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the centreFormation using partial update
        CentreFormation partialUpdatedCentreFormation = new CentreFormation();
        partialUpdatedCentreFormation.setId(centreFormation.getId());

        partialUpdatedCentreFormation
            .code(UPDATED_CODE)
            .nom(UPDATED_NOM)
            .ville(UPDATED_VILLE)
            .adresse(UPDATED_ADRESSE)
            .enteteDocument(UPDATED_ENTETE_DOCUMENT)
            .signataire(UPDATED_SIGNATAIRE)
            .logoUrl(UPDATED_LOGO_URL)
            .nbCyclesCursus(UPDATED_NB_CYCLES_CURSUS)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .actif(UPDATED_ACTIF);

        restCentreFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCentreFormation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCentreFormation))
            )
            .andExpect(status().isOk());

        // Validate the CentreFormation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCentreFormationUpdatableFieldsEquals(
            partialUpdatedCentreFormation,
            getPersistedCentreFormation(partialUpdatedCentreFormation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCentreFormation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centreFormation.setId(longCount.incrementAndGet());

        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, centreFormationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCentreFormation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centreFormation.setId(longCount.incrementAndGet());

        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCentreFormation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        centreFormation.setId(longCount.incrementAndGet());

        // Create the CentreFormation
        CentreFormationDTO centreFormationDTO = centreFormationMapper.toDto(centreFormation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCentreFormationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(centreFormationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CentreFormation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCentreFormation() throws Exception {
        // Initialize the database
        insertedCentreFormation = centreFormationRepository.saveAndFlush(centreFormation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the centreFormation
        restCentreFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, centreFormation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return centreFormationRepository.count();
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

    protected CentreFormation getPersistedCentreFormation(CentreFormation centreFormation) {
        return centreFormationRepository.findById(centreFormation.getId()).orElseThrow();
    }

    protected void assertPersistedCentreFormationToMatchAllProperties(CentreFormation expectedCentreFormation) {
        assertCentreFormationAllPropertiesEquals(expectedCentreFormation, getPersistedCentreFormation(expectedCentreFormation));
    }

    protected void assertPersistedCentreFormationToMatchUpdatableProperties(CentreFormation expectedCentreFormation) {
        assertCentreFormationAllUpdatablePropertiesEquals(expectedCentreFormation, getPersistedCentreFormation(expectedCentreFormation));
    }
}
