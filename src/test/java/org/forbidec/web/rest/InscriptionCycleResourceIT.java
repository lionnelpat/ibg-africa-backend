package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.InscriptionCycleAsserts.*;
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
import org.forbidec.domain.Cycle;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.repository.InscriptionCycleRepository;
import org.forbidec.service.InscriptionCycleService;
import org.forbidec.service.dto.InscriptionCycleDTO;
import org.forbidec.service.mapper.InscriptionCycleMapper;
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
 * Integration tests for the {@link InscriptionCycleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InscriptionCycleResourceIT {

    private static final LocalDate DEFAULT_DATE_INSCRIPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INSCRIPTION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_INSCRIPTION = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_CYCLE_TERMINE = false;
    private static final Boolean UPDATED_CYCLE_TERMINE = true;

    private static final String DEFAULT_GROUPE = "AAAAAAAAAA";
    private static final String UPDATED_GROUPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_1 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_2 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_3 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_5 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_5 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inscription-cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InscriptionCycleRepository inscriptionCycleRepository;

    @Mock
    private InscriptionCycleRepository inscriptionCycleRepositoryMock;

    @Autowired
    private InscriptionCycleMapper inscriptionCycleMapper;

    @Mock
    private InscriptionCycleService inscriptionCycleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionCycleMockMvc;

    private InscriptionCycle inscriptionCycle;

    private InscriptionCycle insertedInscriptionCycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionCycle createEntity(EntityManager em) {
        InscriptionCycle inscriptionCycle = new InscriptionCycle()
            .dateInscription(DEFAULT_DATE_INSCRIPTION)
            .cycleTermine(DEFAULT_CYCLE_TERMINE)
            .groupe(DEFAULT_GROUPE)
            .commentaire1(DEFAULT_COMMENTAIRE_1)
            .commentaire2(DEFAULT_COMMENTAIRE_2)
            .commentaire3(DEFAULT_COMMENTAIRE_3)
            .commentaire5(DEFAULT_COMMENTAIRE_5);
        // Add required entity
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            cycle = CycleResourceIT.createEntity(em);
            em.persist(cycle);
            em.flush();
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        inscriptionCycle.setCycle(cycle);
        // Add required entity
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            etudiant = EtudiantResourceIT.createEntity();
            em.persist(etudiant);
            em.flush();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        inscriptionCycle.setEtudiant(etudiant);
        return inscriptionCycle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InscriptionCycle createUpdatedEntity(EntityManager em) {
        InscriptionCycle updatedInscriptionCycle = new InscriptionCycle()
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .cycleTermine(UPDATED_CYCLE_TERMINE)
            .groupe(UPDATED_GROUPE)
            .commentaire1(UPDATED_COMMENTAIRE_1)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .commentaire5(UPDATED_COMMENTAIRE_5);
        // Add required entity
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            cycle = CycleResourceIT.createUpdatedEntity(em);
            em.persist(cycle);
            em.flush();
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        updatedInscriptionCycle.setCycle(cycle);
        // Add required entity
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            etudiant = EtudiantResourceIT.createUpdatedEntity();
            em.persist(etudiant);
            em.flush();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        updatedInscriptionCycle.setEtudiant(etudiant);
        return updatedInscriptionCycle;
    }

    @BeforeEach
    void initTest() {
        inscriptionCycle = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInscriptionCycle != null) {
            inscriptionCycleRepository.delete(insertedInscriptionCycle);
            insertedInscriptionCycle = null;
        }
    }

    @Test
    @Transactional
    void createInscriptionCycle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);
        var returnedInscriptionCycleDTO = om.readValue(
            restInscriptionCycleMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(inscriptionCycleDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InscriptionCycleDTO.class
        );

        // Validate the InscriptionCycle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInscriptionCycle = inscriptionCycleMapper.toEntity(returnedInscriptionCycleDTO);
        assertInscriptionCycleUpdatableFieldsEquals(returnedInscriptionCycle, getPersistedInscriptionCycle(returnedInscriptionCycle));

        insertedInscriptionCycle = returnedInscriptionCycle;
    }

    @Test
    @Transactional
    void createInscriptionCycleWithExistingId() throws Exception {
        // Create the InscriptionCycle with an existing ID
        inscriptionCycle.setId(1L);
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionCycleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCycleTermineIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inscriptionCycle.setCycleTermine(null);

        // Create the InscriptionCycle, which fails.
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        restInscriptionCycleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInscriptionCycles() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList
        restInscriptionCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].cycleTermine").value(hasItem(DEFAULT_CYCLE_TERMINE)))
            .andExpect(jsonPath("$.[*].groupe").value(hasItem(DEFAULT_GROUPE)))
            .andExpect(jsonPath("$.[*].commentaire1").value(hasItem(DEFAULT_COMMENTAIRE_1)))
            .andExpect(jsonPath("$.[*].commentaire2").value(hasItem(DEFAULT_COMMENTAIRE_2)))
            .andExpect(jsonPath("$.[*].commentaire3").value(hasItem(DEFAULT_COMMENTAIRE_3)))
            .andExpect(jsonPath("$.[*].commentaire5").value(hasItem(DEFAULT_COMMENTAIRE_5)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscriptionCyclesWithEagerRelationshipsIsEnabled() throws Exception {
        when(inscriptionCycleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscriptionCycleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inscriptionCycleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscriptionCyclesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inscriptionCycleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscriptionCycleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inscriptionCycleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInscriptionCycle() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get the inscriptionCycle
        restInscriptionCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, inscriptionCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscriptionCycle.getId().intValue()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.cycleTermine").value(DEFAULT_CYCLE_TERMINE))
            .andExpect(jsonPath("$.groupe").value(DEFAULT_GROUPE))
            .andExpect(jsonPath("$.commentaire1").value(DEFAULT_COMMENTAIRE_1))
            .andExpect(jsonPath("$.commentaire2").value(DEFAULT_COMMENTAIRE_2))
            .andExpect(jsonPath("$.commentaire3").value(DEFAULT_COMMENTAIRE_3))
            .andExpect(jsonPath("$.commentaire5").value(DEFAULT_COMMENTAIRE_5));
    }

    @Test
    @Transactional
    void getInscriptionCyclesByIdFiltering() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        Long id = inscriptionCycle.getId();

        defaultInscriptionCycleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInscriptionCycleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInscriptionCycleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription equals to
        defaultInscriptionCycleFiltering(
            "dateInscription.equals=" + DEFAULT_DATE_INSCRIPTION,
            "dateInscription.equals=" + UPDATED_DATE_INSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription in
        defaultInscriptionCycleFiltering(
            "dateInscription.in=" + DEFAULT_DATE_INSCRIPTION + "," + UPDATED_DATE_INSCRIPTION,
            "dateInscription.in=" + UPDATED_DATE_INSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription is not null
        defaultInscriptionCycleFiltering("dateInscription.specified=true", "dateInscription.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription is greater than or equal to
        defaultInscriptionCycleFiltering(
            "dateInscription.greaterThanOrEqual=" + DEFAULT_DATE_INSCRIPTION,
            "dateInscription.greaterThanOrEqual=" + UPDATED_DATE_INSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription is less than or equal to
        defaultInscriptionCycleFiltering(
            "dateInscription.lessThanOrEqual=" + DEFAULT_DATE_INSCRIPTION,
            "dateInscription.lessThanOrEqual=" + SMALLER_DATE_INSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription is less than
        defaultInscriptionCycleFiltering(
            "dateInscription.lessThan=" + UPDATED_DATE_INSCRIPTION,
            "dateInscription.lessThan=" + DEFAULT_DATE_INSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByDateInscriptionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where dateInscription is greater than
        defaultInscriptionCycleFiltering(
            "dateInscription.greaterThan=" + SMALLER_DATE_INSCRIPTION,
            "dateInscription.greaterThan=" + DEFAULT_DATE_INSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCycleTermineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where cycleTermine equals to
        defaultInscriptionCycleFiltering("cycleTermine.equals=" + DEFAULT_CYCLE_TERMINE, "cycleTermine.equals=" + UPDATED_CYCLE_TERMINE);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCycleTermineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where cycleTermine in
        defaultInscriptionCycleFiltering(
            "cycleTermine.in=" + DEFAULT_CYCLE_TERMINE + "," + UPDATED_CYCLE_TERMINE,
            "cycleTermine.in=" + UPDATED_CYCLE_TERMINE
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCycleTermineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where cycleTermine is not null
        defaultInscriptionCycleFiltering("cycleTermine.specified=true", "cycleTermine.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByGroupeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where groupe equals to
        defaultInscriptionCycleFiltering("groupe.equals=" + DEFAULT_GROUPE, "groupe.equals=" + UPDATED_GROUPE);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByGroupeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where groupe in
        defaultInscriptionCycleFiltering("groupe.in=" + DEFAULT_GROUPE + "," + UPDATED_GROUPE, "groupe.in=" + UPDATED_GROUPE);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByGroupeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where groupe is not null
        defaultInscriptionCycleFiltering("groupe.specified=true", "groupe.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByGroupeContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where groupe contains
        defaultInscriptionCycleFiltering("groupe.contains=" + DEFAULT_GROUPE, "groupe.contains=" + UPDATED_GROUPE);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByGroupeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where groupe does not contain
        defaultInscriptionCycleFiltering("groupe.doesNotContain=" + UPDATED_GROUPE, "groupe.doesNotContain=" + DEFAULT_GROUPE);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire1 equals to
        defaultInscriptionCycleFiltering("commentaire1.equals=" + DEFAULT_COMMENTAIRE_1, "commentaire1.equals=" + UPDATED_COMMENTAIRE_1);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire1 in
        defaultInscriptionCycleFiltering(
            "commentaire1.in=" + DEFAULT_COMMENTAIRE_1 + "," + UPDATED_COMMENTAIRE_1,
            "commentaire1.in=" + UPDATED_COMMENTAIRE_1
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire1 is not null
        defaultInscriptionCycleFiltering("commentaire1.specified=true", "commentaire1.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire1ContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire1 contains
        defaultInscriptionCycleFiltering(
            "commentaire1.contains=" + DEFAULT_COMMENTAIRE_1,
            "commentaire1.contains=" + UPDATED_COMMENTAIRE_1
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire1 does not contain
        defaultInscriptionCycleFiltering(
            "commentaire1.doesNotContain=" + UPDATED_COMMENTAIRE_1,
            "commentaire1.doesNotContain=" + DEFAULT_COMMENTAIRE_1
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire2 equals to
        defaultInscriptionCycleFiltering("commentaire2.equals=" + DEFAULT_COMMENTAIRE_2, "commentaire2.equals=" + UPDATED_COMMENTAIRE_2);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire2 in
        defaultInscriptionCycleFiltering(
            "commentaire2.in=" + DEFAULT_COMMENTAIRE_2 + "," + UPDATED_COMMENTAIRE_2,
            "commentaire2.in=" + UPDATED_COMMENTAIRE_2
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire2 is not null
        defaultInscriptionCycleFiltering("commentaire2.specified=true", "commentaire2.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire2ContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire2 contains
        defaultInscriptionCycleFiltering(
            "commentaire2.contains=" + DEFAULT_COMMENTAIRE_2,
            "commentaire2.contains=" + UPDATED_COMMENTAIRE_2
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire2 does not contain
        defaultInscriptionCycleFiltering(
            "commentaire2.doesNotContain=" + UPDATED_COMMENTAIRE_2,
            "commentaire2.doesNotContain=" + DEFAULT_COMMENTAIRE_2
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire3 equals to
        defaultInscriptionCycleFiltering("commentaire3.equals=" + DEFAULT_COMMENTAIRE_3, "commentaire3.equals=" + UPDATED_COMMENTAIRE_3);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire3 in
        defaultInscriptionCycleFiltering(
            "commentaire3.in=" + DEFAULT_COMMENTAIRE_3 + "," + UPDATED_COMMENTAIRE_3,
            "commentaire3.in=" + UPDATED_COMMENTAIRE_3
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire3 is not null
        defaultInscriptionCycleFiltering("commentaire3.specified=true", "commentaire3.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire3ContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire3 contains
        defaultInscriptionCycleFiltering(
            "commentaire3.contains=" + DEFAULT_COMMENTAIRE_3,
            "commentaire3.contains=" + UPDATED_COMMENTAIRE_3
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire3 does not contain
        defaultInscriptionCycleFiltering(
            "commentaire3.doesNotContain=" + UPDATED_COMMENTAIRE_3,
            "commentaire3.doesNotContain=" + DEFAULT_COMMENTAIRE_3
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire5IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire5 equals to
        defaultInscriptionCycleFiltering("commentaire5.equals=" + DEFAULT_COMMENTAIRE_5, "commentaire5.equals=" + UPDATED_COMMENTAIRE_5);
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire5IsInShouldWork() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire5 in
        defaultInscriptionCycleFiltering(
            "commentaire5.in=" + DEFAULT_COMMENTAIRE_5 + "," + UPDATED_COMMENTAIRE_5,
            "commentaire5.in=" + UPDATED_COMMENTAIRE_5
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire5IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire5 is not null
        defaultInscriptionCycleFiltering("commentaire5.specified=true", "commentaire5.specified=false");
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire5ContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire5 contains
        defaultInscriptionCycleFiltering(
            "commentaire5.contains=" + DEFAULT_COMMENTAIRE_5,
            "commentaire5.contains=" + UPDATED_COMMENTAIRE_5
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCommentaire5NotContainsSomething() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        // Get all the inscriptionCycleList where commentaire5 does not contain
        defaultInscriptionCycleFiltering(
            "commentaire5.doesNotContain=" + UPDATED_COMMENTAIRE_5,
            "commentaire5.doesNotContain=" + DEFAULT_COMMENTAIRE_5
        );
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByCycleIsEqualToSomething() throws Exception {
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            inscriptionCycleRepository.saveAndFlush(inscriptionCycle);
            cycle = CycleResourceIT.createEntity(em);
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        em.persist(cycle);
        em.flush();
        inscriptionCycle.setCycle(cycle);
        inscriptionCycleRepository.saveAndFlush(inscriptionCycle);
        Long cycleId = cycle.getId();
        // Get all the inscriptionCycleList where cycle equals to cycleId
        defaultInscriptionCycleShouldBeFound("cycleId.equals=" + cycleId);

        // Get all the inscriptionCycleList where cycle equals to (cycleId + 1)
        defaultInscriptionCycleShouldNotBeFound("cycleId.equals=" + (cycleId + 1));
    }

    @Test
    @Transactional
    void getAllInscriptionCyclesByEtudiantIsEqualToSomething() throws Exception {
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            inscriptionCycleRepository.saveAndFlush(inscriptionCycle);
            etudiant = EtudiantResourceIT.createEntity();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        em.persist(etudiant);
        em.flush();
        inscriptionCycle.setEtudiant(etudiant);
        inscriptionCycleRepository.saveAndFlush(inscriptionCycle);
        Long etudiantId = etudiant.getId();
        // Get all the inscriptionCycleList where etudiant equals to etudiantId
        defaultInscriptionCycleShouldBeFound("etudiantId.equals=" + etudiantId);

        // Get all the inscriptionCycleList where etudiant equals to (etudiantId + 1)
        defaultInscriptionCycleShouldNotBeFound("etudiantId.equals=" + (etudiantId + 1));
    }

    private void defaultInscriptionCycleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInscriptionCycleShouldBeFound(shouldBeFound);
        defaultInscriptionCycleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInscriptionCycleShouldBeFound(String filter) throws Exception {
        restInscriptionCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscriptionCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].cycleTermine").value(hasItem(DEFAULT_CYCLE_TERMINE)))
            .andExpect(jsonPath("$.[*].groupe").value(hasItem(DEFAULT_GROUPE)))
            .andExpect(jsonPath("$.[*].commentaire1").value(hasItem(DEFAULT_COMMENTAIRE_1)))
            .andExpect(jsonPath("$.[*].commentaire2").value(hasItem(DEFAULT_COMMENTAIRE_2)))
            .andExpect(jsonPath("$.[*].commentaire3").value(hasItem(DEFAULT_COMMENTAIRE_3)))
            .andExpect(jsonPath("$.[*].commentaire5").value(hasItem(DEFAULT_COMMENTAIRE_5)));

        // Check, that the count call also returns 1
        restInscriptionCycleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInscriptionCycleShouldNotBeFound(String filter) throws Exception {
        restInscriptionCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInscriptionCycleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInscriptionCycle() throws Exception {
        // Get the inscriptionCycle
        restInscriptionCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscriptionCycle() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscriptionCycle
        InscriptionCycle updatedInscriptionCycle = inscriptionCycleRepository.findById(inscriptionCycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInscriptionCycle are not directly saved in db
        em.detach(updatedInscriptionCycle);
        updatedInscriptionCycle
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .cycleTermine(UPDATED_CYCLE_TERMINE)
            .groupe(UPDATED_GROUPE)
            .commentaire1(UPDATED_COMMENTAIRE_1)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .commentaire5(UPDATED_COMMENTAIRE_5);
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(updatedInscriptionCycle);

        restInscriptionCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionCycleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInscriptionCycleToMatchAllProperties(updatedInscriptionCycle);
    }

    @Test
    @Transactional
    void putNonExistingInscriptionCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscriptionCycle.setId(longCount.incrementAndGet());

        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionCycleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscriptionCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscriptionCycle.setId(longCount.incrementAndGet());

        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscriptionCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscriptionCycle.setId(longCount.incrementAndGet());

        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionCycleMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionCycleWithPatch() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscriptionCycle using partial update
        InscriptionCycle partialUpdatedInscriptionCycle = new InscriptionCycle();
        partialUpdatedInscriptionCycle.setId(inscriptionCycle.getId());

        partialUpdatedInscriptionCycle.dateInscription(UPDATED_DATE_INSCRIPTION).groupe(UPDATED_GROUPE);

        restInscriptionCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscriptionCycle))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionCycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscriptionCycleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInscriptionCycle, inscriptionCycle),
            getPersistedInscriptionCycle(inscriptionCycle)
        );
    }

    @Test
    @Transactional
    void fullUpdateInscriptionCycleWithPatch() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inscriptionCycle using partial update
        InscriptionCycle partialUpdatedInscriptionCycle = new InscriptionCycle();
        partialUpdatedInscriptionCycle.setId(inscriptionCycle.getId());

        partialUpdatedInscriptionCycle
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .cycleTermine(UPDATED_CYCLE_TERMINE)
            .groupe(UPDATED_GROUPE)
            .commentaire1(UPDATED_COMMENTAIRE_1)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .commentaire5(UPDATED_COMMENTAIRE_5);

        restInscriptionCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscriptionCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInscriptionCycle))
            )
            .andExpect(status().isOk());

        // Validate the InscriptionCycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInscriptionCycleUpdatableFieldsEquals(
            partialUpdatedInscriptionCycle,
            getPersistedInscriptionCycle(partialUpdatedInscriptionCycle)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInscriptionCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscriptionCycle.setId(longCount.incrementAndGet());

        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionCycleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscriptionCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscriptionCycle.setId(longCount.incrementAndGet());

        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscriptionCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inscriptionCycle.setId(longCount.incrementAndGet());

        // Create the InscriptionCycle
        InscriptionCycleDTO inscriptionCycleDTO = inscriptionCycleMapper.toDto(inscriptionCycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionCycleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inscriptionCycleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InscriptionCycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscriptionCycle() throws Exception {
        // Initialize the database
        insertedInscriptionCycle = inscriptionCycleRepository.saveAndFlush(inscriptionCycle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inscriptionCycle
        restInscriptionCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscriptionCycle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inscriptionCycleRepository.count();
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

    protected InscriptionCycle getPersistedInscriptionCycle(InscriptionCycle inscriptionCycle) {
        return inscriptionCycleRepository.findById(inscriptionCycle.getId()).orElseThrow();
    }

    protected void assertPersistedInscriptionCycleToMatchAllProperties(InscriptionCycle expectedInscriptionCycle) {
        assertInscriptionCycleAllPropertiesEquals(expectedInscriptionCycle, getPersistedInscriptionCycle(expectedInscriptionCycle));
    }

    protected void assertPersistedInscriptionCycleToMatchUpdatableProperties(InscriptionCycle expectedInscriptionCycle) {
        assertInscriptionCycleAllUpdatablePropertiesEquals(
            expectedInscriptionCycle,
            getPersistedInscriptionCycle(expectedInscriptionCycle)
        );
    }
}
