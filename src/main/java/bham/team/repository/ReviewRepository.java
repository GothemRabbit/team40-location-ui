package bham.team.repository;

import bham.team.domain.ProfileDetails;
import bham.team.domain.Review;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
    List<Review> findReviewByRetailerId(@Param("profileDetailId") Long profileDetailId);

    Boolean existsReviewByConsumerAndRetailerAndDate(ProfileDetails consumer, ProfileDetails retailer, @NotNull LocalDate date);
    Boolean existsReviewByConsumerAndDate(ProfileDetails consumer, @NotNull LocalDate date);
}
