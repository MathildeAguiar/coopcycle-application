package com.coopcycle.repository;

import com.coopcycle.domain.Livreur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Livreur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long>, JpaSpecificationExecutor<Livreur> {}
