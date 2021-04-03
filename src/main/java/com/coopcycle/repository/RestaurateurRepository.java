package com.coopcycle.repository;

import com.coopcycle.domain.Restaurateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RestaurateurRepository extends JpaRepository<Restaurateur, Long>, JpaSpecificationExecutor<Restaurateur> {}
