package bham.team.repository;

import bham.team.domain.UserInteraction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserInteraction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {}
