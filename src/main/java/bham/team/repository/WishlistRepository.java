package bham.team.repository;

import bham.team.domain.Wishlist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Wishlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {}
