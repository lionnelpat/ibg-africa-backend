package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvaluationPrevueAsserts.*;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.Cours;
import org.forbidec.domain.Cycle;
import org.forbidec.domain.Enseignant;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.Matiere;
import org.forbidec.domain.SousMatiere;
import org.forbidec.domain.TypeTache;
import org.forbidec.repository.EvaluationPrevueRepository;
import org.forbidec.service.EvaluationPrevueService;
import org.forbidec.service.dto.EvaluationPrevueDTO;
import org.forbidec.service.mapper.EvaluationPrevueMapper;
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
 * Integration tests for the {@link EvaluationPrevueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvaluationPrevueResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_IMPRESSION = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_IMPRESSION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_COEFFICIENT = new BigDecimal(0);
    private static final BigDecimal UPDATED_COEFFICIENT = new BigDecimal(1);
    private static final BigDecimal SMALLER_COEFFICIENT = new BigDecimal(0 - 1);

    private static final Boolean DEFAULT_COMPTE_DANS_MOYENNE = false;
    private static final Boolean UPDATED_COMPTE_DANS_MOYENNE = true;

    private static final BigDecimal DEFAULT_NOTE_MAXIMALE = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOTE_MAXIMALE = new BigDecimal(2);
    private static final BigDecimal SMALLER_NOTE_MAXIMALE = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/evaluation-prevues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EvaluationPrevueRepository evaluationPrevueRepository;

    @Mock
    private EvaluationPrevueRepository evaluationPrevueRepositoryMock;

    @Autowired
    private EvaluationPrevueMapper evaluationPrevueMapper;

    @Mock
    private EvaluationPrevueService evaluationPrevueServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluationPrevueMockMvc;

    private EvaluationPrevue evaluationPrevue;

    private EvaluationPrevue insertedEvaluationPrevue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluationPrevue createEntity(EntityManager em) {
        EvaluationPrevue evaluationPrevue = new EvaluationPrevue()
            .intitule(DEFAULT_INTITULE)
            .libelleImpression(DEFAULT_LIBELLE_IMPRESSION)
            .coefficient(DEFAULT_COEFFICIENT)
            .compteDansMoyenne(DEFAULT_COMPTE_DANS_MOYENNE)
            .noteMaximale(DEFAULT_NOTE_MAXIMALE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .commentaire(DEFAULT_COMMENTAIRE);
        // Add required entity
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            cycle = CycleResourceIT.createEntity(em);
            em.persist(cycle);
            em.flush();
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        evaluationPrevue.setCycle(cycle);
        // Add required entity
        Enseignant enseignant;
        if (TestUtil.findAll(em, Enseignant.class).isEmpty()) {
            enseignant = EnseignantResourceIT.createEntity();
            em.persist(enseignant);
            em.flush();
        } else {
            enseignant = TestUtil.findAll(em, Enseignant.class).get(0);
        }
        evaluationPrevue.setEnseignant(enseignant);
        // Add required entity
        Matiere matiere;
        if (TestUtil.findAll(em, Matiere.class).isEmpty()) {
            matiere = MatiereResourceIT.createEntity();
            em.persist(matiere);
            em.flush();
        } else {
            matiere = TestUtil.findAll(em, Matiere.class).get(0);
        }
        evaluationPrevue.setMatiere(matiere);
        // Add required entity
        SousMatiere sousMatiere;
        if (TestUtil.findAll(em, SousMatiere.class).isEmpty()) {
            sousMatiere = SousMatiereResourceIT.createEntity();
            em.persist(sousMatiere);
            em.flush();
        } else {
            sousMatiere = TestUtil.findAll(em, SousMatiere.class).get(0);
        }
        evaluationPrevue.setSousMatiere(sousMatiere);
        // Add required entity
        Cours cours;
        if (TestUtil.findAll(em, Cours.class).isEmpty()) {
            cours = CoursResourceIT.createEntity();
            em.persist(cours);
            em.flush();
        } else {
            cours = TestUtil.findAll(em, Cours.class).get(0);
        }
        evaluationPrevue.setCours(cours);
        // Add required entity
        TypeTache typeTache;
        if (TestUtil.findAll(em, TypeTache.class).isEmpty()) {
            typeTache = TypeTacheResourceIT.createEntity();
            em.persist(typeTache);
            em.flush();
        } else {
            typeTache = TestUtil.findAll(em, TypeTache.class).get(0);
        }
        evaluationPrevue.setTypeTache(typeTache);
        return evaluationPrevue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluationPrevue createUpdatedEntity(EntityManager em) {
        EvaluationPrevue updatedEvaluationPrevue = new EvaluationPrevue()
            .intitule(UPDATED_INTITULE)
            .libelleImpression(UPDATED_LIBELLE_IMPRESSION)
            .coefficient(UPDATED_COEFFICIENT)
            .compteDansMoyenne(UPDATED_COMPTE_DANS_MOYENNE)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);
        // Add required entity
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            cycle = CycleResourceIT.createUpdatedEntity(em);
            em.persist(cycle);
            em.flush();
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        updatedEvaluationPrevue.setCycle(cycle);
        // Add required entity
        Enseignant enseignant;
        if (TestUtil.findAll(em, Enseignant.class).isEmpty()) {
            enseignant = EnseignantResourceIT.createUpdatedEntity();
            em.persist(enseignant);
            em.flush();
        } else {
            enseignant = TestUtil.findAll(em, Enseignant.class).get(0);
        }
        updatedEvaluationPrevue.setEnseignant(enseignant);
        // Add required entity
        Matiere matiere;
        if (TestUtil.findAll(em, Matiere.class).isEmpty()) {
            matiere = MatiereResourceIT.createUpdatedEntity();
            em.persist(matiere);
            em.flush();
        } else {
            matiere = TestUtil.findAll(em, Matiere.class).get(0);
        }
        updatedEvaluationPrevue.setMatiere(matiere);
        // Add required entity
        SousMatiere sousMatiere;
        if (TestUtil.findAll(em, SousMatiere.class).isEmpty()) {
            sousMatiere = SousMatiereResourceIT.createUpdatedEntity();
            em.persist(sousMatiere);
            em.flush();
        } else {
            sousMatiere = TestUtil.findAll(em, SousMatiere.class).get(0);
        }
        updatedEvaluationPrevue.setSousMatiere(sousMatiere);
        // Add required entity
        Cours cours;
        if (TestUtil.findAll(em, Cours.class).isEmpty()) {
            cours = CoursResourceIT.createUpdatedEntity();
            em.persist(cours);
            em.flush();
        } else {
            cours = TestUtil.findAll(em, Cours.class).get(0);
        }
        updatedEvaluationPrevue.setCours(cours);
        // Add required entity
        TypeTache typeTache;
        if (TestUtil.findAll(em, TypeTache.class).isEmpty()) {
            typeTache = TypeTacheResourceIT.createUpdatedEntity();
            em.persist(typeTache);
            em.flush();
        } else {
            typeTache = TestUtil.findAll(em, TypeTache.class).get(0);
        }
        updatedEvaluationPrevue.setTypeTache(typeTache);
        return updatedEvaluationPrevue;
    }

    @BeforeEach
    void initTest() {
        evaluationPrevue = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEvaluationPrevue != null) {
            evaluationPrevueRepository.delete(insertedEvaluationPrevue);
            insertedEvaluationPrevue = null;
        }
    }

    @Test
    @Transactional
    void createEvaluationPrevue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);
        var returnedEvaluationPrevueDTO = om.readValue(
            restEvaluationPrevueMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(evaluationPrevueDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EvaluationPrevueDTO.class
        );

        // Validate the EvaluationPrevue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvaluationPrevue = evaluationPrevueMapper.toEntity(returnedEvaluationPrevueDTO);
        assertEvaluationPrevueUpdatableFieldsEquals(returnedEvaluationPrevue, getPersistedEvaluationPrevue(returnedEvaluationPrevue));

        insertedEvaluationPrevue = returnedEvaluationPrevue;
    }

    @Test
    @Transactional
    void createEvaluationPrevueWithExistingId() throws Exception {
        // Create the EvaluationPrevue with an existing ID
        evaluationPrevue.setId(1L);
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationPrevueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIntituleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationPrevue.setIntitule(null);

        // Create the EvaluationPrevue, which fails.
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        restEvaluationPrevueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLibelleImpressionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationPrevue.setLibelleImpression(null);

        // Create the EvaluationPrevue, which fails.
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        restEvaluationPrevueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCoefficientIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationPrevue.setCoefficient(null);

        // Create the EvaluationPrevue, which fails.
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        restEvaluationPrevueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompteDansMoyenneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationPrevue.setCompteDansMoyenne(null);

        // Create the EvaluationPrevue, which fails.
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        restEvaluationPrevueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNoteMaximaleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationPrevue.setNoteMaximale(null);

        // Create the EvaluationPrevue, which fails.
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        restEvaluationPrevueMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevues() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList
        restEvaluationPrevueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationPrevue.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].libelleImpression").value(hasItem(DEFAULT_LIBELLE_IMPRESSION)))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(sameNumber(DEFAULT_COEFFICIENT))))
            .andExpect(jsonPath("$.[*].compteDansMoyenne").value(hasItem(DEFAULT_COMPTE_DANS_MOYENNE)))
            .andExpect(jsonPath("$.[*].noteMaximale").value(hasItem(sameNumber(DEFAULT_NOTE_MAXIMALE))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluationPrevuesWithEagerRelationshipsIsEnabled() throws Exception {
        when(evaluationPrevueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluationPrevueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evaluationPrevueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluationPrevuesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evaluationPrevueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluationPrevueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evaluationPrevueRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvaluationPrevue() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get the evaluationPrevue
        restEvaluationPrevueMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluationPrevue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluationPrevue.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.libelleImpression").value(DEFAULT_LIBELLE_IMPRESSION))
            .andExpect(jsonPath("$.coefficient").value(sameNumber(DEFAULT_COEFFICIENT)))
            .andExpect(jsonPath("$.compteDansMoyenne").value(DEFAULT_COMPTE_DANS_MOYENNE))
            .andExpect(jsonPath("$.noteMaximale").value(sameNumber(DEFAULT_NOTE_MAXIMALE)))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getEvaluationPrevuesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        Long id = evaluationPrevue.getId();

        defaultEvaluationPrevueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEvaluationPrevueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEvaluationPrevueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByIntituleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where intitule equals to
        defaultEvaluationPrevueFiltering("intitule.equals=" + DEFAULT_INTITULE, "intitule.equals=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByIntituleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where intitule in
        defaultEvaluationPrevueFiltering("intitule.in=" + DEFAULT_INTITULE + "," + UPDATED_INTITULE, "intitule.in=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByIntituleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where intitule is not null
        defaultEvaluationPrevueFiltering("intitule.specified=true", "intitule.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByIntituleContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where intitule contains
        defaultEvaluationPrevueFiltering("intitule.contains=" + DEFAULT_INTITULE, "intitule.contains=" + UPDATED_INTITULE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByIntituleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where intitule does not contain
        defaultEvaluationPrevueFiltering("intitule.doesNotContain=" + UPDATED_INTITULE, "intitule.doesNotContain=" + DEFAULT_INTITULE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByLibelleImpressionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where libelleImpression equals to
        defaultEvaluationPrevueFiltering(
            "libelleImpression.equals=" + DEFAULT_LIBELLE_IMPRESSION,
            "libelleImpression.equals=" + UPDATED_LIBELLE_IMPRESSION
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByLibelleImpressionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where libelleImpression in
        defaultEvaluationPrevueFiltering(
            "libelleImpression.in=" + DEFAULT_LIBELLE_IMPRESSION + "," + UPDATED_LIBELLE_IMPRESSION,
            "libelleImpression.in=" + UPDATED_LIBELLE_IMPRESSION
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByLibelleImpressionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where libelleImpression is not null
        defaultEvaluationPrevueFiltering("libelleImpression.specified=true", "libelleImpression.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByLibelleImpressionContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where libelleImpression contains
        defaultEvaluationPrevueFiltering(
            "libelleImpression.contains=" + DEFAULT_LIBELLE_IMPRESSION,
            "libelleImpression.contains=" + UPDATED_LIBELLE_IMPRESSION
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByLibelleImpressionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where libelleImpression does not contain
        defaultEvaluationPrevueFiltering(
            "libelleImpression.doesNotContain=" + UPDATED_LIBELLE_IMPRESSION,
            "libelleImpression.doesNotContain=" + DEFAULT_LIBELLE_IMPRESSION
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient equals to
        defaultEvaluationPrevueFiltering("coefficient.equals=" + DEFAULT_COEFFICIENT, "coefficient.equals=" + UPDATED_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient in
        defaultEvaluationPrevueFiltering(
            "coefficient.in=" + DEFAULT_COEFFICIENT + "," + UPDATED_COEFFICIENT,
            "coefficient.in=" + UPDATED_COEFFICIENT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient is not null
        defaultEvaluationPrevueFiltering("coefficient.specified=true", "coefficient.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient is greater than or equal to
        defaultEvaluationPrevueFiltering(
            "coefficient.greaterThanOrEqual=" + DEFAULT_COEFFICIENT,
            "coefficient.greaterThanOrEqual=" + UPDATED_COEFFICIENT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient is less than or equal to
        defaultEvaluationPrevueFiltering(
            "coefficient.lessThanOrEqual=" + DEFAULT_COEFFICIENT,
            "coefficient.lessThanOrEqual=" + SMALLER_COEFFICIENT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient is less than
        defaultEvaluationPrevueFiltering("coefficient.lessThan=" + UPDATED_COEFFICIENT, "coefficient.lessThan=" + DEFAULT_COEFFICIENT);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoefficientIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where coefficient is greater than
        defaultEvaluationPrevueFiltering(
            "coefficient.greaterThan=" + SMALLER_COEFFICIENT,
            "coefficient.greaterThan=" + DEFAULT_COEFFICIENT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCompteDansMoyenneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where compteDansMoyenne equals to
        defaultEvaluationPrevueFiltering(
            "compteDansMoyenne.equals=" + DEFAULT_COMPTE_DANS_MOYENNE,
            "compteDansMoyenne.equals=" + UPDATED_COMPTE_DANS_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCompteDansMoyenneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where compteDansMoyenne in
        defaultEvaluationPrevueFiltering(
            "compteDansMoyenne.in=" + DEFAULT_COMPTE_DANS_MOYENNE + "," + UPDATED_COMPTE_DANS_MOYENNE,
            "compteDansMoyenne.in=" + UPDATED_COMPTE_DANS_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCompteDansMoyenneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where compteDansMoyenne is not null
        defaultEvaluationPrevueFiltering("compteDansMoyenne.specified=true", "compteDansMoyenne.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale equals to
        defaultEvaluationPrevueFiltering("noteMaximale.equals=" + DEFAULT_NOTE_MAXIMALE, "noteMaximale.equals=" + UPDATED_NOTE_MAXIMALE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale in
        defaultEvaluationPrevueFiltering(
            "noteMaximale.in=" + DEFAULT_NOTE_MAXIMALE + "," + UPDATED_NOTE_MAXIMALE,
            "noteMaximale.in=" + UPDATED_NOTE_MAXIMALE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale is not null
        defaultEvaluationPrevueFiltering("noteMaximale.specified=true", "noteMaximale.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale is greater than or equal to
        defaultEvaluationPrevueFiltering(
            "noteMaximale.greaterThanOrEqual=" + DEFAULT_NOTE_MAXIMALE,
            "noteMaximale.greaterThanOrEqual=" + UPDATED_NOTE_MAXIMALE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale is less than or equal to
        defaultEvaluationPrevueFiltering(
            "noteMaximale.lessThanOrEqual=" + DEFAULT_NOTE_MAXIMALE,
            "noteMaximale.lessThanOrEqual=" + SMALLER_NOTE_MAXIMALE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale is less than
        defaultEvaluationPrevueFiltering(
            "noteMaximale.lessThan=" + UPDATED_NOTE_MAXIMALE,
            "noteMaximale.lessThan=" + DEFAULT_NOTE_MAXIMALE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByNoteMaximaleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where noteMaximale is greater than
        defaultEvaluationPrevueFiltering(
            "noteMaximale.greaterThan=" + SMALLER_NOTE_MAXIMALE,
            "noteMaximale.greaterThan=" + DEFAULT_NOTE_MAXIMALE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut equals to
        defaultEvaluationPrevueFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut in
        defaultEvaluationPrevueFiltering(
            "dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT,
            "dateDebut.in=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut is not null
        defaultEvaluationPrevueFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut is greater than or equal to
        defaultEvaluationPrevueFiltering(
            "dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT,
            "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut is less than or equal to
        defaultEvaluationPrevueFiltering(
            "dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT,
            "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut is less than
        defaultEvaluationPrevueFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateDebut is greater than
        defaultEvaluationPrevueFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin equals to
        defaultEvaluationPrevueFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin in
        defaultEvaluationPrevueFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin is not null
        defaultEvaluationPrevueFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin is greater than or equal to
        defaultEvaluationPrevueFiltering(
            "dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN,
            "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin is less than or equal to
        defaultEvaluationPrevueFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin is less than
        defaultEvaluationPrevueFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where dateFin is greater than
        defaultEvaluationPrevueFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where commentaire equals to
        defaultEvaluationPrevueFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where commentaire in
        defaultEvaluationPrevueFiltering(
            "commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE,
            "commentaire.in=" + UPDATED_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where commentaire is not null
        defaultEvaluationPrevueFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where commentaire contains
        defaultEvaluationPrevueFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        // Get all the evaluationPrevueList where commentaire does not contain
        defaultEvaluationPrevueFiltering(
            "commentaire.doesNotContain=" + UPDATED_COMMENTAIRE,
            "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCycleIsEqualToSomething() throws Exception {
        Cycle cycle;
        if (TestUtil.findAll(em, Cycle.class).isEmpty()) {
            evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
            cycle = CycleResourceIT.createEntity(em);
        } else {
            cycle = TestUtil.findAll(em, Cycle.class).get(0);
        }
        em.persist(cycle);
        em.flush();
        evaluationPrevue.setCycle(cycle);
        evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
        Long cycleId = cycle.getId();
        // Get all the evaluationPrevueList where cycle equals to cycleId
        defaultEvaluationPrevueShouldBeFound("cycleId.equals=" + cycleId);

        // Get all the evaluationPrevueList where cycle equals to (cycleId + 1)
        defaultEvaluationPrevueShouldNotBeFound("cycleId.equals=" + (cycleId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByEnseignantIsEqualToSomething() throws Exception {
        Enseignant enseignant;
        if (TestUtil.findAll(em, Enseignant.class).isEmpty()) {
            evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
            enseignant = EnseignantResourceIT.createEntity();
        } else {
            enseignant = TestUtil.findAll(em, Enseignant.class).get(0);
        }
        em.persist(enseignant);
        em.flush();
        evaluationPrevue.setEnseignant(enseignant);
        evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
        Long enseignantId = enseignant.getId();
        // Get all the evaluationPrevueList where enseignant equals to enseignantId
        defaultEvaluationPrevueShouldBeFound("enseignantId.equals=" + enseignantId);

        // Get all the evaluationPrevueList where enseignant equals to (enseignantId + 1)
        defaultEvaluationPrevueShouldNotBeFound("enseignantId.equals=" + (enseignantId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByMatiereIsEqualToSomething() throws Exception {
        Matiere matiere;
        if (TestUtil.findAll(em, Matiere.class).isEmpty()) {
            evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
            matiere = MatiereResourceIT.createEntity();
        } else {
            matiere = TestUtil.findAll(em, Matiere.class).get(0);
        }
        em.persist(matiere);
        em.flush();
        evaluationPrevue.setMatiere(matiere);
        evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
        Long matiereId = matiere.getId();
        // Get all the evaluationPrevueList where matiere equals to matiereId
        defaultEvaluationPrevueShouldBeFound("matiereId.equals=" + matiereId);

        // Get all the evaluationPrevueList where matiere equals to (matiereId + 1)
        defaultEvaluationPrevueShouldNotBeFound("matiereId.equals=" + (matiereId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesBySousMatiereIsEqualToSomething() throws Exception {
        SousMatiere sousMatiere;
        if (TestUtil.findAll(em, SousMatiere.class).isEmpty()) {
            evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
            sousMatiere = SousMatiereResourceIT.createEntity();
        } else {
            sousMatiere = TestUtil.findAll(em, SousMatiere.class).get(0);
        }
        em.persist(sousMatiere);
        em.flush();
        evaluationPrevue.setSousMatiere(sousMatiere);
        evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
        Long sousMatiereId = sousMatiere.getId();
        // Get all the evaluationPrevueList where sousMatiere equals to sousMatiereId
        defaultEvaluationPrevueShouldBeFound("sousMatiereId.equals=" + sousMatiereId);

        // Get all the evaluationPrevueList where sousMatiere equals to (sousMatiereId + 1)
        defaultEvaluationPrevueShouldNotBeFound("sousMatiereId.equals=" + (sousMatiereId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByCoursIsEqualToSomething() throws Exception {
        Cours cours;
        if (TestUtil.findAll(em, Cours.class).isEmpty()) {
            evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
            cours = CoursResourceIT.createEntity();
        } else {
            cours = TestUtil.findAll(em, Cours.class).get(0);
        }
        em.persist(cours);
        em.flush();
        evaluationPrevue.setCours(cours);
        evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
        Long coursId = cours.getId();
        // Get all the evaluationPrevueList where cours equals to coursId
        defaultEvaluationPrevueShouldBeFound("coursId.equals=" + coursId);

        // Get all the evaluationPrevueList where cours equals to (coursId + 1)
        defaultEvaluationPrevueShouldNotBeFound("coursId.equals=" + (coursId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationPrevuesByTypeTacheIsEqualToSomething() throws Exception {
        TypeTache typeTache;
        if (TestUtil.findAll(em, TypeTache.class).isEmpty()) {
            evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
            typeTache = TypeTacheResourceIT.createEntity();
        } else {
            typeTache = TestUtil.findAll(em, TypeTache.class).get(0);
        }
        em.persist(typeTache);
        em.flush();
        evaluationPrevue.setTypeTache(typeTache);
        evaluationPrevueRepository.saveAndFlush(evaluationPrevue);
        Long typeTacheId = typeTache.getId();
        // Get all the evaluationPrevueList where typeTache equals to typeTacheId
        defaultEvaluationPrevueShouldBeFound("typeTacheId.equals=" + typeTacheId);

        // Get all the evaluationPrevueList where typeTache equals to (typeTacheId + 1)
        defaultEvaluationPrevueShouldNotBeFound("typeTacheId.equals=" + (typeTacheId + 1));
    }

    private void defaultEvaluationPrevueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEvaluationPrevueShouldBeFound(shouldBeFound);
        defaultEvaluationPrevueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvaluationPrevueShouldBeFound(String filter) throws Exception {
        restEvaluationPrevueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationPrevue.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].libelleImpression").value(hasItem(DEFAULT_LIBELLE_IMPRESSION)))
            .andExpect(jsonPath("$.[*].coefficient").value(hasItem(sameNumber(DEFAULT_COEFFICIENT))))
            .andExpect(jsonPath("$.[*].compteDansMoyenne").value(hasItem(DEFAULT_COMPTE_DANS_MOYENNE)))
            .andExpect(jsonPath("$.[*].noteMaximale").value(hasItem(sameNumber(DEFAULT_NOTE_MAXIMALE))))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restEvaluationPrevueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvaluationPrevueShouldNotBeFound(String filter) throws Exception {
        restEvaluationPrevueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvaluationPrevueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvaluationPrevue() throws Exception {
        // Get the evaluationPrevue
        restEvaluationPrevueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvaluationPrevue() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluationPrevue
        EvaluationPrevue updatedEvaluationPrevue = evaluationPrevueRepository.findById(evaluationPrevue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvaluationPrevue are not directly saved in db
        em.detach(updatedEvaluationPrevue);
        updatedEvaluationPrevue
            .intitule(UPDATED_INTITULE)
            .libelleImpression(UPDATED_LIBELLE_IMPRESSION)
            .coefficient(UPDATED_COEFFICIENT)
            .compteDansMoyenne(UPDATED_COMPTE_DANS_MOYENNE)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(updatedEvaluationPrevue);

        restEvaluationPrevueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationPrevueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEvaluationPrevueToMatchAllProperties(updatedEvaluationPrevue);
    }

    @Test
    @Transactional
    void putNonExistingEvaluationPrevue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationPrevue.setId(longCount.incrementAndGet());

        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationPrevueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationPrevueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluationPrevue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationPrevue.setId(longCount.incrementAndGet());

        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationPrevueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluationPrevue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationPrevue.setId(longCount.incrementAndGet());

        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationPrevueMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluationPrevueWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluationPrevue using partial update
        EvaluationPrevue partialUpdatedEvaluationPrevue = new EvaluationPrevue();
        partialUpdatedEvaluationPrevue.setId(evaluationPrevue.getId());

        partialUpdatedEvaluationPrevue
            .libelleImpression(UPDATED_LIBELLE_IMPRESSION)
            .coefficient(UPDATED_COEFFICIENT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);

        restEvaluationPrevueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluationPrevue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluationPrevue))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationPrevue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationPrevueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEvaluationPrevue, evaluationPrevue),
            getPersistedEvaluationPrevue(evaluationPrevue)
        );
    }

    @Test
    @Transactional
    void fullUpdateEvaluationPrevueWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluationPrevue using partial update
        EvaluationPrevue partialUpdatedEvaluationPrevue = new EvaluationPrevue();
        partialUpdatedEvaluationPrevue.setId(evaluationPrevue.getId());

        partialUpdatedEvaluationPrevue
            .intitule(UPDATED_INTITULE)
            .libelleImpression(UPDATED_LIBELLE_IMPRESSION)
            .coefficient(UPDATED_COEFFICIENT)
            .compteDansMoyenne(UPDATED_COMPTE_DANS_MOYENNE)
            .noteMaximale(UPDATED_NOTE_MAXIMALE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire(UPDATED_COMMENTAIRE);

        restEvaluationPrevueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluationPrevue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluationPrevue))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationPrevue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationPrevueUpdatableFieldsEquals(
            partialUpdatedEvaluationPrevue,
            getPersistedEvaluationPrevue(partialUpdatedEvaluationPrevue)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEvaluationPrevue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationPrevue.setId(longCount.incrementAndGet());

        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationPrevueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluationPrevueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluationPrevue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationPrevue.setId(longCount.incrementAndGet());

        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationPrevueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluationPrevue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationPrevue.setId(longCount.incrementAndGet());

        // Create the EvaluationPrevue
        EvaluationPrevueDTO evaluationPrevueDTO = evaluationPrevueMapper.toDto(evaluationPrevue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationPrevueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationPrevueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluationPrevue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluationPrevue() throws Exception {
        // Initialize the database
        insertedEvaluationPrevue = evaluationPrevueRepository.saveAndFlush(evaluationPrevue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evaluationPrevue
        restEvaluationPrevueMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluationPrevue.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return evaluationPrevueRepository.count();
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

    protected EvaluationPrevue getPersistedEvaluationPrevue(EvaluationPrevue evaluationPrevue) {
        return evaluationPrevueRepository.findById(evaluationPrevue.getId()).orElseThrow();
    }

    protected void assertPersistedEvaluationPrevueToMatchAllProperties(EvaluationPrevue expectedEvaluationPrevue) {
        assertEvaluationPrevueAllPropertiesEquals(expectedEvaluationPrevue, getPersistedEvaluationPrevue(expectedEvaluationPrevue));
    }

    protected void assertPersistedEvaluationPrevueToMatchUpdatableProperties(EvaluationPrevue expectedEvaluationPrevue) {
        assertEvaluationPrevueAllUpdatablePropertiesEquals(
            expectedEvaluationPrevue,
            getPersistedEvaluationPrevue(expectedEvaluationPrevue)
        );
    }
}
