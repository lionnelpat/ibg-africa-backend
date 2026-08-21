package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EtudiantAsserts.*;
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
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.Pays;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.service.EtudiantService;
import org.forbidec.service.dto.EtudiantDTO;
import org.forbidec.service.mapper.EtudiantMapper;
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
 * Integration tests for the {@link EtudiantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtudiantResourceIT {

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICULARITE = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE_ENTREE = 1900;
    private static final Integer UPDATED_ANNEE_ENTREE = 1901;
    private static final Integer SMALLER_ANNEE_ENTREE = 1900 - 1;

    private static final Boolean DEFAULT_CURSUS_ACHEVE = false;
    private static final Boolean UPDATED_CURSUS_ACHEVE = true;

    private static final Integer DEFAULT_ANNEE_FINALE = 1900;
    private static final Integer UPDATED_ANNEE_FINALE = 1901;
    private static final Integer SMALLER_ANNEE_FINALE = 1900 - 1;

    private static final String DEFAULT_KEYCLOAK_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Mock
    private EtudiantRepository etudiantRepositoryMock;

    @Autowired
    private EtudiantMapper etudiantMapper;

    @Mock
    private EtudiantService etudiantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    private Etudiant insertedEtudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity() {
        return new Etudiant()
            .matricule(DEFAULT_MATRICULE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .particularite(DEFAULT_PARTICULARITE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .anneeEntree(DEFAULT_ANNEE_ENTREE)
            .cursusAcheve(DEFAULT_CURSUS_ACHEVE)
            .anneeFinale(DEFAULT_ANNEE_FINALE)
            .keycloakUserId(DEFAULT_KEYCLOAK_USER_ID)
            .commentaire(DEFAULT_COMMENTAIRE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createUpdatedEntity() {
        return new Etudiant()
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .particularite(UPDATED_PARTICULARITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .anneeEntree(UPDATED_ANNEE_ENTREE)
            .cursusAcheve(UPDATED_CURSUS_ACHEVE)
            .anneeFinale(UPDATED_ANNEE_FINALE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        etudiant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEtudiant != null) {
            etudiantRepository.delete(insertedEtudiant);
            insertedEtudiant = null;
        }
    }

    @Test
    @Transactional
    void createEtudiant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);
        var returnedEtudiantDTO = om.readValue(
            restEtudiantMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EtudiantDTO.class
        );

        // Validate the Etudiant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEtudiant = etudiantMapper.toEntity(returnedEtudiantDTO);
        assertEtudiantUpdatableFieldsEquals(returnedEtudiant, getPersistedEtudiant(returnedEtudiant));

        insertedEtudiant = returnedEtudiant;
    }

    @Test
    @Transactional
    void createEtudiantWithExistingId() throws Exception {
        // Create the Etudiant with an existing ID
        etudiant.setId(1L);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setNom(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setPrenom(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCursusAcheveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setCursusAcheve(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        etudiant.setActif(null);

        // Create the Etudiant, which fails.
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtudiants() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].particularite").value(hasItem(DEFAULT_PARTICULARITE)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].anneeEntree").value(hasItem(DEFAULT_ANNEE_ENTREE)))
            .andExpect(jsonPath("$.[*].cursusAcheve").value(hasItem(DEFAULT_CURSUS_ACHEVE)))
            .andExpect(jsonPath("$.[*].anneeFinale").value(hasItem(DEFAULT_ANNEE_FINALE)))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtudiantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(etudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(etudiantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtudiantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(etudiantServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(etudiantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEtudiant() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.particularite").value(DEFAULT_PARTICULARITE))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.anneeEntree").value(DEFAULT_ANNEE_ENTREE))
            .andExpect(jsonPath("$.cursusAcheve").value(DEFAULT_CURSUS_ACHEVE))
            .andExpect(jsonPath("$.anneeFinale").value(DEFAULT_ANNEE_FINALE))
            .andExpect(jsonPath("$.keycloakUserId").value(DEFAULT_KEYCLOAK_USER_ID))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getEtudiantsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        Long id = etudiant.getId();

        defaultEtudiantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEtudiantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEtudiantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEtudiantsByMatriculeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where matricule equals to
        defaultEtudiantFiltering("matricule.equals=" + DEFAULT_MATRICULE, "matricule.equals=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByMatriculeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where matricule in
        defaultEtudiantFiltering("matricule.in=" + DEFAULT_MATRICULE + "," + UPDATED_MATRICULE, "matricule.in=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByMatriculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where matricule is not null
        defaultEtudiantFiltering("matricule.specified=true", "matricule.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByMatriculeContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where matricule contains
        defaultEtudiantFiltering("matricule.contains=" + DEFAULT_MATRICULE, "matricule.contains=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByMatriculeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where matricule does not contain
        defaultEtudiantFiltering("matricule.doesNotContain=" + UPDATED_MATRICULE, "matricule.doesNotContain=" + DEFAULT_MATRICULE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where nom equals to
        defaultEtudiantFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where nom in
        defaultEtudiantFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where nom is not null
        defaultEtudiantFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where nom contains
        defaultEtudiantFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where nom does not contain
        defaultEtudiantFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where prenom equals to
        defaultEtudiantFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where prenom in
        defaultEtudiantFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where prenom is not null
        defaultEtudiantFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where prenom contains
        defaultEtudiantFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where prenom does not contain
        defaultEtudiantFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllEtudiantsByParticulariteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where particularite equals to
        defaultEtudiantFiltering("particularite.equals=" + DEFAULT_PARTICULARITE, "particularite.equals=" + UPDATED_PARTICULARITE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByParticulariteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where particularite in
        defaultEtudiantFiltering(
            "particularite.in=" + DEFAULT_PARTICULARITE + "," + UPDATED_PARTICULARITE,
            "particularite.in=" + UPDATED_PARTICULARITE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByParticulariteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where particularite is not null
        defaultEtudiantFiltering("particularite.specified=true", "particularite.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByParticulariteContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where particularite contains
        defaultEtudiantFiltering("particularite.contains=" + DEFAULT_PARTICULARITE, "particularite.contains=" + UPDATED_PARTICULARITE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByParticulariteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where particularite does not contain
        defaultEtudiantFiltering(
            "particularite.doesNotContain=" + UPDATED_PARTICULARITE,
            "particularite.doesNotContain=" + DEFAULT_PARTICULARITE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance equals to
        defaultEtudiantFiltering("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE, "dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance in
        defaultEtudiantFiltering(
            "dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE,
            "dateNaissance.in=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is not null
        defaultEtudiantFiltering("dateNaissance.specified=true", "dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is greater than or equal to
        defaultEtudiantFiltering(
            "dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE,
            "dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is less than or equal to
        defaultEtudiantFiltering(
            "dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE,
            "dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is less than
        defaultEtudiantFiltering("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE, "dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where dateNaissance is greater than
        defaultEtudiantFiltering(
            "dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE,
            "dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where email equals to
        defaultEtudiantFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEtudiantsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where email in
        defaultEtudiantFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEtudiantsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where email is not null
        defaultEtudiantFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where email contains
        defaultEtudiantFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEtudiantsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where email does not contain
        defaultEtudiantFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEtudiantsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where telephone equals to
        defaultEtudiantFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where telephone in
        defaultEtudiantFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where telephone is not null
        defaultEtudiantFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where telephone contains
        defaultEtudiantFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where telephone does not contain
        defaultEtudiantFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree equals to
        defaultEtudiantFiltering("anneeEntree.equals=" + DEFAULT_ANNEE_ENTREE, "anneeEntree.equals=" + UPDATED_ANNEE_ENTREE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree in
        defaultEtudiantFiltering(
            "anneeEntree.in=" + DEFAULT_ANNEE_ENTREE + "," + UPDATED_ANNEE_ENTREE,
            "anneeEntree.in=" + UPDATED_ANNEE_ENTREE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree is not null
        defaultEtudiantFiltering("anneeEntree.specified=true", "anneeEntree.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree is greater than or equal to
        defaultEtudiantFiltering(
            "anneeEntree.greaterThanOrEqual=" + DEFAULT_ANNEE_ENTREE,
            "anneeEntree.greaterThanOrEqual=" + (DEFAULT_ANNEE_ENTREE + 1)
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree is less than or equal to
        defaultEtudiantFiltering(
            "anneeEntree.lessThanOrEqual=" + DEFAULT_ANNEE_ENTREE,
            "anneeEntree.lessThanOrEqual=" + SMALLER_ANNEE_ENTREE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree is less than
        defaultEtudiantFiltering("anneeEntree.lessThan=" + (DEFAULT_ANNEE_ENTREE + 1), "anneeEntree.lessThan=" + DEFAULT_ANNEE_ENTREE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeEntreeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeEntree is greater than
        defaultEtudiantFiltering("anneeEntree.greaterThan=" + SMALLER_ANNEE_ENTREE, "anneeEntree.greaterThan=" + DEFAULT_ANNEE_ENTREE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByCursusAcheveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where cursusAcheve equals to
        defaultEtudiantFiltering("cursusAcheve.equals=" + DEFAULT_CURSUS_ACHEVE, "cursusAcheve.equals=" + UPDATED_CURSUS_ACHEVE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByCursusAcheveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where cursusAcheve in
        defaultEtudiantFiltering(
            "cursusAcheve.in=" + DEFAULT_CURSUS_ACHEVE + "," + UPDATED_CURSUS_ACHEVE,
            "cursusAcheve.in=" + UPDATED_CURSUS_ACHEVE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByCursusAcheveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where cursusAcheve is not null
        defaultEtudiantFiltering("cursusAcheve.specified=true", "cursusAcheve.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale equals to
        defaultEtudiantFiltering("anneeFinale.equals=" + DEFAULT_ANNEE_FINALE, "anneeFinale.equals=" + UPDATED_ANNEE_FINALE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale in
        defaultEtudiantFiltering(
            "anneeFinale.in=" + DEFAULT_ANNEE_FINALE + "," + UPDATED_ANNEE_FINALE,
            "anneeFinale.in=" + UPDATED_ANNEE_FINALE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale is not null
        defaultEtudiantFiltering("anneeFinale.specified=true", "anneeFinale.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale is greater than or equal to
        defaultEtudiantFiltering(
            "anneeFinale.greaterThanOrEqual=" + DEFAULT_ANNEE_FINALE,
            "anneeFinale.greaterThanOrEqual=" + (DEFAULT_ANNEE_FINALE + 1)
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale is less than or equal to
        defaultEtudiantFiltering(
            "anneeFinale.lessThanOrEqual=" + DEFAULT_ANNEE_FINALE,
            "anneeFinale.lessThanOrEqual=" + SMALLER_ANNEE_FINALE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale is less than
        defaultEtudiantFiltering("anneeFinale.lessThan=" + (DEFAULT_ANNEE_FINALE + 1), "anneeFinale.lessThan=" + DEFAULT_ANNEE_FINALE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByAnneeFinaleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where anneeFinale is greater than
        defaultEtudiantFiltering("anneeFinale.greaterThan=" + SMALLER_ANNEE_FINALE, "anneeFinale.greaterThan=" + DEFAULT_ANNEE_FINALE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByKeycloakUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where keycloakUserId equals to
        defaultEtudiantFiltering("keycloakUserId.equals=" + DEFAULT_KEYCLOAK_USER_ID, "keycloakUserId.equals=" + UPDATED_KEYCLOAK_USER_ID);
    }

    @Test
    @Transactional
    void getAllEtudiantsByKeycloakUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where keycloakUserId in
        defaultEtudiantFiltering(
            "keycloakUserId.in=" + DEFAULT_KEYCLOAK_USER_ID + "," + UPDATED_KEYCLOAK_USER_ID,
            "keycloakUserId.in=" + UPDATED_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByKeycloakUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where keycloakUserId is not null
        defaultEtudiantFiltering("keycloakUserId.specified=true", "keycloakUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByKeycloakUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where keycloakUserId contains
        defaultEtudiantFiltering(
            "keycloakUserId.contains=" + DEFAULT_KEYCLOAK_USER_ID,
            "keycloakUserId.contains=" + UPDATED_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByKeycloakUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where keycloakUserId does not contain
        defaultEtudiantFiltering(
            "keycloakUserId.doesNotContain=" + UPDATED_KEYCLOAK_USER_ID,
            "keycloakUserId.doesNotContain=" + DEFAULT_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where commentaire equals to
        defaultEtudiantFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where commentaire in
        defaultEtudiantFiltering(
            "commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE,
            "commentaire.in=" + UPDATED_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllEtudiantsByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where commentaire is not null
        defaultEtudiantFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where commentaire contains
        defaultEtudiantFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where commentaire does not contain
        defaultEtudiantFiltering("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE, "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEtudiantsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where actif equals to
        defaultEtudiantFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllEtudiantsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where actif in
        defaultEtudiantFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllEtudiantsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList where actif is not null
        defaultEtudiantFiltering("actif.specified=true", "actif.specified=false");
    }

    @Test
    @Transactional
    void getAllEtudiantsByPaysIsEqualToSomething() throws Exception {
        Pays pays;
        if (TestUtil.findAll(em, Pays.class).isEmpty()) {
            etudiantRepository.saveAndFlush(etudiant);
            pays = PaysResourceIT.createEntity();
        } else {
            pays = TestUtil.findAll(em, Pays.class).get(0);
        }
        em.persist(pays);
        em.flush();
        etudiant.setPays(pays);
        etudiantRepository.saveAndFlush(etudiant);
        Long paysId = pays.getId();
        // Get all the etudiantList where pays equals to paysId
        defaultEtudiantShouldBeFound("paysId.equals=" + paysId);

        // Get all the etudiantList where pays equals to (paysId + 1)
        defaultEtudiantShouldNotBeFound("paysId.equals=" + (paysId + 1));
    }

    private void defaultEtudiantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEtudiantShouldBeFound(shouldBeFound);
        defaultEtudiantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEtudiantShouldBeFound(String filter) throws Exception {
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].particularite").value(hasItem(DEFAULT_PARTICULARITE)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].anneeEntree").value(hasItem(DEFAULT_ANNEE_ENTREE)))
            .andExpect(jsonPath("$.[*].cursusAcheve").value(hasItem(DEFAULT_CURSUS_ACHEVE)))
            .andExpect(jsonPath("$.[*].anneeFinale").value(hasItem(DEFAULT_ANNEE_FINALE)))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEtudiantShouldNotBeFound(String filter) throws Exception {
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtudiant() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtudiant are not directly saved in db
        em.detach(updatedEtudiant);
        updatedEtudiant
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .particularite(UPDATED_PARTICULARITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .anneeEntree(UPDATED_ANNEE_ENTREE)
            .cursusAcheve(UPDATED_CURSUS_ACHEVE)
            .anneeFinale(UPDATED_ANNEE_FINALE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(updatedEtudiant);

        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEtudiantToMatchAllProperties(updatedEtudiant);
    }

    @Test
    @Transactional
    void putNonExistingEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(etudiantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .prenom(UPDATED_PRENOM)
            .particularite(UPDATED_PARTICULARITE)
            .anneeEntree(UPDATED_ANNEE_ENTREE)
            .cursusAcheve(UPDATED_CURSUS_ACHEVE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtudiantUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEtudiant, etudiant), getPersistedEtudiant(etudiant));
    }

    @Test
    @Transactional
    void fullUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .particularite(UPDATED_PARTICULARITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .anneeEntree(UPDATED_ANNEE_ENTREE)
            .cursusAcheve(UPDATED_CURSUS_ACHEVE)
            .anneeFinale(UPDATED_ANNEE_FINALE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEtudiantUpdatableFieldsEquals(partialUpdatedEtudiant, getPersistedEtudiant(partialUpdatedEtudiant));
    }

    @Test
    @Transactional
    void patchNonExistingEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etudiantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtudiant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        etudiant.setId(longCount.incrementAndGet());

        // Create the Etudiant
        EtudiantDTO etudiantDTO = etudiantMapper.toDto(etudiant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(etudiantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtudiant() throws Exception {
        // Initialize the database
        insertedEtudiant = etudiantRepository.saveAndFlush(etudiant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the etudiant
        restEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, etudiant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return etudiantRepository.count();
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

    protected Etudiant getPersistedEtudiant(Etudiant etudiant) {
        return etudiantRepository.findById(etudiant.getId()).orElseThrow();
    }

    protected void assertPersistedEtudiantToMatchAllProperties(Etudiant expectedEtudiant) {
        assertEtudiantAllPropertiesEquals(expectedEtudiant, getPersistedEtudiant(expectedEtudiant));
    }

    protected void assertPersistedEtudiantToMatchUpdatableProperties(Etudiant expectedEtudiant) {
        assertEtudiantAllUpdatablePropertiesEquals(expectedEtudiant, getPersistedEtudiant(expectedEtudiant));
    }
}
