package bham.team.repository;

import bham.team.domain.ProductStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {}
