package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CoursAsserts.*;
import static org.forbidec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.forbidec.web.rest.TestUtil.sameNumber;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.Cours;
import org.forbidec.repository.CoursRepository;
import org.forbidec.service.dto.CoursDTO;
import org.forbidec.service.mapper.CoursMapper;
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
 * Integration tests for the {@link CoursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoursResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_LONG = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_COURT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_COURT = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE_AFFICHAGE = 1;
    private static final Integer UPDATED_ORDRE_AFFICHAGE = 2;
    private static final Integer SMALLER_ORDRE_AFFICHAGE = 1 - 1;

    private static final Integer DEFAULT_NB_PERIODES = 0;
    private static final Integer UPDATED_NB_PERIODES = 1;
    private static final Integer SMALLER_NB_PERIODES = 0 - 1;

    private static final BigDecimal DEFAULT_COEFFICIENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_COEFFICIENT = new BigDecimal(1);
    private static final BigDecimal SMALLER_COEFFICIENT = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/cours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoursRepository coursRepository;

    @Autowired
    private CoursMapper coursMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoursMockMvc;

    private Cours cours;

    private Cours insertedCours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cours createEntity() {
        return new Cours()
            .intitule(DEFAULT_INTITULE)
            .libelleLong(DEFAULT_LIBELLE_LONG)
            .libelleCourt(DEFAULT_LIBELLE_COURT)
            .ordreAffichage(DEFAULT_ORDRE_AFFICHAGE)
            .nbPeriodes(DEFAULT_NB_PERIODES)
            .coefficient(DEFAULT_COEFFICIENT)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .commentaire(DEFAULT_COMMENTAIRE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cours createUpdatedEntity() {
        return new Cours()
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .nbPeriodes(UPDATED_NB_PERIODES)
            .coefficient(UPDATED_COEFFICIENT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        cours = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCours != null) {
            coursRepository.delete(insertedCours);
            insertedCours = null;
        }
    }

    @Test
    @Transactional
    void createCours() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);
        var returnedCoursDTO = om.readValue(
            restCoursMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CoursDTO.class
        );

        // Validate the Cours in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCours = coursMapper.toEntity(returnedCoursDTO);
        assertCoursUpdatableFieldsEquals(returnedCours, getPersistedCours(returnedCours));

        insertedCours = returnedCours;
    }

    @Test
    @Transactional
    void createCoursWithExistingId() throws Exception {
        // Create the Cours with an existing ID
        cours.setId(1L);
        CoursDTO coursDTO = coursMapper.toDto(cours);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setIntitule(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdreAffichageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setOrdreAffichage(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoefficientIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setCoefficient(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cours.setActif(null);

        // Create the Cours, which fails.
        CoursDTO coursDTO = coursMapper.toDto(cours);

        restCoursMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].ordreAffichage").value(hasItem(DEFAULT_ORDRE_AFFICHAGE)))
            .andExpect(jsonPath("$.[*].nbPeriodes").value(hasItem(DEFAULT_NB_PERIODES)))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(sameNumber(DEFAULT_COEFFICIENT))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get the cours
        restCoursMockMvc
            .perform(get(ENTITY_API_URL_ID, cours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cours.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.libelleLong").value(DEFAULT_LIBELLE_LONG))
            .andExpect(jsonPath("$.libelleCourt").value(DEFAULT_LIBELLE_COURT))
            .andExpect(jsonPath("$.ordreAffichage").value(DEFAULT_ORDRE_AFFICHAGE))
            .andExpect(jsonPath("$.nbPeriodes").value(DEFAULT_NB_PERIODES))
            .andExpect(jsonPath("$.coefficient").value(sameNumber(DEFAULT_COEFFICIENT)))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getCoursByIdFiltering() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        Long id = cours.getId();

        defaultCoursFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCoursFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCoursFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule equals to
        defaultCoursFiltering("intitule.equals=" + DEFAULT_INTITULE, "intitule.equals=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule in
        defaultCoursFiltering("intitule.in=" + DEFAULT_INTITULE + "," + UPDATED_INTITULE, "intitule.in=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule is not null
        defaultCoursFiltering("intitule.specified=true", "intitule.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByIntituleContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule contains
        defaultCoursFiltering("intitule.contains=" + DEFAULT_INTITULE, "intitule.contains=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByIntituleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where intitule does not contain
        defaultCoursFiltering("intitule.doesNotContain=" + UPDATED_INTITULE, "intitule.doesNotContain=" + DEFAULT_INTITULE);
    }

    @Test
    @Transactional
    void getAllCoursByLibelleLongIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleLong equals to
        defaultCoursFiltering("libelleLong.equals=" + DEFAULT_LIBELLE_LONG, "libelleLong.equals=" + UPDATED_LIBELLE_LONG);
    }

    @Test
    @Transactional
    void getAllCoursByLibelleLongIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleLong in
        defaultCoursFiltering(
            "libelleLong.in=" + DEFAULT_LIBELLE_LONG + "," + UPDATED_LIBELLE_LONG,
            "libelleLong.in=" + UPDATED_LIBELLE_LONG
        );
    }

    @Test
    @Transactional
    void getAllCoursByLibelleLongIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleLong is not null
        defaultCoursFiltering("libelleLong.specified=true", "libelleLong.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByLibelleLongContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleLong contains
        defaultCoursFiltering("libelleLong.contains=" + DEFAULT_LIBELLE_LONG, "libelleLong.contains=" + UPDATED_LIBELLE_LONG);
    }

    @Test
    @Transactional
    void getAllCoursByLibelleLongNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleLong does not contain
        defaultCoursFiltering("libelleLong.doesNotContain=" + UPDATED_LIBELLE_LONG, "libelleLong.doesNotContain=" + DEFAULT_LIBELLE_LONG);
    }

    @Test
    @Transactional
    void getAllCoursByLibelleCourtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleCourt equals to
        defaultCoursFiltering("libelleCourt.equals=" + DEFAULT_LIBELLE_COURT, "libelleCourt.equals=" + UPDATED_LIBELLE_COURT);
    }

    @Test
    @Transactional
    void getAllCoursByLibelleCourtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleCourt in
        defaultCoursFiltering(
            "libelleCourt.in=" + DEFAULT_LIBELLE_COURT + "," + UPDATED_LIBELLE_COURT,
            "libelleCourt.in=" + UPDATED_LIBELLE_COURT
        );
    }

    @Test
    @Transactional
    void getAllCoursByLibelleCourtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleCourt is not null
        defaultCoursFiltering("libelleCourt.specified=true", "libelleCourt.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByLibelleCourtContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleCourt contains
        defaultCoursFiltering("libelleCourt.contains=" + DEFAULT_LIBELLE_COURT, "libelleCourt.contains=" + UPDATED_LIBELLE_COURT);
    }

    @Test
    @Transactional
    void getAllCoursByLibelleCourtNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where libelleCourt does not contain
        defaultCoursFiltering(
            "libelleCourt.doesNotContain=" + UPDATED_LIBELLE_COURT,
            "libelleCourt.doesNotContain=" + DEFAULT_LIBELLE_COURT
        );
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage equals to
        defaultCoursFiltering("ordreAffichage.equals=" + DEFAULT_ORDRE_AFFICHAGE, "ordreAffichage.equals=" + UPDATED_ORDRE_AFFICHAGE);
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage in
        defaultCoursFiltering(
            "ordreAffichage.in=" + DEFAULT_ORDRE_AFFICHAGE + "," + UPDATED_ORDRE_AFFICHAGE,
            "ordreAffichage.in=" + UPDATED_ORDRE_AFFICHAGE
        );
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage is not null
        defaultCoursFiltering("ordreAffichage.specified=true", "ordreAffichage.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage is greater than or equal to
        defaultCoursFiltering(
            "ordreAffichage.greaterThanOrEqual=" + DEFAULT_ORDRE_AFFICHAGE,
            "ordreAffichage.greaterThanOrEqual=" + UPDATED_ORDRE_AFFICHAGE
        );
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage is less than or equal to
        defaultCoursFiltering(
            "ordreAffichage.lessThanOrEqual=" + DEFAULT_ORDRE_AFFICHAGE,
            "ordreAffichage.lessThanOrEqual=" + SMALLER_ORDRE_AFFICHAGE
        );
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage is less than
        defaultCoursFiltering("ordreAffichage.lessThan=" + UPDATED_ORDRE_AFFICHAGE, "ordreAffichage.lessThan=" + DEFAULT_ORDRE_AFFICHAGE);
    }

    @Test
    @Transactional
    void getAllCoursByOrdreAffichageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where ordreAffichage is greater than
        defaultCoursFiltering(
            "ordreAffichage.greaterThan=" + SMALLER_ORDRE_AFFICHAGE,
            "ordreAffichage.greaterThan=" + DEFAULT_ORDRE_AFFICHAGE
        );
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes equals to
        defaultCoursFiltering("nbPeriodes.equals=" + DEFAULT_NB_PERIODES, "nbPeriodes.equals=" + UPDATED_NB_PERIODES);
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes in
        defaultCoursFiltering("nbPeriodes.in=" + DEFAULT_NB_PERIODES + "," + UPDATED_NB_PERIODES, "nbPeriodes.in=" + UPDATED_NB_PERIODES);
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes is not null
        defaultCoursFiltering("nbPeriodes.specified=true", "nbPeriodes.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes is greater than or equal to
        defaultCoursFiltering(
            "nbPeriodes.greaterThanOrEqual=" + DEFAULT_NB_PERIODES,
            "nbPeriodes.greaterThanOrEqual=" + UPDATED_NB_PERIODES
        );
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes is less than or equal to
        defaultCoursFiltering("nbPeriodes.lessThanOrEqual=" + DEFAULT_NB_PERIODES, "nbPeriodes.lessThanOrEqual=" + SMALLER_NB_PERIODES);
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes is less than
        defaultCoursFiltering("nbPeriodes.lessThan=" + UPDATED_NB_PERIODES, "nbPeriodes.lessThan=" + DEFAULT_NB_PERIODES);
    }

    @Test
    @Transactional
    void getAllCoursByNbPeriodesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where nbPeriodes is greater than
        defaultCoursFiltering("nbPeriodes.greaterThan=" + SMALLER_NB_PERIODES, "nbPeriodes.greaterThan=" + DEFAULT_NB_PERIODES);
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient equals to
        defaultCoursFiltering("coefficient.equals=" + DEFAULT_COEFFICIENT, "coefficient.equals=" + UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient in
        defaultCoursFiltering("coefficient.in=" + DEFAULT_COEFFICIENT + "," + UPDATED_COEFFICIENT, "coefficient.in=" + UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient is not null
        defaultCoursFiltering("coefficient.specified=true", "coefficient.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient is greater than or equal to
        defaultCoursFiltering(
            "coefficient.greaterThanOrEqual=" + DEFAULT_COEFFICIENT,
            "coefficient.greaterThanOrEqual=" + UPDATED_COEFFICIENT
        );
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient is less than or equal to
        defaultCoursFiltering("coefficient.lessThanOrEqual=" + DEFAULT_COEFFICIENT, "coefficient.lessThanOrEqual=" + SMALLER_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient is less than
        defaultCoursFiltering("coefficient.lessThan=" + UPDATED_COEFFICIENT, "coefficient.lessThan=" + DEFAULT_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllCoursByCoefficientIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where coefficient is greater than
        defaultCoursFiltering("coefficient.greaterThan=" + SMALLER_COEFFICIENT, "coefficient.greaterThan=" + DEFAULT_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut equals to
        defaultCoursFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut in
        defaultCoursFiltering("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT, "dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut is not null
        defaultCoursFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut is greater than or equal to
        defaultCoursFiltering("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut is less than or equal to
        defaultCoursFiltering("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut is less than
        defaultCoursFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCoursByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateDebut is greater than
        defaultCoursFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin equals to
        defaultCoursFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin in
        defaultCoursFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin is not null
        defaultCoursFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin is greater than or equal to
        defaultCoursFiltering("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin is less than or equal to
        defaultCoursFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin is less than
        defaultCoursFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCoursByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where dateFin is greater than
        defaultCoursFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllCoursByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where commentaire equals to
        defaultCoursFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCoursByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where commentaire in
        defaultCoursFiltering("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE, "commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCoursByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where commentaire is not null
        defaultCoursFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where commentaire contains
        defaultCoursFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCoursByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where commentaire does not contain
        defaultCoursFiltering("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE, "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCoursByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where actif equals to
        defaultCoursFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllCoursByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where actif in
        defaultCoursFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllCoursByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        // Get all the coursList where actif is not null
        defaultCoursFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultCoursFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCoursShouldBeFound(shouldBeFound);
        defaultCoursShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCoursShouldBeFound(String filter) throws Exception {
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cours.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].ordreAffichage").value(hasItem(DEFAULT_ORDRE_AFFICHAGE)))
            .andExpect(jsonPath("$.[*].nbPeriodes").value(hasItem(DEFAULT_NB_PERIODES)))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(sameNumber(DEFAULT_COEFFICIENT))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCoursShouldNotBeFound(String filter) throws Exception {
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCoursMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCours() throws Exception {
        // Get the cours
        restCoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours
        Cours updatedCours = coursRepository.findById(cours.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCours are not directly saved in db
        em.detach(updatedCours);
        updatedCours
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .nbPeriodes(UPDATED_NB_PERIODES)
            .coefficient(UPDATED_COEFFICIENT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
        CoursDTO coursDTO = coursMapper.toDto(updatedCours);

        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCoursToMatchAllProperties(updatedCours);
    }

    @Test
    @Transactional
    void putNonExistingCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coursDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoursWithPatch() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours using partial update
        Cours partialUpdatedCours = new Cours();
        partialUpdatedCours.setId(cours.getId());

        partialUpdatedCours
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .dateFin(UPDATED_DATE_FIN);

        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCours.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCours))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoursUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCours, cours), getPersistedCours(cours));
    }

    @Test
    @Transactional
    void fullUpdateCoursWithPatch() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cours using partial update
        Cours partialUpdatedCours = new Cours();
        partialUpdatedCours.setId(cours.getId());

        partialUpdatedCours
            .intitule(UPDATED_INTITULE)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .ordreAffichage(UPDATED_ORDRE_AFFICHAGE)
            .nbPeriodes(UPDATED_NB_PERIODES)
            .coefficient(UPDATED_COEFFICIENT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);

        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCours.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCours))
            )
            .andExpect(status().isOk());

        // Validate the Cours in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCoursUpdatableFieldsEquals(partialUpdatedCours, getPersistedCours(partialUpdatedCours));
    }

    @Test
    @Transactional
    void patchNonExistingCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coursDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(coursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCours() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cours.setId(longCount.incrementAndGet());

        // Create the Cours
        CoursDTO coursDTO = coursMapper.toDto(cours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoursMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(coursDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cours in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCours() throws Exception {
        // Initialize the database
        insertedCours = coursRepository.saveAndFlush(cours);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cours
        restCoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, cours.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return coursRepository.count();
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

    protected Cours getPersistedCours(Cours cours) {
        return coursRepository.findById(cours.getId()).orElseThrow();
    }

    protected void assertPersistedCoursToMatchAllProperties(Cours expectedCours) {
        assertCoursAllPropertiesEquals(expectedCours, getPersistedCours(expectedCours));
    }

    protected void assertPersistedCoursToMatchUpdatableProperties(Cours expectedCours) {
        assertCoursAllUpdatablePropertiesEquals(expectedCours, getPersistedCours(expectedCours));
    }
}
