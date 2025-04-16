package bham.team.repository;

import bham.team.domain.Likes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Likes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    // Get all likes for an item
    @Query("SELECT l FROM Likes l WHERE l.item.id = :itemId")
    List<Likes> findAllByItemId(@Param("itemId") Long itemId);

    // Count total likes for an item
    @Query("SELECT COUNT(l) FROM Likes l WHERE l.item.id = :itemId")
    int countLikesByItemId(@Param("itemId") Long itemId);

    // Check if a user has liked a specific item
    @Query("SELECT COUNT(l) > 0 FROM Likes l WHERE l.item.id = :itemId AND l.profileDetails.id = :profileId")
    boolean existsByItemIdAndProfileId(@Param("itemId") Long itemId, @Param("profileId") Long profileId);

    @Query("SELECT l FROM Likes l WHERE l.item.id = :itemId AND l.profileDetails.id = :profileId")
    Optional<Likes> findByItemIdAndProfileId(@Param("itemId") Long itemId, @Param("profileId") Long profileId);

    Optional<Likes> findByItemIdAndProfileDetailsId(Long itemId, Long profileId);
    int countByItemId(Long itemId);
    void deleteByItemIdAndProfileDetailsId(Long itemId, Long profileId);
}
