package bham.team.repository;

import bham.team.domain.ProductStatus;
import bham.team.domain.enumeration.ProductState;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
    Page<ProductStatus> findByProfileDetailsId(Long profileDetailsId, Pageable pageable);

    Optional<ProductStatus> findByIdAndProfileDetailsId(Long id, Long profileDetailsId);

    @Modifying
    @Transactional
    int deleteByIdAndProfileDetailsId(Long id, Long profileDetailsId);

    Optional<ProductStatus> findByItemIdAndStatus(Long itemId, ProductState productState);
}
