package bham.team.repository;

import bham.team.domain.UserSearchHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSearchHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSearchHistoryRepository extends JpaRepository<UserSearchHistory, Long> {}
