package bham.team.repository;

import bham.team.domain.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Review entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT review from Review review WHERE review.retailer.id =: retailerId")
    List<Review> findReviewByRetailerId(@Param("retailerId") Long retailerId);

    @Query("SELECT review from Review review LEFT JOIN FETCH review.retailer LEFT JOIN FETCH review.consumer where review.id = :id")
    Optional<Review> findReviewByIdWithConsumerAndRetailer(@Param("id") Long id);
}
