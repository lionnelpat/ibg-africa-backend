package org.forbidec.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EnseignantAsserts.*;
import static org.forbidec.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.forbidec.IntegrationTest;
import org.forbidec.domain.Enseignant;
import org.forbidec.repository.EnseignantRepository;
import org.forbidec.service.dto.EnseignantDTO;
import org.forbidec.service.mapper.EnseignantMapper;
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
 * Integration tests for the {@link EnseignantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnseignantResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_LONG = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_LONG = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE_COURT = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE_COURT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_KEYCLOAK_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_KEYCLOAK_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/enseignants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignantMapper enseignantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    private Enseignant insertedEnseignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity() {
        return new Enseignant()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .libelleLong(DEFAULT_LIBELLE_LONG)
            .libelleCourt(DEFAULT_LIBELLE_COURT)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
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
    public static Enseignant createUpdatedEntity() {
        return new Enseignant()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        enseignant = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEnseignant != null) {
            enseignantRepository.delete(insertedEnseignant);
            insertedEnseignant = null;
        }
    }

    @Test
    @Transactional
    void createEnseignant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);
        var returnedEnseignantDTO = om.readValue(
            restEnseignantMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnseignantDTO.class
        );

        // Validate the Enseignant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnseignant = enseignantMapper.toEntity(returnedEnseignantDTO);
        assertEnseignantUpdatableFieldsEquals(returnedEnseignant, getPersistedEnseignant(returnedEnseignant));

        insertedEnseignant = returnedEnseignant;
    }

    @Test
    @Transactional
    void createEnseignantWithExistingId() throws Exception {
        // Create the Enseignant with an existing ID
        enseignant.setId(1L);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setNom(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setPrenom(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enseignant.setActif(null);

        // Create the Enseignant, which fails.
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnseignants() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL_ID, enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.libelleLong").value(DEFAULT_LIBELLE_LONG))
            .andExpect(jsonPath("$.libelleCourt").value(DEFAULT_LIBELLE_COURT))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.keycloakUserId").value(DEFAULT_KEYCLOAK_USER_ID))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getEnseignantsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        Long id = enseignant.getId();

        defaultEnseignantFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEnseignantFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEnseignantFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom equals to
        defaultEnseignantFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom in
        defaultEnseignantFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom is not null
        defaultEnseignantFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom contains
        defaultEnseignantFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where nom does not contain
        defaultEnseignantFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom equals to
        defaultEnseignantFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom in
        defaultEnseignantFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom is not null
        defaultEnseignantFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom contains
        defaultEnseignantFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where prenom does not contain
        defaultEnseignantFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleLongIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleLong equals to
        defaultEnseignantFiltering("libelleLong.equals=" + DEFAULT_LIBELLE_LONG, "libelleLong.equals=" + UPDATED_LIBELLE_LONG);
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleLongIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleLong in
        defaultEnseignantFiltering(
            "libelleLong.in=" + DEFAULT_LIBELLE_LONG + "," + UPDATED_LIBELLE_LONG,
            "libelleLong.in=" + UPDATED_LIBELLE_LONG
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleLongIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleLong is not null
        defaultEnseignantFiltering("libelleLong.specified=true", "libelleLong.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleLongContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleLong contains
        defaultEnseignantFiltering("libelleLong.contains=" + DEFAULT_LIBELLE_LONG, "libelleLong.contains=" + UPDATED_LIBELLE_LONG);
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleLongNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleLong does not contain
        defaultEnseignantFiltering(
            "libelleLong.doesNotContain=" + UPDATED_LIBELLE_LONG,
            "libelleLong.doesNotContain=" + DEFAULT_LIBELLE_LONG
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleCourtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleCourt equals to
        defaultEnseignantFiltering("libelleCourt.equals=" + DEFAULT_LIBELLE_COURT, "libelleCourt.equals=" + UPDATED_LIBELLE_COURT);
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleCourtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleCourt in
        defaultEnseignantFiltering(
            "libelleCourt.in=" + DEFAULT_LIBELLE_COURT + "," + UPDATED_LIBELLE_COURT,
            "libelleCourt.in=" + UPDATED_LIBELLE_COURT
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleCourtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleCourt is not null
        defaultEnseignantFiltering("libelleCourt.specified=true", "libelleCourt.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleCourtContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleCourt contains
        defaultEnseignantFiltering("libelleCourt.contains=" + DEFAULT_LIBELLE_COURT, "libelleCourt.contains=" + UPDATED_LIBELLE_COURT);
    }

    @Test
    @Transactional
    void getAllEnseignantsByLibelleCourtNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where libelleCourt does not contain
        defaultEnseignantFiltering(
            "libelleCourt.doesNotContain=" + UPDATED_LIBELLE_COURT,
            "libelleCourt.doesNotContain=" + DEFAULT_LIBELLE_COURT
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email equals to
        defaultEnseignantFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email in
        defaultEnseignantFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email is not null
        defaultEnseignantFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email contains
        defaultEnseignantFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where email does not contain
        defaultEnseignantFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone equals to
        defaultEnseignantFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone in
        defaultEnseignantFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone is not null
        defaultEnseignantFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone contains
        defaultEnseignantFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where telephone does not contain
        defaultEnseignantFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByKeycloakUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where keycloakUserId equals to
        defaultEnseignantFiltering(
            "keycloakUserId.equals=" + DEFAULT_KEYCLOAK_USER_ID,
            "keycloakUserId.equals=" + UPDATED_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByKeycloakUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where keycloakUserId in
        defaultEnseignantFiltering(
            "keycloakUserId.in=" + DEFAULT_KEYCLOAK_USER_ID + "," + UPDATED_KEYCLOAK_USER_ID,
            "keycloakUserId.in=" + UPDATED_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByKeycloakUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where keycloakUserId is not null
        defaultEnseignantFiltering("keycloakUserId.specified=true", "keycloakUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByKeycloakUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where keycloakUserId contains
        defaultEnseignantFiltering(
            "keycloakUserId.contains=" + DEFAULT_KEYCLOAK_USER_ID,
            "keycloakUserId.contains=" + UPDATED_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByKeycloakUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where keycloakUserId does not contain
        defaultEnseignantFiltering(
            "keycloakUserId.doesNotContain=" + UPDATED_KEYCLOAK_USER_ID,
            "keycloakUserId.doesNotContain=" + DEFAULT_KEYCLOAK_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where commentaire equals to
        defaultEnseignantFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where commentaire in
        defaultEnseignantFiltering(
            "commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE,
            "commentaire.in=" + UPDATED_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where commentaire is not null
        defaultEnseignantFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllEnseignantsByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where commentaire contains
        defaultEnseignantFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllEnseignantsByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where commentaire does not contain
        defaultEnseignantFiltering(
            "commentaire.doesNotContain=" + UPDATED_COMMENTAIRE,
            "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllEnseignantsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where actif equals to
        defaultEnseignantFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllEnseignantsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where actif in
        defaultEnseignantFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllEnseignantsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList where actif is not null
        defaultEnseignantFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultEnseignantFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEnseignantShouldBeFound(shouldBeFound);
        defaultEnseignantShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnseignantShouldBeFound(String filter) throws Exception {
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].libelleLong").value(hasItem(DEFAULT_LIBELLE_LONG)))
            .andExpect(jsonPath("$.[*].libelleCourt").value(hasItem(DEFAULT_LIBELLE_COURT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].keycloakUserId").value(hasItem(DEFAULT_KEYCLOAK_USER_ID)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnseignantShouldNotBeFound(String filter) throws Exception {
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(updatedEnseignant);

        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnseignantToMatchAllProperties(updatedEnseignant);
    }

    @Test
    @Transactional
    void putNonExistingEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enseignantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .prenom(UPDATED_PRENOM)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .email(UPDATED_EMAIL)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnseignantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnseignant, enseignant),
            getPersistedEnseignant(enseignant)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .libelleLong(UPDATED_LIBELLE_LONG)
            .libelleCourt(UPDATED_LIBELLE_COURT)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .keycloakUserId(UPDATED_KEYCLOAK_USER_ID)
            .commentaire(UPDATED_COMMENTAIRE)
            .actif(UPDATED_ACTIF);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnseignantUpdatableFieldsEquals(partialUpdatedEnseignant, getPersistedEnseignant(partialUpdatedEnseignant));
    }

    @Test
    @Transactional
    void patchNonExistingEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enseignantDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enseignant.setId(longCount.incrementAndGet());

        // Create the Enseignant
        EnseignantDTO enseignantDTO = enseignantMapper.toDto(enseignant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enseignantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnseignant() throws Exception {
        // Initialize the database
        insertedEnseignant = enseignantRepository.saveAndFlush(enseignant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enseignant
        restEnseignantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enseignantRepository.count();
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

    protected Enseignant getPersistedEnseignant(Enseignant enseignant) {
        return enseignantRepository.findById(enseignant.getId()).orElseThrow();
    }

    protected void assertPersistedEnseignantToMatchAllProperties(Enseignant expectedEnseignant) {
        assertEnseignantAllPropertiesEquals(expectedEnseignant, getPersistedEnseignant(expectedEnseignant));
    }

    protected void assertPersistedEnseignantToMatchUpdatableProperties(Enseignant expectedEnseignant) {
        assertEnseignantAllUpdatablePropertiesEquals(expectedEnseignant, getPersistedEnseignant(expectedEnseignant));
    }
}
