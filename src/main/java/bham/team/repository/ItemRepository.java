package bham.team.repository;

import bham.team.domain.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Item entity.
 *
 * When extending this class, extend ItemRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ItemRepository extends ItemRepositoryWithBagRelationships, JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    default Optional<Item> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Item> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Item> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query("select item from Item item left join fetch item.likes where item.id =:id")
    Optional<Item> findOneWithLikes(@Param("id") Long id);

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.images WHERE i.id = :id")
    Optional<Item> findByIdWithImages(@Param("id") Long id);
}
