package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CycleAsserts.*;
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
import org.forbidec.domain.CentreFormation;
import org.forbidec.domain.Cycle;
import org.forbidec.repository.CycleRepository;
import org.forbidec.service.CycleService;
import org.forbidec.service.dto.CycleDTO;
import org.forbidec.service.mapper.CycleMapper;
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
 * Integration tests for the {@link CycleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CycleResourceIT {

    private static final Integer DEFAULT_ANNEE = 1900;
    private static final Integer UPDATED_ANNEE = 1901;
    private static final Integer SMALLER_ANNEE = 1900 - 1;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_CLOTURE = false;
    private static final Boolean UPDATED_CLOTURE = true;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cycles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CycleRepository cycleRepository;

    @Mock
    private CycleRepository cycleRepositoryMock;

    @Autowired
    private CycleMapper cycleMapper;

    @Mock
    private CycleService cycleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCycleMockMvc;

    private Cycle cycle;

    private Cycle insertedCycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createEntity(EntityManager em) {
        Cycle cycle = new Cycle()
            .annee(DEFAULT_ANNEE)
            .libelle(DEFAULT_LIBELLE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .cloture(DEFAULT_CLOTURE)
            .commentaire(DEFAULT_COMMENTAIRE);
        // Add required entity
        CentreFormation centreFormation;
        if (TestUtil.findAll(em, CentreFormation.class).isEmpty()) {
            centreFormation = CentreFormationResourceIT.createEntity(em);
            em.persist(centreFormation);
            em.flush();
        } else {
            centreFormation = TestUtil.findAll(em, CentreFormation.class).get(0);
        }
        cycle.setCentre(centreFormation);
        return cycle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycle createUpdatedEntity(EntityManager em) {
        Cycle updatedCycle = new Cycle()
            .annee(UPDATED_ANNEE)
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .cloture(UPDATED_CLOTURE)
            .commentaire(UPDATED_COMMENTAIRE);
        // Add required entity
        CentreFormation centreFormation;
        if (TestUtil.findAll(em, CentreFormation.class).isEmpty()) {
            centreFormation = CentreFormationResourceIT.createUpdatedEntity(em);
            em.persist(centreFormation);
            em.flush();
        } else {
            centreFormation = TestUtil.findAll(em, CentreFormation.class).get(0);
        }
        updatedCycle.setCentre(centreFormation);
        return updatedCycle;
    }

    @BeforeEach
    void initTest() {
        cycle = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCycle != null) {
            cycleRepository.delete(insertedCycle);
            insertedCycle = null;
        }
    }

    @Test
    @Transactional
    void createCycle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);
        var returnedCycleDTO = om.readValue(
            restCycleMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cycleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CycleDTO.class
        );

        // Validate the Cycle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCycle = cycleMapper.toEntity(returnedCycleDTO);
        assertCycleUpdatableFieldsEquals(returnedCycle, getPersistedCycle(returnedCycle));

        insertedCycle = returnedCycle;
    }

    @Test
    @Transactional
    void createCycleWithExistingId() throws Exception {
        // Create the Cycle with an existing ID
        cycle.setId(1L);
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCycleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cycleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnneeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cycle.setAnnee(null);

        // Create the Cycle, which fails.
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        restCycleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cycleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClotureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cycle.setCloture(null);

        // Create the Cycle, which fails.
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        restCycleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cycleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCycles() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].cloture").value(hasItem(DEFAULT_CLOTURE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCyclesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cycleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCycleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cycleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCyclesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cycleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCycleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cycleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCycle() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get the cycle
        restCycleMockMvc
            .perform(get(ENTITY_API_URL_ID, cycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cycle.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.cloture").value(DEFAULT_CLOTURE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getCyclesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        Long id = cycle.getId();

        defaultCycleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCycleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCycleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee equals to
        defaultCycleFiltering("annee.equals=" + DEFAULT_ANNEE, "annee.equals=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee in
        defaultCycleFiltering("annee.in=" + DEFAULT_ANNEE + "," + UPDATED_ANNEE, "annee.in=" + UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee is not null
        defaultCycleFiltering("annee.specified=true", "annee.specified=false");
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee is greater than or equal to
        defaultCycleFiltering("annee.greaterThanOrEqual=" + DEFAULT_ANNEE, "annee.greaterThanOrEqual=" + (DEFAULT_ANNEE + 1));
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee is less than or equal to
        defaultCycleFiltering("annee.lessThanOrEqual=" + DEFAULT_ANNEE, "annee.lessThanOrEqual=" + SMALLER_ANNEE);
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee is less than
        defaultCycleFiltering("annee.lessThan=" + (DEFAULT_ANNEE + 1), "annee.lessThan=" + DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void getAllCyclesByAnneeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where annee is greater than
        defaultCycleFiltering("annee.greaterThan=" + SMALLER_ANNEE, "annee.greaterThan=" + DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void getAllCyclesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where libelle equals to
        defaultCycleFiltering("libelle.equals=" + DEFAULT_LIBELLE, "libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCyclesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where libelle in
        defaultCycleFiltering("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE, "libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCyclesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where libelle is not null
        defaultCycleFiltering("libelle.specified=true", "libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllCyclesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where libelle contains
        defaultCycleFiltering("libelle.contains=" + DEFAULT_LIBELLE, "libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCyclesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where libelle does not contain
        defaultCycleFiltering("libelle.doesNotContain=" + UPDATED_LIBELLE, "libelle.doesNotContain=" + DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut equals to
        defaultCycleFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut in
        defaultCycleFiltering("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT, "dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut is not null
        defaultCycleFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut is greater than or equal to
        defaultCycleFiltering("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut is less than or equal to
        defaultCycleFiltering("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut is less than
        defaultCycleFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCyclesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateDebut is greater than
        defaultCycleFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin equals to
        defaultCycleFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin in
        defaultCycleFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin is not null
        defaultCycleFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin is greater than or equal to
        defaultCycleFiltering("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin is less than or equal to
        defaultCycleFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin is less than
        defaultCycleFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCyclesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where dateFin is greater than
        defaultCycleFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCyclesByClotureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where cloture equals to
        defaultCycleFiltering("cloture.equals=" + DEFAULT_CLOTURE, "cloture.equals=" + UPDATED_CLOTURE);
    }

    @Test
    @Transactional
    void getAllCyclesByClotureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where cloture in
        defaultCycleFiltering("cloture.in=" + DEFAULT_CLOTURE + "," + UPDATED_CLOTURE, "cloture.in=" + UPDATED_CLOTURE);
    }

    @Test
    @Transactional
    void getAllCyclesByClotureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where cloture is not null
        defaultCycleFiltering("cloture.specified=true", "cloture.specified=false");
    }

    @Test
    @Transactional
    void getAllCyclesByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where commentaire equals to
        defaultCycleFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCyclesByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where commentaire in
        defaultCycleFiltering("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE, "commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCyclesByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where commentaire is not null
        defaultCycleFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllCyclesByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where commentaire contains
        defaultCycleFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCyclesByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        // Get all the cycleList where commentaire does not contain
        defaultCycleFiltering("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE, "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCyclesByCentreIsEqualToSomething() throws Exception {
        CentreFormation centre;
        if (TestUtil.findAll(em, CentreFormation.class).isEmpty()) {
            cycleRepository.saveAndFlush(cycle);
            centre = CentreFormationResourceIT.createEntity(em);
        } else {
            centre = TestUtil.findAll(em, CentreFormation.class).get(0);
        }
        em.persist(centre);
        em.flush();
        cycle.setCentre(centre);
        cycleRepository.saveAndFlush(cycle);
        Long centreId = centre.getId();
        // Get all the cycleList where centre equals to centreId
        defaultCycleShouldBeFound("centreId.equals=" + centreId);

        // Get all the cycleList where centre equals to (centreId + 1)
        defaultCycleShouldNotBeFound("centreId.equals=" + (centreId + 1));
    }

    private void defaultCycleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCycleShouldBeFound(shouldBeFound);
        defaultCycleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCycleShouldBeFound(String filter) throws Exception {
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].cloture").value(hasItem(DEFAULT_CLOTURE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCycleShouldNotBeFound(String filter) throws Exception {
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCycleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCycle() throws Exception {
        // Get the cycle
        restCycleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCycle() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cycle
        Cycle updatedCycle = cycleRepository.findById(cycle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCycle are not directly saved in db
        em.detach(updatedCycle);
        updatedCycle
            .annee(UPDATED_ANNEE)
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .cloture(UPDATED_CLOTURE)
            .commentaire(UPDATED_COMMENTAIRE);
        CycleDTO cycleDTO = cycleMapper.toDto(updatedCycle);

        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cycleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCycleToMatchAllProperties(updatedCycle);
    }

    @Test
    @Transactional
    void putNonExistingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cycleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cycleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        partialUpdatedCycle
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCycleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCycle, cycle), getPersistedCycle(cycle));
    }

    @Test
    @Transactional
    void fullUpdateCycleWithPatch() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cycle using partial update
        Cycle partialUpdatedCycle = new Cycle();
        partialUpdatedCycle.setId(cycle.getId());

        partialUpdatedCycle
            .annee(UPDATED_ANNEE)
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .cloture(UPDATED_CLOTURE)
            .commentaire(UPDATED_COMMENTAIRE);

        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCycle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCycle))
            )
            .andExpect(status().isOk());

        // Validate the Cycle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCycleUpdatableFieldsEquals(partialUpdatedCycle, getPersistedCycle(partialUpdatedCycle));
    }

    @Test
    @Transactional
    void patchNonExistingCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cycleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cycleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCycle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cycle.setId(longCount.incrementAndGet());

        // Create the Cycle
        CycleDTO cycleDTO = cycleMapper.toDto(cycle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCycleMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cycleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cycle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCycle() throws Exception {
        // Initialize the database
        insertedCycle = cycleRepository.saveAndFlush(cycle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cycle
        restCycleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cycle.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cycleRepository.count();
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

    protected Cycle getPersistedCycle(Cycle cycle) {
        return cycleRepository.findById(cycle.getId()).orElseThrow();
    }

    protected void assertPersistedCycleToMatchAllProperties(Cycle expectedCycle) {
        assertCycleAllPropertiesEquals(expectedCycle, getPersistedCycle(expectedCycle));
    }

    protected void assertPersistedCycleToMatchUpdatableProperties(Cycle expectedCycle) {
        assertCycleAllUpdatablePropertiesEquals(expectedCycle, getPersistedCycle(expectedCycle));
    }
}
