package com.coopcycle.service;

import com.coopcycle.service.dto.RestaurateurDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.coopcycle.domain.Restaurateur}.
 */
public interface RestaurateurService {
    /**
     * Save a restaurateur.
     *
     * @param restaurateurDTO the entity to save.
     * @return the persisted entity.
     */
    RestaurateurDTO save(RestaurateurDTO restaurateurDTO);

    /**
     * Partially updates a restaurateur.
     *
     * @param restaurateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RestaurateurDTO> partialUpdate(RestaurateurDTO restaurateurDTO);

    /**
     * Get all the restaurateurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RestaurateurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" restaurateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RestaurateurDTO> findOne(Long id);

    /**
     * Delete the "id" restaurateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
