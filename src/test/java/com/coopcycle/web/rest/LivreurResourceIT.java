package com.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coopcycle.IntegrationTest;
import com.coopcycle.domain.Commande;
import com.coopcycle.domain.Livreur;
import com.coopcycle.repository.LivreurRepository;
import com.coopcycle.service.criteria.LivreurCriteria;
import com.coopcycle.service.dto.LivreurDTO;
import com.coopcycle.service.mapper.LivreurMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LivreurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivreurResourceIT {

    private static final Integer DEFAULT_NUM_LIVREUR = 1;
    private static final Integer UPDATED_NUM_LIVREUR = 2;
    private static final Integer SMALLER_NUM_LIVREUR = 1 - 1;

    private static final String DEFAULT_NOM_LIVREUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LIVREUR = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION_GPS = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_GPS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private LivreurMapper livreurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivreurMockMvc;

    private Livreur livreur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createEntity(EntityManager em) {
        Livreur livreur = new Livreur().numLivreur(DEFAULT_NUM_LIVREUR).nomLivreur(DEFAULT_NOM_LIVREUR).positionGPS(DEFAULT_POSITION_GPS);
        return livreur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createUpdatedEntity(EntityManager em) {
        Livreur livreur = new Livreur().numLivreur(UPDATED_NUM_LIVREUR).nomLivreur(UPDATED_NOM_LIVREUR).positionGPS(UPDATED_POSITION_GPS);
        return livreur;
    }

    @BeforeEach
    public void initTest() {
        livreur = createEntity(em);
    }

    @Test
    @Transactional
    void createLivreur() throws Exception {
        int databaseSizeBeforeCreate = livreurRepository.findAll().size();
        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);
        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreurDTO)))
            .andExpect(status().isCreated());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate + 1);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getNumLivreur()).isEqualTo(DEFAULT_NUM_LIVREUR);
        assertThat(testLivreur.getNomLivreur()).isEqualTo(DEFAULT_NOM_LIVREUR);
        assertThat(testLivreur.getPositionGPS()).isEqualTo(DEFAULT_POSITION_GPS);
    }

    @Test
    @Transactional
    void createLivreurWithExistingId() throws Exception {
        // Create the Livreur with an existing ID
        livreur.setId(1L);
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        int databaseSizeBeforeCreate = livreurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumLivreurIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().size();
        // set the field null
        livreur.setNumLivreur(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreurDTO)))
            .andExpect(status().isBadRequest());

        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomLivreurIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().size();
        // set the field null
        livreur.setNomLivreur(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreurDTO)))
            .andExpect(status().isBadRequest());

        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionGPSIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().size();
        // set the field null
        livreur.setPositionGPS(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreurDTO)))
            .andExpect(status().isBadRequest());

        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLivreurs() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livreur.getId().intValue())))
            .andExpect(jsonPath("$.[*].numLivreur").value(hasItem(DEFAULT_NUM_LIVREUR)))
            .andExpect(jsonPath("$.[*].nomLivreur").value(hasItem(DEFAULT_NOM_LIVREUR)))
            .andExpect(jsonPath("$.[*].positionGPS").value(hasItem(DEFAULT_POSITION_GPS)));
    }

    @Test
    @Transactional
    void getLivreur() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get the livreur
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL_ID, livreur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livreur.getId().intValue()))
            .andExpect(jsonPath("$.numLivreur").value(DEFAULT_NUM_LIVREUR))
            .andExpect(jsonPath("$.nomLivreur").value(DEFAULT_NOM_LIVREUR))
            .andExpect(jsonPath("$.positionGPS").value(DEFAULT_POSITION_GPS));
    }

    @Test
    @Transactional
    void getLivreursByIdFiltering() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        Long id = livreur.getId();

        defaultLivreurShouldBeFound("id.equals=" + id);
        defaultLivreurShouldNotBeFound("id.notEquals=" + id);

        defaultLivreurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLivreurShouldNotBeFound("id.greaterThan=" + id);

        defaultLivreurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLivreurShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur equals to DEFAULT_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.equals=" + DEFAULT_NUM_LIVREUR);

        // Get all the livreurList where numLivreur equals to UPDATED_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.equals=" + UPDATED_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsNotEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur not equals to DEFAULT_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.notEquals=" + DEFAULT_NUM_LIVREUR);

        // Get all the livreurList where numLivreur not equals to UPDATED_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.notEquals=" + UPDATED_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsInShouldWork() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur in DEFAULT_NUM_LIVREUR or UPDATED_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.in=" + DEFAULT_NUM_LIVREUR + "," + UPDATED_NUM_LIVREUR);

        // Get all the livreurList where numLivreur equals to UPDATED_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.in=" + UPDATED_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsNullOrNotNull() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur is not null
        defaultLivreurShouldBeFound("numLivreur.specified=true");

        // Get all the livreurList where numLivreur is null
        defaultLivreurShouldNotBeFound("numLivreur.specified=false");
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur is greater than or equal to DEFAULT_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.greaterThanOrEqual=" + DEFAULT_NUM_LIVREUR);

        // Get all the livreurList where numLivreur is greater than or equal to UPDATED_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.greaterThanOrEqual=" + UPDATED_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur is less than or equal to DEFAULT_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.lessThanOrEqual=" + DEFAULT_NUM_LIVREUR);

        // Get all the livreurList where numLivreur is less than or equal to SMALLER_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.lessThanOrEqual=" + SMALLER_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsLessThanSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur is less than DEFAULT_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.lessThan=" + DEFAULT_NUM_LIVREUR);

        // Get all the livreurList where numLivreur is less than UPDATED_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.lessThan=" + UPDATED_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNumLivreurIsGreaterThanSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where numLivreur is greater than DEFAULT_NUM_LIVREUR
        defaultLivreurShouldNotBeFound("numLivreur.greaterThan=" + DEFAULT_NUM_LIVREUR);

        // Get all the livreurList where numLivreur is greater than SMALLER_NUM_LIVREUR
        defaultLivreurShouldBeFound("numLivreur.greaterThan=" + SMALLER_NUM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNomLivreurIsEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where nomLivreur equals to DEFAULT_NOM_LIVREUR
        defaultLivreurShouldBeFound("nomLivreur.equals=" + DEFAULT_NOM_LIVREUR);

        // Get all the livreurList where nomLivreur equals to UPDATED_NOM_LIVREUR
        defaultLivreurShouldNotBeFound("nomLivreur.equals=" + UPDATED_NOM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNomLivreurIsNotEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where nomLivreur not equals to DEFAULT_NOM_LIVREUR
        defaultLivreurShouldNotBeFound("nomLivreur.notEquals=" + DEFAULT_NOM_LIVREUR);

        // Get all the livreurList where nomLivreur not equals to UPDATED_NOM_LIVREUR
        defaultLivreurShouldBeFound("nomLivreur.notEquals=" + UPDATED_NOM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNomLivreurIsInShouldWork() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where nomLivreur in DEFAULT_NOM_LIVREUR or UPDATED_NOM_LIVREUR
        defaultLivreurShouldBeFound("nomLivreur.in=" + DEFAULT_NOM_LIVREUR + "," + UPDATED_NOM_LIVREUR);

        // Get all the livreurList where nomLivreur equals to UPDATED_NOM_LIVREUR
        defaultLivreurShouldNotBeFound("nomLivreur.in=" + UPDATED_NOM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNomLivreurIsNullOrNotNull() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where nomLivreur is not null
        defaultLivreurShouldBeFound("nomLivreur.specified=true");

        // Get all the livreurList where nomLivreur is null
        defaultLivreurShouldNotBeFound("nomLivreur.specified=false");
    }

    @Test
    @Transactional
    void getAllLivreursByNomLivreurContainsSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where nomLivreur contains DEFAULT_NOM_LIVREUR
        defaultLivreurShouldBeFound("nomLivreur.contains=" + DEFAULT_NOM_LIVREUR);

        // Get all the livreurList where nomLivreur contains UPDATED_NOM_LIVREUR
        defaultLivreurShouldNotBeFound("nomLivreur.contains=" + UPDATED_NOM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByNomLivreurNotContainsSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where nomLivreur does not contain DEFAULT_NOM_LIVREUR
        defaultLivreurShouldNotBeFound("nomLivreur.doesNotContain=" + DEFAULT_NOM_LIVREUR);

        // Get all the livreurList where nomLivreur does not contain UPDATED_NOM_LIVREUR
        defaultLivreurShouldBeFound("nomLivreur.doesNotContain=" + UPDATED_NOM_LIVREUR);
    }

    @Test
    @Transactional
    void getAllLivreursByPositionGPSIsEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where positionGPS equals to DEFAULT_POSITION_GPS
        defaultLivreurShouldBeFound("positionGPS.equals=" + DEFAULT_POSITION_GPS);

        // Get all the livreurList where positionGPS equals to UPDATED_POSITION_GPS
        defaultLivreurShouldNotBeFound("positionGPS.equals=" + UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void getAllLivreursByPositionGPSIsNotEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where positionGPS not equals to DEFAULT_POSITION_GPS
        defaultLivreurShouldNotBeFound("positionGPS.notEquals=" + DEFAULT_POSITION_GPS);

        // Get all the livreurList where positionGPS not equals to UPDATED_POSITION_GPS
        defaultLivreurShouldBeFound("positionGPS.notEquals=" + UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void getAllLivreursByPositionGPSIsInShouldWork() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where positionGPS in DEFAULT_POSITION_GPS or UPDATED_POSITION_GPS
        defaultLivreurShouldBeFound("positionGPS.in=" + DEFAULT_POSITION_GPS + "," + UPDATED_POSITION_GPS);

        // Get all the livreurList where positionGPS equals to UPDATED_POSITION_GPS
        defaultLivreurShouldNotBeFound("positionGPS.in=" + UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void getAllLivreursByPositionGPSIsNullOrNotNull() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where positionGPS is not null
        defaultLivreurShouldBeFound("positionGPS.specified=true");

        // Get all the livreurList where positionGPS is null
        defaultLivreurShouldNotBeFound("positionGPS.specified=false");
    }

    @Test
    @Transactional
    void getAllLivreursByPositionGPSContainsSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where positionGPS contains DEFAULT_POSITION_GPS
        defaultLivreurShouldBeFound("positionGPS.contains=" + DEFAULT_POSITION_GPS);

        // Get all the livreurList where positionGPS contains UPDATED_POSITION_GPS
        defaultLivreurShouldNotBeFound("positionGPS.contains=" + UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void getAllLivreursByPositionGPSNotContainsSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList where positionGPS does not contain DEFAULT_POSITION_GPS
        defaultLivreurShouldNotBeFound("positionGPS.doesNotContain=" + DEFAULT_POSITION_GPS);

        // Get all the livreurList where positionGPS does not contain UPDATED_POSITION_GPS
        defaultLivreurShouldBeFound("positionGPS.doesNotContain=" + UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void getAllLivreursByCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);
        Commande commande = CommandeResourceIT.createEntity(em);
        em.persist(commande);
        em.flush();
        livreur.addCommande(commande);
        livreurRepository.saveAndFlush(livreur);
        Long commandeId = commande.getId();

        // Get all the livreurList where commande equals to commandeId
        defaultLivreurShouldBeFound("commandeId.equals=" + commandeId);

        // Get all the livreurList where commande equals to (commandeId + 1)
        defaultLivreurShouldNotBeFound("commandeId.equals=" + (commandeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLivreurShouldBeFound(String filter) throws Exception {
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livreur.getId().intValue())))
            .andExpect(jsonPath("$.[*].numLivreur").value(hasItem(DEFAULT_NUM_LIVREUR)))
            .andExpect(jsonPath("$.[*].nomLivreur").value(hasItem(DEFAULT_NOM_LIVREUR)))
            .andExpect(jsonPath("$.[*].positionGPS").value(hasItem(DEFAULT_POSITION_GPS)));

        // Check, that the count call also returns 1
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLivreurShouldNotBeFound(String filter) throws Exception {
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLivreur() throws Exception {
        // Get the livreur
        restLivreurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivreur() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();

        // Update the livreur
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).get();
        // Disconnect from session so that the updates on updatedLivreur are not directly saved in db
        em.detach(updatedLivreur);
        updatedLivreur.numLivreur(UPDATED_NUM_LIVREUR).nomLivreur(UPDATED_NOM_LIVREUR).positionGPS(UPDATED_POSITION_GPS);
        LivreurDTO livreurDTO = livreurMapper.toDto(updatedLivreur);

        restLivreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livreurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getNumLivreur()).isEqualTo(UPDATED_NUM_LIVREUR);
        assertThat(testLivreur.getNomLivreur()).isEqualTo(UPDATED_NOM_LIVREUR);
        assertThat(testLivreur.getPositionGPS()).isEqualTo(UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void putNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livreurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.positionGPS(UPDATED_POSITION_GPS);

        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            )
            .andExpect(status().isOk());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getNumLivreur()).isEqualTo(DEFAULT_NUM_LIVREUR);
        assertThat(testLivreur.getNomLivreur()).isEqualTo(DEFAULT_NOM_LIVREUR);
        assertThat(testLivreur.getPositionGPS()).isEqualTo(UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void fullUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.numLivreur(UPDATED_NUM_LIVREUR).nomLivreur(UPDATED_NOM_LIVREUR).positionGPS(UPDATED_POSITION_GPS);

        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            )
            .andExpect(status().isOk());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getNumLivreur()).isEqualTo(UPDATED_NUM_LIVREUR);
        assertThat(testLivreur.getNomLivreur()).isEqualTo(UPDATED_NOM_LIVREUR);
        assertThat(testLivreur.getPositionGPS()).isEqualTo(UPDATED_POSITION_GPS);
    }

    @Test
    @Transactional
    void patchNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livreurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livreurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(livreurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivreur() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeDelete = livreurRepository.findAll().size();

        // Delete the livreur
        restLivreurMockMvc
            .perform(delete(ENTITY_API_URL_ID, livreur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
