package com.coopcycle.service;

import com.coopcycle.domain.*; // for static metamodels
import com.coopcycle.domain.Restaurateur;
import com.coopcycle.repository.RestaurateurRepository;
import com.coopcycle.service.criteria.RestaurateurCriteria;
import com.coopcycle.service.dto.RestaurateurDTO;
import com.coopcycle.service.mapper.RestaurateurMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Restaurateur} entities in the database.
 * The main input is a {@link RestaurateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RestaurateurDTO} or a {@link Page} of {@link RestaurateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RestaurateurQueryService extends QueryService<Restaurateur> {

    private final Logger log = LoggerFactory.getLogger(RestaurateurQueryService.class);

    private final RestaurateurRepository restaurateurRepository;

    private final RestaurateurMapper restaurateurMapper;

    public RestaurateurQueryService(RestaurateurRepository restaurateurRepository, RestaurateurMapper restaurateurMapper) {
        this.restaurateurRepository = restaurateurRepository;
        this.restaurateurMapper = restaurateurMapper;
    }

    /**
     * Return a {@link List} of {@link RestaurateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RestaurateurDTO> findByCriteria(RestaurateurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Restaurateur> specification = createSpecification(criteria);
        return restaurateurMapper.toDto(restaurateurRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RestaurateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RestaurateurDTO> findByCriteria(RestaurateurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Restaurateur> specification = createSpecification(criteria);
        return restaurateurRepository.findAll(specification, page).map(restaurateurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RestaurateurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Restaurateur> specification = createSpecification(criteria);
        return restaurateurRepository.count(specification);
    }

    /**
     * Function to convert {@link RestaurateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Restaurateur> createSpecification(RestaurateurCriteria criteria) {
        Specification<Restaurateur> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Restaurateur_.id));
            }
            if (criteria.getnRestaurant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getnRestaurant(), Restaurateur_.nRestaurant));
            }
            if (criteria.getAdresseRestaurant() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAdresseRestaurant(), Restaurateur_.adresseRestaurant));
            }
            if (criteria.getNomRestaurant() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomRestaurant(), Restaurateur_.nomRestaurant));
            }
            if (criteria.getCommandeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommandeId(),
                            root -> root.join(Restaurateur_.commandes, JoinType.LEFT).get(Commande_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
