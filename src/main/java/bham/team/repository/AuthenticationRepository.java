package bham.team.repository;

import bham.team.domain.Authentication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authentication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {}
