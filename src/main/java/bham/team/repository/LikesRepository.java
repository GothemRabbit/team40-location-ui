package bham.team.repository;

import bham.team.domain.Likes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Likes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {}
