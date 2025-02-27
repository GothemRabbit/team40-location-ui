package bham.team.repository;

import bham.team.domain.UserRecommendation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserRecommendation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRecommendationRepository extends JpaRepository<UserRecommendation, Long> {}
