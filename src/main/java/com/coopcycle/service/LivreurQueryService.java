package com.coopcycle.service;

import com.coopcycle.domain.*; // for static metamodels
import com.coopcycle.domain.Livreur;
import com.coopcycle.repository.LivreurRepository;
import com.coopcycle.service.criteria.LivreurCriteria;
import com.coopcycle.service.dto.LivreurDTO;
import com.coopcycle.service.mapper.LivreurMapper;
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
 * Service for executing complex queries for {@link Livreur} entities in the database.
 * The main input is a {@link LivreurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LivreurDTO} or a {@link Page} of {@link LivreurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LivreurQueryService extends QueryService<Livreur> {

    private final Logger log = LoggerFactory.getLogger(LivreurQueryService.class);

    private final LivreurRepository livreurRepository;

    private final LivreurMapper livreurMapper;

    public LivreurQueryService(LivreurRepository livreurRepository, LivreurMapper livreurMapper) {
        this.livreurRepository = livreurRepository;
        this.livreurMapper = livreurMapper;
    }

    /**
     * Return a {@link List} of {@link LivreurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LivreurDTO> findByCriteria(LivreurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Livreur> specification = createSpecification(criteria);
        return livreurMapper.toDto(livreurRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LivreurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LivreurDTO> findByCriteria(LivreurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Livreur> specification = createSpecification(criteria);
        return livreurRepository.findAll(specification, page).map(livreurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LivreurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Livreur> specification = createSpecification(criteria);
        return livreurRepository.count(specification);
    }

    /**
     * Function to convert {@link LivreurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Livreur> createSpecification(LivreurCriteria criteria) {
        Specification<Livreur> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Livreur_.id));
            }
            if (criteria.getNumLivreur() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumLivreur(), Livreur_.numLivreur));
            }
            if (criteria.getNomLivreur() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomLivreur(), Livreur_.nomLivreur));
            }
            if (criteria.getPositionGPS() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPositionGPS(), Livreur_.positionGPS));
            }
            if (criteria.getCommandeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommandeId(), root -> root.join(Livreur_.commandes, JoinType.LEFT).get(Commande_.id))
                    );
            }
        }
        return specification;
    }
}
