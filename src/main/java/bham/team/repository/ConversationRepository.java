package bham.team.repository;

import bham.team.domain.Conversation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Conversation entity.
 *
 * When extending this class, extend ConversationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ConversationRepository extends ConversationRepositoryWithBagRelationships, JpaRepository<Conversation, Long> {
    default Optional<Conversation> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Conversation> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Conversation> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query(
        "SELECT DISTINCT c FROM Conversation c LEFT JOIN c.profileDetails pd LEFT JOIN c.participants par WHERE pd.id = :profileId OR par.id = :profileId"
    )
    List<Conversation> fetchConvosByProfile(@Param("profileId") Long profileId);

    @Query(
        """
        SELECT c
        FROM Conversation c
        JOIN c.profileDetails pd1
        JOIN c.participants  pd2
        WHERE (pd1.id = :u1 AND pd2.id = :u2)
           OR (pd1.id = :u2 AND pd2.id = :u1)
        """
    )
    Optional<Conversation> findByTwoUsers(@Param("u1") Long u1, @Param("u2") Long u2);
}
