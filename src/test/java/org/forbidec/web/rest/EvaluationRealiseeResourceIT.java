package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvaluationRealiseeAsserts.*;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.domain.enumeration.StatutNote;
import org.forbidec.repository.EvaluationRealiseeRepository;
import org.forbidec.service.EvaluationRealiseeService;
import org.forbidec.service.dto.EvaluationRealiseeDTO;
import org.forbidec.service.mapper.EvaluationRealiseeMapper;
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
 * Integration tests for the {@link EvaluationRealiseeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvaluationRealiseeResourceIT {

    private static final BigDecimal DEFAULT_NOTE = new BigDecimal(0);
    private static final BigDecimal UPDATED_NOTE = new BigDecimal(1);
    private static final BigDecimal SMALLER_NOTE = new BigDecimal(0 - 1);

    private static final StatutNote DEFAULT_STATUT = StatutNote.NON_SAISIE;
    private static final StatutNote UPDATED_STATUT = StatutNote.SAISIE;

    private static final Boolean DEFAULT_COMPTE_DANS_MOYENNE = false;
    private static final Boolean UPDATED_COMPTE_DANS_MOYENNE = true;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COMMENTAIRE_1 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_2 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE_3 = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_SAISIE_PAR = "AAAAAAAAAA";
    private static final String UPDATED_SAISIE_PAR = "BBBBBBBBBB";

    private static final Instant DEFAULT_SAISIE_LE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SAISIE_LE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VALIDEE_PAR = "AAAAAAAAAA";
    private static final String UPDATED_VALIDEE_PAR = "BBBBBBBBBB";

    private static final Instant DEFAULT_VALIDEE_LE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VALIDEE_LE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/evaluation-realisees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EvaluationRealiseeRepository evaluationRealiseeRepository;

    @Mock
    private EvaluationRealiseeRepository evaluationRealiseeRepositoryMock;

    @Autowired
    private EvaluationRealiseeMapper evaluationRealiseeMapper;

    @Mock
    private EvaluationRealiseeService evaluationRealiseeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluationRealiseeMockMvc;

    private EvaluationRealisee evaluationRealisee;

    private EvaluationRealisee insertedEvaluationRealisee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluationRealisee createEntity(EntityManager em) {
        EvaluationRealisee evaluationRealisee = new EvaluationRealisee()
            .note(DEFAULT_NOTE)
            .statut(DEFAULT_STATUT)
            .compteDansMoyenne(DEFAULT_COMPTE_DANS_MOYENNE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .commentaire1(DEFAULT_COMMENTAIRE_1)
            .commentaire2(DEFAULT_COMMENTAIRE_2)
            .commentaire3(DEFAULT_COMMENTAIRE_3)
            .saisiePar(DEFAULT_SAISIE_PAR)
            .saisieLe(DEFAULT_SAISIE_LE)
            .valideePar(DEFAULT_VALIDEE_PAR)
            .valideeLe(DEFAULT_VALIDEE_LE);
        // Add required entity
        EvaluationPrevue evaluationPrevue;
        if (TestUtil.findAll(em, EvaluationPrevue.class).isEmpty()) {
            evaluationPrevue = EvaluationPrevueResourceIT.createEntity(em);
            em.persist(evaluationPrevue);
            em.flush();
        } else {
            evaluationPrevue = TestUtil.findAll(em, EvaluationPrevue.class).get(0);
        }
        evaluationRealisee.setEvaluationPrevue(evaluationPrevue);
        // Add required entity
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            etudiant = EtudiantResourceIT.createEntity();
            em.persist(etudiant);
            em.flush();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        evaluationRealisee.setEtudiant(etudiant);
        return evaluationRealisee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluationRealisee createUpdatedEntity(EntityManager em) {
        EvaluationRealisee updatedEvaluationRealisee = new EvaluationRealisee()
            .note(UPDATED_NOTE)
            .statut(UPDATED_STATUT)
            .compteDansMoyenne(UPDATED_COMPTE_DANS_MOYENNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire1(UPDATED_COMMENTAIRE_1)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .saisiePar(UPDATED_SAISIE_PAR)
            .saisieLe(UPDATED_SAISIE_LE)
            .valideePar(UPDATED_VALIDEE_PAR)
            .valideeLe(UPDATED_VALIDEE_LE);
        // Add required entity
        EvaluationPrevue evaluationPrevue;
        if (TestUtil.findAll(em, EvaluationPrevue.class).isEmpty()) {
            evaluationPrevue = EvaluationPrevueResourceIT.createUpdatedEntity(em);
            em.persist(evaluationPrevue);
            em.flush();
        } else {
            evaluationPrevue = TestUtil.findAll(em, EvaluationPrevue.class).get(0);
        }
        updatedEvaluationRealisee.setEvaluationPrevue(evaluationPrevue);
        // Add required entity
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            etudiant = EtudiantResourceIT.createUpdatedEntity();
            em.persist(etudiant);
            em.flush();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        updatedEvaluationRealisee.setEtudiant(etudiant);
        return updatedEvaluationRealisee;
    }

    @BeforeEach
    void initTest() {
        evaluationRealisee = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEvaluationRealisee != null) {
            evaluationRealiseeRepository.delete(insertedEvaluationRealisee);
            insertedEvaluationRealisee = null;
        }
    }

    @Test
    @Transactional
    void createEvaluationRealisee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);
        var returnedEvaluationRealiseeDTO = om.readValue(
            restEvaluationRealiseeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(evaluationRealiseeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EvaluationRealiseeDTO.class
        );

        // Validate the EvaluationRealisee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvaluationRealisee = evaluationRealiseeMapper.toEntity(returnedEvaluationRealiseeDTO);
        assertEvaluationRealiseeUpdatableFieldsEquals(
            returnedEvaluationRealisee,
            getPersistedEvaluationRealisee(returnedEvaluationRealisee)
        );

        insertedEvaluationRealisee = returnedEvaluationRealisee;
    }

    @Test
    @Transactional
    void createEvaluationRealiseeWithExistingId() throws Exception {
        // Create the EvaluationRealisee with an existing ID
        evaluationRealisee.setId(1L);
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationRealiseeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationRealisee.setStatut(null);

        // Create the EvaluationRealisee, which fails.
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        restEvaluationRealiseeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompteDansMoyenneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evaluationRealisee.setCompteDansMoyenne(null);

        // Create the EvaluationRealisee, which fails.
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        restEvaluationRealiseeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvaluationRealisees() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList
        restEvaluationRealiseeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationRealisee.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(sameNumber(DEFAULT_NOTE))))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].compteDansMoyenne").value(hasItem(DEFAULT_COMPTE_DANS_MOYENNE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire1").value(hasItem(DEFAULT_COMMENTAIRE_1)))
            .andExpect(jsonPath("$.[*].commentaire2").value(hasItem(DEFAULT_COMMENTAIRE_2)))
            .andExpect(jsonPath("$.[*].commentaire3").value(hasItem(DEFAULT_COMMENTAIRE_3)))
            .andExpect(jsonPath("$.[*].saisiePar").value(hasItem(DEFAULT_SAISIE_PAR)))
            .andExpect(jsonPath("$.[*].saisieLe").value(hasItem(DEFAULT_SAISIE_LE.toString())))
            .andExpect(jsonPath("$.[*].valideePar").value(hasItem(DEFAULT_VALIDEE_PAR)))
            .andExpect(jsonPath("$.[*].valideeLe").value(hasItem(DEFAULT_VALIDEE_LE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluationRealiseesWithEagerRelationshipsIsEnabled() throws Exception {
        when(evaluationRealiseeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluationRealiseeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evaluationRealiseeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvaluationRealiseesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evaluationRealiseeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvaluationRealiseeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evaluationRealiseeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvaluationRealisee() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get the evaluationRealisee
        restEvaluationRealiseeMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluationRealisee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluationRealisee.getId().intValue()))
            .andExpect(jsonPath("$.note").value(sameNumber(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.compteDansMoyenne").value(DEFAULT_COMPTE_DANS_MOYENNE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.commentaire1").value(DEFAULT_COMMENTAIRE_1))
            .andExpect(jsonPath("$.commentaire2").value(DEFAULT_COMMENTAIRE_2))
            .andExpect(jsonPath("$.commentaire3").value(DEFAULT_COMMENTAIRE_3))
            .andExpect(jsonPath("$.saisiePar").value(DEFAULT_SAISIE_PAR))
            .andExpect(jsonPath("$.saisieLe").value(DEFAULT_SAISIE_LE.toString()))
            .andExpect(jsonPath("$.valideePar").value(DEFAULT_VALIDEE_PAR))
            .andExpect(jsonPath("$.valideeLe").value(DEFAULT_VALIDEE_LE.toString()));
    }

    @Test
    @Transactional
    void getEvaluationRealiseesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        Long id = evaluationRealisee.getId();

        defaultEvaluationRealiseeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEvaluationRealiseeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEvaluationRealiseeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note equals to
        defaultEvaluationRealiseeFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note in
        defaultEvaluationRealiseeFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note is not null
        defaultEvaluationRealiseeFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note is greater than or equal to
        defaultEvaluationRealiseeFiltering("note.greaterThanOrEqual=" + DEFAULT_NOTE, "note.greaterThanOrEqual=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note is less than or equal to
        defaultEvaluationRealiseeFiltering("note.lessThanOrEqual=" + DEFAULT_NOTE, "note.lessThanOrEqual=" + SMALLER_NOTE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note is less than
        defaultEvaluationRealiseeFiltering("note.lessThan=" + UPDATED_NOTE, "note.lessThan=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByNoteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where note is greater than
        defaultEvaluationRealiseeFiltering("note.greaterThan=" + SMALLER_NOTE, "note.greaterThan=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where statut equals to
        defaultEvaluationRealiseeFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where statut in
        defaultEvaluationRealiseeFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where statut is not null
        defaultEvaluationRealiseeFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCompteDansMoyenneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where compteDansMoyenne equals to
        defaultEvaluationRealiseeFiltering(
            "compteDansMoyenne.equals=" + DEFAULT_COMPTE_DANS_MOYENNE,
            "compteDansMoyenne.equals=" + UPDATED_COMPTE_DANS_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCompteDansMoyenneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where compteDansMoyenne in
        defaultEvaluationRealiseeFiltering(
            "compteDansMoyenne.in=" + DEFAULT_COMPTE_DANS_MOYENNE + "," + UPDATED_COMPTE_DANS_MOYENNE,
            "compteDansMoyenne.in=" + UPDATED_COMPTE_DANS_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCompteDansMoyenneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where compteDansMoyenne is not null
        defaultEvaluationRealiseeFiltering("compteDansMoyenne.specified=true", "compteDansMoyenne.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut equals to
        defaultEvaluationRealiseeFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut in
        defaultEvaluationRealiseeFiltering(
            "dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT,
            "dateDebut.in=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut is not null
        defaultEvaluationRealiseeFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut is greater than or equal to
        defaultEvaluationRealiseeFiltering(
            "dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT,
            "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut is less than or equal to
        defaultEvaluationRealiseeFiltering(
            "dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT,
            "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut is less than
        defaultEvaluationRealiseeFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateDebut is greater than
        defaultEvaluationRealiseeFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin equals to
        defaultEvaluationRealiseeFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin in
        defaultEvaluationRealiseeFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin is not null
        defaultEvaluationRealiseeFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin is greater than or equal to
        defaultEvaluationRealiseeFiltering(
            "dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN,
            "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin is less than or equal to
        defaultEvaluationRealiseeFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin is less than
        defaultEvaluationRealiseeFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where dateFin is greater than
        defaultEvaluationRealiseeFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire1 equals to
        defaultEvaluationRealiseeFiltering("commentaire1.equals=" + DEFAULT_COMMENTAIRE_1, "commentaire1.equals=" + UPDATED_COMMENTAIRE_1);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire1 in
        defaultEvaluationRealiseeFiltering(
            "commentaire1.in=" + DEFAULT_COMMENTAIRE_1 + "," + UPDATED_COMMENTAIRE_1,
            "commentaire1.in=" + UPDATED_COMMENTAIRE_1
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire1 is not null
        defaultEvaluationRealiseeFiltering("commentaire1.specified=true", "commentaire1.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire1ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire1 contains
        defaultEvaluationRealiseeFiltering(
            "commentaire1.contains=" + DEFAULT_COMMENTAIRE_1,
            "commentaire1.contains=" + UPDATED_COMMENTAIRE_1
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire1 does not contain
        defaultEvaluationRealiseeFiltering(
            "commentaire1.doesNotContain=" + UPDATED_COMMENTAIRE_1,
            "commentaire1.doesNotContain=" + DEFAULT_COMMENTAIRE_1
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire2 equals to
        defaultEvaluationRealiseeFiltering("commentaire2.equals=" + DEFAULT_COMMENTAIRE_2, "commentaire2.equals=" + UPDATED_COMMENTAIRE_2);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire2 in
        defaultEvaluationRealiseeFiltering(
            "commentaire2.in=" + DEFAULT_COMMENTAIRE_2 + "," + UPDATED_COMMENTAIRE_2,
            "commentaire2.in=" + UPDATED_COMMENTAIRE_2
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire2 is not null
        defaultEvaluationRealiseeFiltering("commentaire2.specified=true", "commentaire2.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire2ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire2 contains
        defaultEvaluationRealiseeFiltering(
            "commentaire2.contains=" + DEFAULT_COMMENTAIRE_2,
            "commentaire2.contains=" + UPDATED_COMMENTAIRE_2
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire2 does not contain
        defaultEvaluationRealiseeFiltering(
            "commentaire2.doesNotContain=" + UPDATED_COMMENTAIRE_2,
            "commentaire2.doesNotContain=" + DEFAULT_COMMENTAIRE_2
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire3 equals to
        defaultEvaluationRealiseeFiltering("commentaire3.equals=" + DEFAULT_COMMENTAIRE_3, "commentaire3.equals=" + UPDATED_COMMENTAIRE_3);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire3 in
        defaultEvaluationRealiseeFiltering(
            "commentaire3.in=" + DEFAULT_COMMENTAIRE_3 + "," + UPDATED_COMMENTAIRE_3,
            "commentaire3.in=" + UPDATED_COMMENTAIRE_3
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire3 is not null
        defaultEvaluationRealiseeFiltering("commentaire3.specified=true", "commentaire3.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire3ContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire3 contains
        defaultEvaluationRealiseeFiltering(
            "commentaire3.contains=" + DEFAULT_COMMENTAIRE_3,
            "commentaire3.contains=" + UPDATED_COMMENTAIRE_3
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByCommentaire3NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where commentaire3 does not contain
        defaultEvaluationRealiseeFiltering(
            "commentaire3.doesNotContain=" + UPDATED_COMMENTAIRE_3,
            "commentaire3.doesNotContain=" + DEFAULT_COMMENTAIRE_3
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieParIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisiePar equals to
        defaultEvaluationRealiseeFiltering("saisiePar.equals=" + DEFAULT_SAISIE_PAR, "saisiePar.equals=" + UPDATED_SAISIE_PAR);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieParIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisiePar in
        defaultEvaluationRealiseeFiltering(
            "saisiePar.in=" + DEFAULT_SAISIE_PAR + "," + UPDATED_SAISIE_PAR,
            "saisiePar.in=" + UPDATED_SAISIE_PAR
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieParIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisiePar is not null
        defaultEvaluationRealiseeFiltering("saisiePar.specified=true", "saisiePar.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieParContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisiePar contains
        defaultEvaluationRealiseeFiltering("saisiePar.contains=" + DEFAULT_SAISIE_PAR, "saisiePar.contains=" + UPDATED_SAISIE_PAR);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieParNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisiePar does not contain
        defaultEvaluationRealiseeFiltering(
            "saisiePar.doesNotContain=" + UPDATED_SAISIE_PAR,
            "saisiePar.doesNotContain=" + DEFAULT_SAISIE_PAR
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieLeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisieLe equals to
        defaultEvaluationRealiseeFiltering("saisieLe.equals=" + DEFAULT_SAISIE_LE, "saisieLe.equals=" + UPDATED_SAISIE_LE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieLeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisieLe in
        defaultEvaluationRealiseeFiltering(
            "saisieLe.in=" + DEFAULT_SAISIE_LE + "," + UPDATED_SAISIE_LE,
            "saisieLe.in=" + UPDATED_SAISIE_LE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesBySaisieLeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where saisieLe is not null
        defaultEvaluationRealiseeFiltering("saisieLe.specified=true", "saisieLe.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeParIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideePar equals to
        defaultEvaluationRealiseeFiltering("valideePar.equals=" + DEFAULT_VALIDEE_PAR, "valideePar.equals=" + UPDATED_VALIDEE_PAR);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeParIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideePar in
        defaultEvaluationRealiseeFiltering(
            "valideePar.in=" + DEFAULT_VALIDEE_PAR + "," + UPDATED_VALIDEE_PAR,
            "valideePar.in=" + UPDATED_VALIDEE_PAR
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeParIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideePar is not null
        defaultEvaluationRealiseeFiltering("valideePar.specified=true", "valideePar.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeParContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideePar contains
        defaultEvaluationRealiseeFiltering("valideePar.contains=" + DEFAULT_VALIDEE_PAR, "valideePar.contains=" + UPDATED_VALIDEE_PAR);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeParNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideePar does not contain
        defaultEvaluationRealiseeFiltering(
            "valideePar.doesNotContain=" + UPDATED_VALIDEE_PAR,
            "valideePar.doesNotContain=" + DEFAULT_VALIDEE_PAR
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeLeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideeLe equals to
        defaultEvaluationRealiseeFiltering("valideeLe.equals=" + DEFAULT_VALIDEE_LE, "valideeLe.equals=" + UPDATED_VALIDEE_LE);
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeLeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideeLe in
        defaultEvaluationRealiseeFiltering(
            "valideeLe.in=" + DEFAULT_VALIDEE_LE + "," + UPDATED_VALIDEE_LE,
            "valideeLe.in=" + UPDATED_VALIDEE_LE
        );
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByValideeLeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        // Get all the evaluationRealiseeList where valideeLe is not null
        defaultEvaluationRealiseeFiltering("valideeLe.specified=true", "valideeLe.specified=false");
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByEvaluationPrevueIsEqualToSomething() throws Exception {
        EvaluationPrevue evaluationPrevue;
        if (TestUtil.findAll(em, EvaluationPrevue.class).isEmpty()) {
            evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);
            evaluationPrevue = EvaluationPrevueResourceIT.createEntity(em);
        } else {
            evaluationPrevue = TestUtil.findAll(em, EvaluationPrevue.class).get(0);
        }
        em.persist(evaluationPrevue);
        em.flush();
        evaluationRealisee.setEvaluationPrevue(evaluationPrevue);
        evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);
        Long evaluationPrevueId = evaluationPrevue.getId();
        // Get all the evaluationRealiseeList where evaluationPrevue equals to evaluationPrevueId
        defaultEvaluationRealiseeShouldBeFound("evaluationPrevueId.equals=" + evaluationPrevueId);

        // Get all the evaluationRealiseeList where evaluationPrevue equals to (evaluationPrevueId + 1)
        defaultEvaluationRealiseeShouldNotBeFound("evaluationPrevueId.equals=" + (evaluationPrevueId + 1));
    }

    @Test
    @Transactional
    void getAllEvaluationRealiseesByEtudiantIsEqualToSomething() throws Exception {
        Etudiant etudiant;
        if (TestUtil.findAll(em, Etudiant.class).isEmpty()) {
            evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);
            etudiant = EtudiantResourceIT.createEntity();
        } else {
            etudiant = TestUtil.findAll(em, Etudiant.class).get(0);
        }
        em.persist(etudiant);
        em.flush();
        evaluationRealisee.setEtudiant(etudiant);
        evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);
        Long etudiantId = etudiant.getId();
        // Get all the evaluationRealiseeList where etudiant equals to etudiantId
        defaultEvaluationRealiseeShouldBeFound("etudiantId.equals=" + etudiantId);

        // Get all the evaluationRealiseeList where etudiant equals to (etudiantId + 1)
        defaultEvaluationRealiseeShouldNotBeFound("etudiantId.equals=" + (etudiantId + 1));
    }

    private void defaultEvaluationRealiseeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEvaluationRealiseeShouldBeFound(shouldBeFound);
        defaultEvaluationRealiseeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvaluationRealiseeShouldBeFound(String filter) throws Exception {
        restEvaluationRealiseeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationRealisee.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(sameNumber(DEFAULT_NOTE))))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].compteDansMoyenne").value(hasItem(DEFAULT_COMPTE_DANS_MOYENNE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].commentaire1").value(hasItem(DEFAULT_COMMENTAIRE_1)))
            .andExpect(jsonPath("$.[*].commentaire2").value(hasItem(DEFAULT_COMMENTAIRE_2)))
            .andExpect(jsonPath("$.[*].commentaire3").value(hasItem(DEFAULT_COMMENTAIRE_3)))
            .andExpect(jsonPath("$.[*].saisiePar").value(hasItem(DEFAULT_SAISIE_PAR)))
            .andExpect(jsonPath("$.[*].saisieLe").value(hasItem(DEFAULT_SAISIE_LE.toString())))
            .andExpect(jsonPath("$.[*].valideePar").value(hasItem(DEFAULT_VALIDEE_PAR)))
            .andExpect(jsonPath("$.[*].valideeLe").value(hasItem(DEFAULT_VALIDEE_LE.toString())));

        // Check, that the count call also returns 1
        restEvaluationRealiseeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvaluationRealiseeShouldNotBeFound(String filter) throws Exception {
        restEvaluationRealiseeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvaluationRealiseeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvaluationRealisee() throws Exception {
        // Get the evaluationRealisee
        restEvaluationRealiseeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvaluationRealisee() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluationRealisee
        EvaluationRealisee updatedEvaluationRealisee = evaluationRealiseeRepository.findById(evaluationRealisee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvaluationRealisee are not directly saved in db
        em.detach(updatedEvaluationRealisee);
        updatedEvaluationRealisee
            .note(UPDATED_NOTE)
            .statut(UPDATED_STATUT)
            .compteDansMoyenne(UPDATED_COMPTE_DANS_MOYENNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire1(UPDATED_COMMENTAIRE_1)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .saisiePar(UPDATED_SAISIE_PAR)
            .saisieLe(UPDATED_SAISIE_LE)
            .valideePar(UPDATED_VALIDEE_PAR)
            .valideeLe(UPDATED_VALIDEE_LE);
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(updatedEvaluationRealisee);

        restEvaluationRealiseeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationRealiseeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEvaluationRealiseeToMatchAllProperties(updatedEvaluationRealisee);
    }

    @Test
    @Transactional
    void putNonExistingEvaluationRealisee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationRealisee.setId(longCount.incrementAndGet());

        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationRealiseeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationRealiseeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluationRealisee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationRealisee.setId(longCount.incrementAndGet());

        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationRealiseeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluationRealisee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationRealisee.setId(longCount.incrementAndGet());

        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationRealiseeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluationRealiseeWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluationRealisee using partial update
        EvaluationRealisee partialUpdatedEvaluationRealisee = new EvaluationRealisee();
        partialUpdatedEvaluationRealisee.setId(evaluationRealisee.getId());

        partialUpdatedEvaluationRealisee
            .statut(UPDATED_STATUT)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .saisiePar(UPDATED_SAISIE_PAR)
            .valideePar(UPDATED_VALIDEE_PAR)
            .valideeLe(UPDATED_VALIDEE_LE);

        restEvaluationRealiseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluationRealisee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluationRealisee))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationRealisee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationRealiseeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEvaluationRealisee, evaluationRealisee),
            getPersistedEvaluationRealisee(evaluationRealisee)
        );
    }

    @Test
    @Transactional
    void fullUpdateEvaluationRealiseeWithPatch() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evaluationRealisee using partial update
        EvaluationRealisee partialUpdatedEvaluationRealisee = new EvaluationRealisee();
        partialUpdatedEvaluationRealisee.setId(evaluationRealisee.getId());

        partialUpdatedEvaluationRealisee
            .note(UPDATED_NOTE)
            .statut(UPDATED_STATUT)
            .compteDansMoyenne(UPDATED_COMPTE_DANS_MOYENNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .commentaire1(UPDATED_COMMENTAIRE_1)
            .commentaire2(UPDATED_COMMENTAIRE_2)
            .commentaire3(UPDATED_COMMENTAIRE_3)
            .saisiePar(UPDATED_SAISIE_PAR)
            .saisieLe(UPDATED_SAISIE_LE)
            .valideePar(UPDATED_VALIDEE_PAR)
            .valideeLe(UPDATED_VALIDEE_LE);

        restEvaluationRealiseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluationRealisee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvaluationRealisee))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationRealisee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvaluationRealiseeUpdatableFieldsEquals(
            partialUpdatedEvaluationRealisee,
            getPersistedEvaluationRealisee(partialUpdatedEvaluationRealisee)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEvaluationRealisee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationRealisee.setId(longCount.incrementAndGet());

        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationRealiseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluationRealiseeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluationRealisee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationRealisee.setId(longCount.incrementAndGet());

        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationRealiseeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluationRealisee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evaluationRealisee.setId(longCount.incrementAndGet());

        // Create the EvaluationRealisee
        EvaluationRealiseeDTO evaluationRealiseeDTO = evaluationRealiseeMapper.toDto(evaluationRealisee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationRealiseeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evaluationRealiseeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluationRealisee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluationRealisee() throws Exception {
        // Initialize the database
        insertedEvaluationRealisee = evaluationRealiseeRepository.saveAndFlush(evaluationRealisee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evaluationRealisee
        restEvaluationRealiseeMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluationRealisee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return evaluationRealiseeRepository.count();
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

    protected EvaluationRealisee getPersistedEvaluationRealisee(EvaluationRealisee evaluationRealisee) {
        return evaluationRealiseeRepository.findById(evaluationRealisee.getId()).orElseThrow();
    }

    protected void assertPersistedEvaluationRealiseeToMatchAllProperties(EvaluationRealisee expectedEvaluationRealisee) {
        assertEvaluationRealiseeAllPropertiesEquals(expectedEvaluationRealisee, getPersistedEvaluationRealisee(expectedEvaluationRealisee));
    }

    protected void assertPersistedEvaluationRealiseeToMatchUpdatableProperties(EvaluationRealisee expectedEvaluationRealisee) {
        assertEvaluationRealiseeAllUpdatablePropertiesEquals(
            expectedEvaluationRealisee,
            getPersistedEvaluationRealisee(expectedEvaluationRealisee)
        );
    }
}
