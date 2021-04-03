package com.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.coopcycle.IntegrationTest;
import com.coopcycle.domain.Client;
import com.coopcycle.domain.Commande;
import com.coopcycle.domain.Livreur;
import com.coopcycle.domain.Restaurateur;
import com.coopcycle.repository.CommandeRepository;
import com.coopcycle.service.criteria.CommandeCriteria;
import com.coopcycle.service.dto.CommandeDTO;
import com.coopcycle.service.mapper.CommandeMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommandeResourceIT {

    private static final Long DEFAULT_N_COMMANDE = 1L;
    private static final Long UPDATED_N_COMMANDE = 2L;
    private static final Long SMALLER_N_COMMANDE = 1L - 1L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;
    private static final Long SMALLER_MONTANT = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private CommandeMapper commandeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandeMockMvc;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .nCommande(DEFAULT_N_COMMANDE)
            .date(DEFAULT_DATE)
            .contenu(DEFAULT_CONTENU)
            .montant(DEFAULT_MONTANT);
        return commande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande()
            .nCommande(UPDATED_N_COMMANDE)
            .date(UPDATED_DATE)
            .contenu(UPDATED_CONTENU)
            .montant(UPDATED_MONTANT);
        return commande;
    }

    @BeforeEach
    public void initTest() {
        commande = createEntity(em);
    }

    @Test
    @Transactional
    void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();
        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);
        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getnCommande()).isEqualTo(DEFAULT_N_COMMANDE);
        assertThat(testCommande.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommande.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testCommande.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createCommandeWithExistingId() throws Exception {
        // Create the Commande with an existing ID
        commande.setId(1L);
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checknCommandeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setnCommande(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setDate(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContenuIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setContenu(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().size();
        // set the field null
        commande.setMontant(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        restCommandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCommande").value(hasItem(DEFAULT_N_COMMANDE.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())));
    }

    @Test
    @Transactional
    void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.nCommande").value(DEFAULT_N_COMMANDE.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()));
    }

    @Test
    @Transactional
    void getCommandesByIdFiltering() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        Long id = commande.getId();

        defaultCommandeShouldBeFound("id.equals=" + id);
        defaultCommandeShouldNotBeFound("id.notEquals=" + id);

        defaultCommandeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommandeShouldNotBeFound("id.greaterThan=" + id);

        defaultCommandeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommandeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande equals to DEFAULT_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.equals=" + DEFAULT_N_COMMANDE);

        // Get all the commandeList where nCommande equals to UPDATED_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.equals=" + UPDATED_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande not equals to DEFAULT_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.notEquals=" + DEFAULT_N_COMMANDE);

        // Get all the commandeList where nCommande not equals to UPDATED_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.notEquals=" + UPDATED_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande in DEFAULT_N_COMMANDE or UPDATED_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.in=" + DEFAULT_N_COMMANDE + "," + UPDATED_N_COMMANDE);

        // Get all the commandeList where nCommande equals to UPDATED_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.in=" + UPDATED_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande is not null
        defaultCommandeShouldBeFound("nCommande.specified=true");

        // Get all the commandeList where nCommande is null
        defaultCommandeShouldNotBeFound("nCommande.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande is greater than or equal to DEFAULT_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.greaterThanOrEqual=" + DEFAULT_N_COMMANDE);

        // Get all the commandeList where nCommande is greater than or equal to UPDATED_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.greaterThanOrEqual=" + UPDATED_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande is less than or equal to DEFAULT_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.lessThanOrEqual=" + DEFAULT_N_COMMANDE);

        // Get all the commandeList where nCommande is less than or equal to SMALLER_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.lessThanOrEqual=" + SMALLER_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsLessThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande is less than DEFAULT_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.lessThan=" + DEFAULT_N_COMMANDE);

        // Get all the commandeList where nCommande is less than UPDATED_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.lessThan=" + UPDATED_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesBynCommandeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where nCommande is greater than DEFAULT_N_COMMANDE
        defaultCommandeShouldNotBeFound("nCommande.greaterThan=" + DEFAULT_N_COMMANDE);

        // Get all the commandeList where nCommande is greater than SMALLER_N_COMMANDE
        defaultCommandeShouldBeFound("nCommande.greaterThan=" + SMALLER_N_COMMANDE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date equals to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the commandeList where date equals to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date not equals to DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the commandeList where date not equals to UPDATED_DATE
        defaultCommandeShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCommandeShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the commandeList where date equals to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is not null
        defaultCommandeShouldBeFound("date.specified=true");

        // Get all the commandeList where date is null
        defaultCommandeShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is greater than or equal to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandeList where date is greater than or equal to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is less than or equal to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandeList where date is less than or equal to SMALLER_DATE
        defaultCommandeShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is less than DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the commandeList where date is less than UPDATED_DATE
        defaultCommandeShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is greater than DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the commandeList where date is greater than SMALLER_DATE
        defaultCommandeShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllCommandesByContenuIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where contenu equals to DEFAULT_CONTENU
        defaultCommandeShouldBeFound("contenu.equals=" + DEFAULT_CONTENU);

        // Get all the commandeList where contenu equals to UPDATED_CONTENU
        defaultCommandeShouldNotBeFound("contenu.equals=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommandesByContenuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where contenu not equals to DEFAULT_CONTENU
        defaultCommandeShouldNotBeFound("contenu.notEquals=" + DEFAULT_CONTENU);

        // Get all the commandeList where contenu not equals to UPDATED_CONTENU
        defaultCommandeShouldBeFound("contenu.notEquals=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommandesByContenuIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where contenu in DEFAULT_CONTENU or UPDATED_CONTENU
        defaultCommandeShouldBeFound("contenu.in=" + DEFAULT_CONTENU + "," + UPDATED_CONTENU);

        // Get all the commandeList where contenu equals to UPDATED_CONTENU
        defaultCommandeShouldNotBeFound("contenu.in=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommandesByContenuIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where contenu is not null
        defaultCommandeShouldBeFound("contenu.specified=true");

        // Get all the commandeList where contenu is null
        defaultCommandeShouldNotBeFound("contenu.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandesByContenuContainsSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where contenu contains DEFAULT_CONTENU
        defaultCommandeShouldBeFound("contenu.contains=" + DEFAULT_CONTENU);

        // Get all the commandeList where contenu contains UPDATED_CONTENU
        defaultCommandeShouldNotBeFound("contenu.contains=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommandesByContenuNotContainsSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where contenu does not contain DEFAULT_CONTENU
        defaultCommandeShouldNotBeFound("contenu.doesNotContain=" + DEFAULT_CONTENU);

        // Get all the commandeList where contenu does not contain UPDATED_CONTENU
        defaultCommandeShouldBeFound("contenu.doesNotContain=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant equals to DEFAULT_MONTANT
        defaultCommandeShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the commandeList where montant equals to UPDATED_MONTANT
        defaultCommandeShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant not equals to DEFAULT_MONTANT
        defaultCommandeShouldNotBeFound("montant.notEquals=" + DEFAULT_MONTANT);

        // Get all the commandeList where montant not equals to UPDATED_MONTANT
        defaultCommandeShouldBeFound("montant.notEquals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultCommandeShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the commandeList where montant equals to UPDATED_MONTANT
        defaultCommandeShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant is not null
        defaultCommandeShouldBeFound("montant.specified=true");

        // Get all the commandeList where montant is null
        defaultCommandeShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant is greater than or equal to DEFAULT_MONTANT
        defaultCommandeShouldBeFound("montant.greaterThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the commandeList where montant is greater than or equal to UPDATED_MONTANT
        defaultCommandeShouldNotBeFound("montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant is less than or equal to DEFAULT_MONTANT
        defaultCommandeShouldBeFound("montant.lessThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the commandeList where montant is less than or equal to SMALLER_MONTANT
        defaultCommandeShouldNotBeFound("montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant is less than DEFAULT_MONTANT
        defaultCommandeShouldNotBeFound("montant.lessThan=" + DEFAULT_MONTANT);

        // Get all the commandeList where montant is less than UPDATED_MONTANT
        defaultCommandeShouldBeFound("montant.lessThan=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where montant is greater than DEFAULT_MONTANT
        defaultCommandeShouldNotBeFound("montant.greaterThan=" + DEFAULT_MONTANT);

        // Get all the commandeList where montant is greater than SMALLER_MONTANT
        defaultCommandeShouldBeFound("montant.greaterThan=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllCommandesByRestaurateurIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Restaurateur restaurateur = RestaurateurResourceIT.createEntity(em);
        em.persist(restaurateur);
        em.flush();
        commande.setRestaurateur(restaurateur);
        commandeRepository.saveAndFlush(commande);
        Long restaurateurId = restaurateur.getId();

        // Get all the commandeList where restaurateur equals to restaurateurId
        defaultCommandeShouldBeFound("restaurateurId.equals=" + restaurateurId);

        // Get all the commandeList where restaurateur equals to (restaurateurId + 1)
        defaultCommandeShouldNotBeFound("restaurateurId.equals=" + (restaurateurId + 1));
    }

    @Test
    @Transactional
    void getAllCommandesByLivreurIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Livreur livreur = LivreurResourceIT.createEntity(em);
        em.persist(livreur);
        em.flush();
        commande.setLivreur(livreur);
        commandeRepository.saveAndFlush(commande);
        Long livreurId = livreur.getId();

        // Get all the commandeList where livreur equals to livreurId
        defaultCommandeShouldBeFound("livreurId.equals=" + livreurId);

        // Get all the commandeList where livreur equals to (livreurId + 1)
        defaultCommandeShouldNotBeFound("livreurId.equals=" + (livreurId + 1));
    }

    @Test
    @Transactional
    void getAllCommandesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Client client = ClientResourceIT.createEntity(em);
        em.persist(client);
        em.flush();
        commande.setClient(client);
        commandeRepository.saveAndFlush(commande);
        Long clientId = client.getId();

        // Get all the commandeList where client equals to clientId
        defaultCommandeShouldBeFound("clientId.equals=" + clientId);

        // Get all the commandeList where client equals to (clientId + 1)
        defaultCommandeShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommandeShouldBeFound(String filter) throws Exception {
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].nCommande").value(hasItem(DEFAULT_N_COMMANDE.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())));

        // Check, that the count call also returns 1
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommandeShouldNotBeFound(String filter) throws Exception {
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommandeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).get();
        // Disconnect from session so that the updates on updatedCommande are not directly saved in db
        em.detach(updatedCommande);
        updatedCommande.nCommande(UPDATED_N_COMMANDE).date(UPDATED_DATE).contenu(UPDATED_CONTENU).montant(UPDATED_MONTANT);
        CommandeDTO commandeDTO = commandeMapper.toDto(updatedCommande);

        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getnCommande()).isEqualTo(UPDATED_N_COMMANDE);
        assertThat(testCommande.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getnCommande()).isEqualTo(DEFAULT_N_COMMANDE);
        assertThat(testCommande.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.nCommande(UPDATED_N_COMMANDE).date(UPDATED_DATE).contenu(UPDATED_CONTENU).montant(UPDATED_MONTANT);

        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            )
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getnCommande()).isEqualTo(UPDATED_N_COMMANDE);
        assertThat(testCommande.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testCommande.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commandeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Delete the commande
        restCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, commande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
