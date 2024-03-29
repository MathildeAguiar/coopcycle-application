package com.coopcycle.web.rest;

import com.coopcycle.repository.RestaurateurRepository;
import com.coopcycle.service.RestaurateurQueryService;
import com.coopcycle.service.RestaurateurService;
import com.coopcycle.service.criteria.RestaurateurCriteria;
import com.coopcycle.service.dto.RestaurateurDTO;
import com.coopcycle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.coopcycle.domain.Restaurateur}.
 */
@RestController
@RequestMapping("/api")
public class RestaurateurResource {

    private final Logger log = LoggerFactory.getLogger(RestaurateurResource.class);

    private static final String ENTITY_NAME = "restaurateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurateurService restaurateurService;

    private final RestaurateurRepository restaurateurRepository;

    private final RestaurateurQueryService restaurateurQueryService;

    public RestaurateurResource(
        RestaurateurService restaurateurService,
        RestaurateurRepository restaurateurRepository,
        RestaurateurQueryService restaurateurQueryService
    ) {
        this.restaurateurService = restaurateurService;
        this.restaurateurRepository = restaurateurRepository;
        this.restaurateurQueryService = restaurateurQueryService;
    }

    /**
     * {@code POST  /restaurateurs} : Create a new restaurateur.
     *
     * @param restaurateurDTO the restaurateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurateurDTO, or with status {@code 400 (Bad Request)} if the restaurateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurateurs")
    public ResponseEntity<RestaurateurDTO> createRestaurateur(@Valid @RequestBody RestaurateurDTO restaurateurDTO)
        throws URISyntaxException {
        log.debug("REST request to save Restaurateur : {}", restaurateurDTO);
        if (restaurateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RestaurateurDTO result = restaurateurService.save(restaurateurDTO);
        return ResponseEntity
            .created(new URI("/api/restaurateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurateurs/:id} : Updates an existing restaurateur.
     *
     * @param id the id of the restaurateurDTO to save.
     * @param restaurateurDTO the restaurateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurateurDTO,
     * or with status {@code 400 (Bad Request)} if the restaurateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurateurs/{id}")
    public ResponseEntity<RestaurateurDTO> updateRestaurateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RestaurateurDTO restaurateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Restaurateur : {}, {}", id, restaurateurDTO);
        if (restaurateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RestaurateurDTO result = restaurateurService.save(restaurateurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurateurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurateurs/:id} : Partial updates given fields of an existing restaurateur, field will ignore if it is null
     *
     * @param id the id of the restaurateurDTO to save.
     * @param restaurateurDTO the restaurateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurateurDTO,
     * or with status {@code 400 (Bad Request)} if the restaurateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the restaurateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurateurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RestaurateurDTO> partialUpdateRestaurateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RestaurateurDTO restaurateurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaurateur partially : {}, {}", id, restaurateurDTO);
        if (restaurateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurateurDTO> result = restaurateurService.partialUpdate(restaurateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurateurs} : get all the restaurateurs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurateurs in body.
     */
    @GetMapping("/restaurateurs")
    public ResponseEntity<List<RestaurateurDTO>> getAllRestaurateurs(RestaurateurCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Restaurateurs by criteria: {}", criteria);
        Page<RestaurateurDTO> page = restaurateurQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaurateurs/count} : count all the restaurateurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/restaurateurs/count")
    public ResponseEntity<Long> countRestaurateurs(RestaurateurCriteria criteria) {
        log.debug("REST request to count Restaurateurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(restaurateurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /restaurateurs/:id} : get the "id" restaurateur.
     *
     * @param id the id of the restaurateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurateurs/{id}")
    public ResponseEntity<RestaurateurDTO> getRestaurateur(@PathVariable Long id) {
        log.debug("REST request to get Restaurateur : {}", id);
        Optional<RestaurateurDTO> restaurateurDTO = restaurateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurateurDTO);
    }

    /**
     * {@code DELETE  /restaurateurs/:id} : delete the "id" restaurateur.
     *
     * @param id the id of the restaurateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurateurs/{id}")
    public ResponseEntity<Void> deleteRestaurateur(@PathVariable Long id) {
        log.debug("REST request to delete Restaurateur : {}", id);
        restaurateurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
