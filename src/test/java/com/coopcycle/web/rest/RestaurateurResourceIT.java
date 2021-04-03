package com.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coopcycle.IntegrationTest;
import com.coopcycle.domain.Commande;
import com.coopcycle.domain.Restaurateur;
import com.coopcycle.repository.RestaurateurRepository;
import com.coopcycle.service.criteria.RestaurateurCriteria;
import com.coopcycle.service.dto.RestaurateurDTO;
import com.coopcycle.service.mapper.RestaurateurMapper;
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
 * Integration tests for the {@link RestaurateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurateurResourceIT {

    private static final Long DEFAULT_N_RESTAURANT = 1L;
    private static final Long UPDATED_N_RESTAURANT = 2L;
    private static final Long SMALLER_N_RESTAURANT = 1L - 1L;

    private static final String DEFAULT_ADRESSE_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_RESTAURANT = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RESTAURANT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurateurRepository restaurateurRepository;

    @Autowired
    private RestaurateurMapper restaurateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurateurMockMvc;

    private Restaurateur restaurateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurateur createEntity(EntityManager em) {
        Restaurateur restaurateur = new Restaurateur()
            .nRestaurant(DEFAULT_N_RESTAURANT)
            .adresseRestaurant(DEFAULT_ADRESSE_RESTAURANT)
            .nomRestaurant(DEFAULT_NOM_RESTAURANT);
        return restaurateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurateur createUpdatedEntity(EntityManager em) {
        Restaurateur restaurateur = new Restaurateur()
            .nRestaurant(UPDATED_N_RESTAURANT)
            .adresseRestaurant(UPDATED_ADRESSE_RESTAURANT)
            .nomRestaurant(UPDATED_NOM_RESTAURANT);
        return restaurateur;
    }

    @BeforeEach
    public void initTest() {
        restaurateur = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurateur() throws Exception {
        int databaseSizeBeforeCreate = restaurateurRepository.findAll().size();
        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);
        restRestaurateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getnRestaurant()).isEqualTo(DEFAULT_N_RESTAURANT);
        assertThat(testRestaurateur.getAdresseRestaurant()).isEqualTo(DEFAULT_ADRESSE_RESTAURANT);
        assertThat(testRestaurateur.getNomRestaurant()).isEqualTo(DEFAULT_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void createRestaurateurWithExistingId() throws Exception {
        // Create the Restaurateur with an existing ID
        restaurateur.setId(1L);
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        int databaseSizeBeforeCreate = restaurateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checknRestaurantIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurateurRepository.findAll().size();
        // set the field null
        restaurateur.setnRestaurant(null);

        // Create the Restaurateur, which fails.
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        restRestaurateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAdresseRestaurantIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurateurRepository.findAll().size();
        // set the field null
        restaurateur.setAdresseRestaurant(null);

        // Create the Restaurateur, which fails.
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        restRestaurateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomRestaurantIsRequired() throws Exception {
        int databaseSizeBeforeTest = restaurateurRepository.findAll().size();
        // set the field null
        restaurateur.setNomRestaurant(null);

        // Create the Restaurateur, which fails.
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        restRestaurateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRestaurateurs() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nRestaurant").value(hasItem(DEFAULT_N_RESTAURANT.intValue())))
            .andExpect(jsonPath("$.[*].adresseRestaurant").value(hasItem(DEFAULT_ADRESSE_RESTAURANT)))
            .andExpect(jsonPath("$.[*].nomRestaurant").value(hasItem(DEFAULT_NOM_RESTAURANT)));
    }

    @Test
    @Transactional
    void getRestaurateur() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get the restaurateur
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurateur.getId().intValue()))
            .andExpect(jsonPath("$.nRestaurant").value(DEFAULT_N_RESTAURANT.intValue()))
            .andExpect(jsonPath("$.adresseRestaurant").value(DEFAULT_ADRESSE_RESTAURANT))
            .andExpect(jsonPath("$.nomRestaurant").value(DEFAULT_NOM_RESTAURANT));
    }

    @Test
    @Transactional
    void getRestaurateursByIdFiltering() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        Long id = restaurateur.getId();

        defaultRestaurateurShouldBeFound("id.equals=" + id);
        defaultRestaurateurShouldNotBeFound("id.notEquals=" + id);

        defaultRestaurateurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRestaurateurShouldNotBeFound("id.greaterThan=" + id);

        defaultRestaurateurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRestaurateurShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant equals to DEFAULT_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.equals=" + DEFAULT_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant equals to UPDATED_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.equals=" + UPDATED_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant not equals to DEFAULT_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.notEquals=" + DEFAULT_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant not equals to UPDATED_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.notEquals=" + UPDATED_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsInShouldWork() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant in DEFAULT_N_RESTAURANT or UPDATED_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.in=" + DEFAULT_N_RESTAURANT + "," + UPDATED_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant equals to UPDATED_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.in=" + UPDATED_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant is not null
        defaultRestaurateurShouldBeFound("nRestaurant.specified=true");

        // Get all the restaurateurList where nRestaurant is null
        defaultRestaurateurShouldNotBeFound("nRestaurant.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant is greater than or equal to DEFAULT_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.greaterThanOrEqual=" + DEFAULT_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant is greater than or equal to UPDATED_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.greaterThanOrEqual=" + UPDATED_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant is less than or equal to DEFAULT_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.lessThanOrEqual=" + DEFAULT_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant is less than or equal to SMALLER_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.lessThanOrEqual=" + SMALLER_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsLessThanSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant is less than DEFAULT_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.lessThan=" + DEFAULT_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant is less than UPDATED_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.lessThan=" + UPDATED_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursBynRestaurantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nRestaurant is greater than DEFAULT_N_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nRestaurant.greaterThan=" + DEFAULT_N_RESTAURANT);

        // Get all the restaurateurList where nRestaurant is greater than SMALLER_N_RESTAURANT
        defaultRestaurateurShouldBeFound("nRestaurant.greaterThan=" + SMALLER_N_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByAdresseRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where adresseRestaurant equals to DEFAULT_ADRESSE_RESTAURANT
        defaultRestaurateurShouldBeFound("adresseRestaurant.equals=" + DEFAULT_ADRESSE_RESTAURANT);

        // Get all the restaurateurList where adresseRestaurant equals to UPDATED_ADRESSE_RESTAURANT
        defaultRestaurateurShouldNotBeFound("adresseRestaurant.equals=" + UPDATED_ADRESSE_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByAdresseRestaurantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where adresseRestaurant not equals to DEFAULT_ADRESSE_RESTAURANT
        defaultRestaurateurShouldNotBeFound("adresseRestaurant.notEquals=" + DEFAULT_ADRESSE_RESTAURANT);

        // Get all the restaurateurList where adresseRestaurant not equals to UPDATED_ADRESSE_RESTAURANT
        defaultRestaurateurShouldBeFound("adresseRestaurant.notEquals=" + UPDATED_ADRESSE_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByAdresseRestaurantIsInShouldWork() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where adresseRestaurant in DEFAULT_ADRESSE_RESTAURANT or UPDATED_ADRESSE_RESTAURANT
        defaultRestaurateurShouldBeFound("adresseRestaurant.in=" + DEFAULT_ADRESSE_RESTAURANT + "," + UPDATED_ADRESSE_RESTAURANT);

        // Get all the restaurateurList where adresseRestaurant equals to UPDATED_ADRESSE_RESTAURANT
        defaultRestaurateurShouldNotBeFound("adresseRestaurant.in=" + UPDATED_ADRESSE_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByAdresseRestaurantIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where adresseRestaurant is not null
        defaultRestaurateurShouldBeFound("adresseRestaurant.specified=true");

        // Get all the restaurateurList where adresseRestaurant is null
        defaultRestaurateurShouldNotBeFound("adresseRestaurant.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurateursByAdresseRestaurantContainsSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where adresseRestaurant contains DEFAULT_ADRESSE_RESTAURANT
        defaultRestaurateurShouldBeFound("adresseRestaurant.contains=" + DEFAULT_ADRESSE_RESTAURANT);

        // Get all the restaurateurList where adresseRestaurant contains UPDATED_ADRESSE_RESTAURANT
        defaultRestaurateurShouldNotBeFound("adresseRestaurant.contains=" + UPDATED_ADRESSE_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByAdresseRestaurantNotContainsSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where adresseRestaurant does not contain DEFAULT_ADRESSE_RESTAURANT
        defaultRestaurateurShouldNotBeFound("adresseRestaurant.doesNotContain=" + DEFAULT_ADRESSE_RESTAURANT);

        // Get all the restaurateurList where adresseRestaurant does not contain UPDATED_ADRESSE_RESTAURANT
        defaultRestaurateurShouldBeFound("adresseRestaurant.doesNotContain=" + UPDATED_ADRESSE_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByNomRestaurantIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nomRestaurant equals to DEFAULT_NOM_RESTAURANT
        defaultRestaurateurShouldBeFound("nomRestaurant.equals=" + DEFAULT_NOM_RESTAURANT);

        // Get all the restaurateurList where nomRestaurant equals to UPDATED_NOM_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nomRestaurant.equals=" + UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByNomRestaurantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nomRestaurant not equals to DEFAULT_NOM_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nomRestaurant.notEquals=" + DEFAULT_NOM_RESTAURANT);

        // Get all the restaurateurList where nomRestaurant not equals to UPDATED_NOM_RESTAURANT
        defaultRestaurateurShouldBeFound("nomRestaurant.notEquals=" + UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByNomRestaurantIsInShouldWork() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nomRestaurant in DEFAULT_NOM_RESTAURANT or UPDATED_NOM_RESTAURANT
        defaultRestaurateurShouldBeFound("nomRestaurant.in=" + DEFAULT_NOM_RESTAURANT + "," + UPDATED_NOM_RESTAURANT);

        // Get all the restaurateurList where nomRestaurant equals to UPDATED_NOM_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nomRestaurant.in=" + UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByNomRestaurantIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nomRestaurant is not null
        defaultRestaurateurShouldBeFound("nomRestaurant.specified=true");

        // Get all the restaurateurList where nomRestaurant is null
        defaultRestaurateurShouldNotBeFound("nomRestaurant.specified=false");
    }

    @Test
    @Transactional
    void getAllRestaurateursByNomRestaurantContainsSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nomRestaurant contains DEFAULT_NOM_RESTAURANT
        defaultRestaurateurShouldBeFound("nomRestaurant.contains=" + DEFAULT_NOM_RESTAURANT);

        // Get all the restaurateurList where nomRestaurant contains UPDATED_NOM_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nomRestaurant.contains=" + UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByNomRestaurantNotContainsSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList where nomRestaurant does not contain DEFAULT_NOM_RESTAURANT
        defaultRestaurateurShouldNotBeFound("nomRestaurant.doesNotContain=" + DEFAULT_NOM_RESTAURANT);

        // Get all the restaurateurList where nomRestaurant does not contain UPDATED_NOM_RESTAURANT
        defaultRestaurateurShouldBeFound("nomRestaurant.doesNotContain=" + UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void getAllRestaurateursByCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);
        Commande commande = CommandeResourceIT.createEntity(em);
        em.persist(commande);
        em.flush();
        restaurateur.addCommande(commande);
        restaurateurRepository.saveAndFlush(restaurateur);
        Long commandeId = commande.getId();

        // Get all the restaurateurList where commande equals to commandeId
        defaultRestaurateurShouldBeFound("commandeId.equals=" + commandeId);

        // Get all the restaurateurList where commande equals to (commandeId + 1)
        defaultRestaurateurShouldNotBeFound("commandeId.equals=" + (commandeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRestaurateurShouldBeFound(String filter) throws Exception {
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nRestaurant").value(hasItem(DEFAULT_N_RESTAURANT.intValue())))
            .andExpect(jsonPath("$.[*].adresseRestaurant").value(hasItem(DEFAULT_ADRESSE_RESTAURANT)))
            .andExpect(jsonPath("$.[*].nomRestaurant").value(hasItem(DEFAULT_NOM_RESTAURANT)));

        // Check, that the count call also returns 1
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRestaurateurShouldNotBeFound(String filter) throws Exception {
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRestaurateur() throws Exception {
        // Get the restaurateur
        restRestaurateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurateur() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();

        // Update the restaurateur
        Restaurateur updatedRestaurateur = restaurateurRepository.findById(restaurateur.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurateur are not directly saved in db
        em.detach(updatedRestaurateur);
        updatedRestaurateur
            .nRestaurant(UPDATED_N_RESTAURANT)
            .adresseRestaurant(UPDATED_ADRESSE_RESTAURANT)
            .nomRestaurant(UPDATED_NOM_RESTAURANT);
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(updatedRestaurateur);

        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getnRestaurant()).isEqualTo(UPDATED_N_RESTAURANT);
        assertThat(testRestaurateur.getAdresseRestaurant()).isEqualTo(UPDATED_ADRESSE_RESTAURANT);
        assertThat(testRestaurateur.getNomRestaurant()).isEqualTo(UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void putNonExistingRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurateurWithPatch() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();

        // Update the restaurateur using partial update
        Restaurateur partialUpdatedRestaurateur = new Restaurateur();
        partialUpdatedRestaurateur.setId(restaurateur.getId());

        partialUpdatedRestaurateur.nomRestaurant(UPDATED_NOM_RESTAURANT);

        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurateur))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getnRestaurant()).isEqualTo(DEFAULT_N_RESTAURANT);
        assertThat(testRestaurateur.getAdresseRestaurant()).isEqualTo(DEFAULT_ADRESSE_RESTAURANT);
        assertThat(testRestaurateur.getNomRestaurant()).isEqualTo(UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void fullUpdateRestaurateurWithPatch() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();

        // Update the restaurateur using partial update
        Restaurateur partialUpdatedRestaurateur = new Restaurateur();
        partialUpdatedRestaurateur.setId(restaurateur.getId());

        partialUpdatedRestaurateur
            .nRestaurant(UPDATED_N_RESTAURANT)
            .adresseRestaurant(UPDATED_ADRESSE_RESTAURANT)
            .nomRestaurant(UPDATED_NOM_RESTAURANT);

        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurateur))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getnRestaurant()).isEqualTo(UPDATED_N_RESTAURANT);
        assertThat(testRestaurateur.getAdresseRestaurant()).isEqualTo(UPDATED_ADRESSE_RESTAURANT);
        assertThat(testRestaurateur.getNomRestaurant()).isEqualTo(UPDATED_NOM_RESTAURANT);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // Create the Restaurateur
        RestaurateurDTO restaurateurDTO = restaurateurMapper.toDto(restaurateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateurDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurateur() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeDelete = restaurateurRepository.findAll().size();

        // Delete the restaurateur
        restRestaurateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
