package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.HistoriqueNoteAsserts.*;
import static org.forbidec.web.rest.TestUtil.sameNumber;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.domain.HistoriqueNote;
import org.forbidec.domain.enumeration.StatutNote;
import org.forbidec.domain.enumeration.StatutNote;
import org.forbidec.repository.HistoriqueNoteRepository;
import org.forbidec.service.HistoriqueNoteService;
import org.forbidec.service.mapper.HistoriqueNoteMapper;
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
 * Integration tests for the {@link HistoriqueNoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueNoteResourceIT {

    private static final BigDecimal DEFAULT_NOTE_AVANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOTE_AVANT = new BigDecimal(2);
    private static final BigDecimal SMALLER_NOTE_AVANT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_NOTE_APRES = new BigDecimal(1);
    private static final BigDecimal UPDATED_NOTE_APRES = new BigDecimal(2);
    private static final BigDecimal SMALLER_NOTE_APRES = new BigDecimal(1 - 1);

    private static final StatutNote DEFAULT_STATUT_AVANT = StatutNote.NON_SAISIE;
    private static final StatutNote UPDATED_STATUT_AVANT = StatutNote.SAISIE;

    private static final StatutNote DEFAULT_STATUT_APRES = StatutNote.NON_SAISIE;
    private static final StatutNote UPDATED_STATUT_APRES = StatutNote.SAISIE;

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final String DEFAULT_MODIFIE_PAR = "AAAAAAAAAA";
    private static final String UPDATED_MODIFIE_PAR = "BBBBBBBBBB";

    private static final Instant DEFAULT_MODIFIE_LE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIE_LE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/historique-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueNoteRepository historiqueNoteRepository;

    @Mock
    private HistoriqueNoteRepository historiqueNoteRepositoryMock;

    @Autowired
    private HistoriqueNoteMapper historiqueNoteMapper;

    @Mock
    private HistoriqueNoteService historiqueNoteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueNoteMockMvc;

    private HistoriqueNote historiqueNote;

    private HistoriqueNote insertedHistoriqueNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueNote createEntity(EntityManager em) {
        HistoriqueNote historiqueNote = new HistoriqueNote()
            .noteAvant(DEFAULT_NOTE_AVANT)
            .noteApres(DEFAULT_NOTE_APRES)
            .statutAvant(DEFAULT_STATUT_AVANT)
            .statutApres(DEFAULT_STATUT_APRES)
            .motif(DEFAULT_MOTIF)
            .modifiePar(DEFAULT_MODIFIE_PAR)
            .modifieLe(DEFAULT_MODIFIE_LE);
        // Add required entity
        EvaluationRealisee evaluationRealisee;
        if (TestUtil.findAll(em, EvaluationRealisee.class).isEmpty()) {
            evaluationRealisee = EvaluationRealiseeResourceIT.createEntity(em);
            em.persist(evaluationRealisee);
            em.flush();
        } else {
            evaluationRealisee = TestUtil.findAll(em, EvaluationRealisee.class).get(0);
        }
        historiqueNote.setEvaluationRealisee(evaluationRealisee);
        return historiqueNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueNote createUpdatedEntity(EntityManager em) {
        HistoriqueNote updatedHistoriqueNote = new HistoriqueNote()
            .noteAvant(UPDATED_NOTE_AVANT)
            .noteApres(UPDATED_NOTE_APRES)
            .statutAvant(UPDATED_STATUT_AVANT)
            .statutApres(UPDATED_STATUT_APRES)
            .motif(UPDATED_MOTIF)
            .modifiePar(UPDATED_MODIFIE_PAR)
            .modifieLe(UPDATED_MODIFIE_LE);
        // Add required entity
        EvaluationRealisee evaluationRealisee;
        if (TestUtil.findAll(em, EvaluationRealisee.class).isEmpty()) {
            evaluationRealisee = EvaluationRealiseeResourceIT.createUpdatedEntity(em);
            em.persist(evaluationRealisee);
            em.flush();
        } else {
            evaluationRealisee = TestUtil.findAll(em, EvaluationRealisee.class).get(0);
        }
        updatedHistoriqueNote.setEvaluationRealisee(evaluationRealisee);
        return updatedHistoriqueNote;
    }

    @BeforeEach
    void initTest() {
        historiqueNote = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriqueNote != null) {
            historiqueNoteRepository.delete(insertedHistoriqueNote);
            insertedHistoriqueNote = null;
        }
    }

    @Test
    @Transactional
    void getAllHistoriqueNotes() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList
        restHistoriqueNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].noteAvant").value(hasItem(sameNumber(DEFAULT_NOTE_AVANT))))
            .andExpect(jsonPath("$.[*].noteApres").value(hasItem(sameNumber(DEFAULT_NOTE_APRES))))
            .andExpect(jsonPath("$.[*].statutAvant").value(hasItem(DEFAULT_STATUT_AVANT.toString())))
            .andExpect(jsonPath("$.[*].statutApres").value(hasItem(DEFAULT_STATUT_APRES.toString())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].modifiePar").value(hasItem(DEFAULT_MODIFIE_PAR)))
            .andExpect(jsonPath("$.[*].modifieLe").value(hasItem(DEFAULT_MODIFIE_LE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoriqueNotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(historiqueNoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoriqueNoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(historiqueNoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoriqueNotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(historiqueNoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoriqueNoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(historiqueNoteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHistoriqueNote() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get the historiqueNote
        restHistoriqueNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueNote.getId().intValue()))
            .andExpect(jsonPath("$.noteAvant").value(sameNumber(DEFAULT_NOTE_AVANT)))
            .andExpect(jsonPath("$.noteApres").value(sameNumber(DEFAULT_NOTE_APRES)))
            .andExpect(jsonPath("$.statutAvant").value(DEFAULT_STATUT_AVANT.toString()))
            .andExpect(jsonPath("$.statutApres").value(DEFAULT_STATUT_APRES.toString()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.modifiePar").value(DEFAULT_MODIFIE_PAR))
            .andExpect(jsonPath("$.modifieLe").value(DEFAULT_MODIFIE_LE.toString()));
    }

    @Test
    @Transactional
    void getHistoriqueNotesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        Long id = historiqueNote.getId();

        defaultHistoriqueNoteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoriqueNoteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoriqueNoteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant equals to
        defaultHistoriqueNoteFiltering("noteAvant.equals=" + DEFAULT_NOTE_AVANT, "noteAvant.equals=" + UPDATED_NOTE_AVANT);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant in
        defaultHistoriqueNoteFiltering(
            "noteAvant.in=" + DEFAULT_NOTE_AVANT + "," + UPDATED_NOTE_AVANT,
            "noteAvant.in=" + UPDATED_NOTE_AVANT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant is not null
        defaultHistoriqueNoteFiltering("noteAvant.specified=true", "noteAvant.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant is greater than or equal to
        defaultHistoriqueNoteFiltering(
            "noteAvant.greaterThanOrEqual=" + DEFAULT_NOTE_AVANT,
            "noteAvant.greaterThanOrEqual=" + UPDATED_NOTE_AVANT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant is less than or equal to
        defaultHistoriqueNoteFiltering(
            "noteAvant.lessThanOrEqual=" + DEFAULT_NOTE_AVANT,
            "noteAvant.lessThanOrEqual=" + SMALLER_NOTE_AVANT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant is less than
        defaultHistoriqueNoteFiltering("noteAvant.lessThan=" + UPDATED_NOTE_AVANT, "noteAvant.lessThan=" + DEFAULT_NOTE_AVANT);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteAvantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteAvant is greater than
        defaultHistoriqueNoteFiltering("noteAvant.greaterThan=" + SMALLER_NOTE_AVANT, "noteAvant.greaterThan=" + DEFAULT_NOTE_AVANT);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres equals to
        defaultHistoriqueNoteFiltering("noteApres.equals=" + DEFAULT_NOTE_APRES, "noteApres.equals=" + UPDATED_NOTE_APRES);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres in
        defaultHistoriqueNoteFiltering(
            "noteApres.in=" + DEFAULT_NOTE_APRES + "," + UPDATED_NOTE_APRES,
            "noteApres.in=" + UPDATED_NOTE_APRES
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres is not null
        defaultHistoriqueNoteFiltering("noteApres.specified=true", "noteApres.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres is greater than or equal to
        defaultHistoriqueNoteFiltering(
            "noteApres.greaterThanOrEqual=" + DEFAULT_NOTE_APRES,
            "noteApres.greaterThanOrEqual=" + UPDATED_NOTE_APRES
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres is less than or equal to
        defaultHistoriqueNoteFiltering(
            "noteApres.lessThanOrEqual=" + DEFAULT_NOTE_APRES,
            "noteApres.lessThanOrEqual=" + SMALLER_NOTE_APRES
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres is less than
        defaultHistoriqueNoteFiltering("noteApres.lessThan=" + UPDATED_NOTE_APRES, "noteApres.lessThan=" + DEFAULT_NOTE_APRES);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByNoteApresIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where noteApres is greater than
        defaultHistoriqueNoteFiltering("noteApres.greaterThan=" + SMALLER_NOTE_APRES, "noteApres.greaterThan=" + DEFAULT_NOTE_APRES);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByStatutAvantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where statutAvant equals to
        defaultHistoriqueNoteFiltering("statutAvant.equals=" + DEFAULT_STATUT_AVANT, "statutAvant.equals=" + UPDATED_STATUT_AVANT);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByStatutAvantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where statutAvant in
        defaultHistoriqueNoteFiltering(
            "statutAvant.in=" + DEFAULT_STATUT_AVANT + "," + UPDATED_STATUT_AVANT,
            "statutAvant.in=" + UPDATED_STATUT_AVANT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByStatutAvantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where statutAvant is not null
        defaultHistoriqueNoteFiltering("statutAvant.specified=true", "statutAvant.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByStatutApresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where statutApres equals to
        defaultHistoriqueNoteFiltering("statutApres.equals=" + DEFAULT_STATUT_APRES, "statutApres.equals=" + UPDATED_STATUT_APRES);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByStatutApresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where statutApres in
        defaultHistoriqueNoteFiltering(
            "statutApres.in=" + DEFAULT_STATUT_APRES + "," + UPDATED_STATUT_APRES,
            "statutApres.in=" + UPDATED_STATUT_APRES
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByStatutApresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where statutApres is not null
        defaultHistoriqueNoteFiltering("statutApres.specified=true", "statutApres.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByMotifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where motif equals to
        defaultHistoriqueNoteFiltering("motif.equals=" + DEFAULT_MOTIF, "motif.equals=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByMotifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where motif in
        defaultHistoriqueNoteFiltering("motif.in=" + DEFAULT_MOTIF + "," + UPDATED_MOTIF, "motif.in=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByMotifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where motif is not null
        defaultHistoriqueNoteFiltering("motif.specified=true", "motif.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByMotifContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where motif contains
        defaultHistoriqueNoteFiltering("motif.contains=" + DEFAULT_MOTIF, "motif.contains=" + UPDATED_MOTIF);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByMotifNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where motif does not contain
        defaultHistoriqueNoteFiltering("motif.doesNotContain=" + UPDATED_MOTIF, "motif.doesNotContain=" + DEFAULT_MOTIF);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieParIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifiePar equals to
        defaultHistoriqueNoteFiltering("modifiePar.equals=" + DEFAULT_MODIFIE_PAR, "modifiePar.equals=" + UPDATED_MODIFIE_PAR);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieParIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifiePar in
        defaultHistoriqueNoteFiltering(
            "modifiePar.in=" + DEFAULT_MODIFIE_PAR + "," + UPDATED_MODIFIE_PAR,
            "modifiePar.in=" + UPDATED_MODIFIE_PAR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieParIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifiePar is not null
        defaultHistoriqueNoteFiltering("modifiePar.specified=true", "modifiePar.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieParContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifiePar contains
        defaultHistoriqueNoteFiltering("modifiePar.contains=" + DEFAULT_MODIFIE_PAR, "modifiePar.contains=" + UPDATED_MODIFIE_PAR);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieParNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifiePar does not contain
        defaultHistoriqueNoteFiltering(
            "modifiePar.doesNotContain=" + UPDATED_MODIFIE_PAR,
            "modifiePar.doesNotContain=" + DEFAULT_MODIFIE_PAR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieLeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifieLe equals to
        defaultHistoriqueNoteFiltering("modifieLe.equals=" + DEFAULT_MODIFIE_LE, "modifieLe.equals=" + UPDATED_MODIFIE_LE);
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieLeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifieLe in
        defaultHistoriqueNoteFiltering(
            "modifieLe.in=" + DEFAULT_MODIFIE_LE + "," + UPDATED_MODIFIE_LE,
            "modifieLe.in=" + UPDATED_MODIFIE_LE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByModifieLeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueNote = historiqueNoteRepository.saveAndFlush(historiqueNote);

        // Get all the historiqueNoteList where modifieLe is not null
        defaultHistoriqueNoteFiltering("modifieLe.specified=true", "modifieLe.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueNotesByEvaluationRealiseeIsEqualToSomething() throws Exception {
        EvaluationRealisee evaluationRealisee;
        if (TestUtil.findAll(em, EvaluationRealisee.class).isEmpty()) {
            historiqueNoteRepository.saveAndFlush(historiqueNote);
            evaluationRealisee = EvaluationRealiseeResourceIT.createEntity(em);
        } else {
            evaluationRealisee = TestUtil.findAll(em, EvaluationRealisee.class).get(0);
        }
        em.persist(evaluationRealisee);
        em.flush();
        historiqueNote.setEvaluationRealisee(evaluationRealisee);
        historiqueNoteRepository.saveAndFlush(historiqueNote);
        Long evaluationRealiseeId = evaluationRealisee.getId();
        // Get all the historiqueNoteList where evaluationRealisee equals to evaluationRealiseeId
        defaultHistoriqueNoteShouldBeFound("evaluationRealiseeId.equals=" + evaluationRealiseeId);

        // Get all the historiqueNoteList where evaluationRealisee equals to (evaluationRealiseeId + 1)
        defaultHistoriqueNoteShouldNotBeFound("evaluationRealiseeId.equals=" + (evaluationRealiseeId + 1));
    }

    private void defaultHistoriqueNoteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoriqueNoteShouldBeFound(shouldBeFound);
        defaultHistoriqueNoteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueNoteShouldBeFound(String filter) throws Exception {
        restHistoriqueNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].noteAvant").value(hasItem(sameNumber(DEFAULT_NOTE_AVANT))))
            .andExpect(jsonPath("$.[*].noteApres").value(hasItem(sameNumber(DEFAULT_NOTE_APRES))))
            .andExpect(jsonPath("$.[*].statutAvant").value(hasItem(DEFAULT_STATUT_AVANT.toString())))
            .andExpect(jsonPath("$.[*].statutApres").value(hasItem(DEFAULT_STATUT_APRES.toString())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].modifiePar").value(hasItem(DEFAULT_MODIFIE_PAR)))
            .andExpect(jsonPath("$.[*].modifieLe").value(hasItem(DEFAULT_MODIFIE_LE.toString())));

        // Check, that the count call also returns 1
        restHistoriqueNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueNoteShouldNotBeFound(String filter) throws Exception {
        restHistoriqueNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueNote() throws Exception {
        // Get the historiqueNote
        restHistoriqueNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    protected long getRepositoryCount() {
        return historiqueNoteRepository.count();
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

    protected HistoriqueNote getPersistedHistoriqueNote(HistoriqueNote historiqueNote) {
        return historiqueNoteRepository.findById(historiqueNote.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueNoteToMatchAllProperties(HistoriqueNote expectedHistoriqueNote) {
        assertHistoriqueNoteAllPropertiesEquals(expectedHistoriqueNote, getPersistedHistoriqueNote(expectedHistoriqueNote));
    }

    protected void assertPersistedHistoriqueNoteToMatchUpdatableProperties(HistoriqueNote expectedHistoriqueNote) {
        assertHistoriqueNoteAllUpdatablePropertiesEquals(expectedHistoriqueNote, getPersistedHistoriqueNote(expectedHistoriqueNote));
    }
}
