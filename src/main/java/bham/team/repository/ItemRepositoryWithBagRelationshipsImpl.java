package bham.team.repository;

import bham.team.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ItemRepositoryWithBagRelationshipsImpl implements ItemRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ITEMS_PARAMETER = "items";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Item> fetchBagRelationships(Optional<Item> item) {
        return item.map(this::fetchWishlists);
    }

    @Override
    public Page<Item> fetchBagRelationships(Page<Item> items) {
        return new PageImpl<>(fetchBagRelationships(items.getContent()), items.getPageable(), items.getTotalElements());
    }

    @Override
    public List<Item> fetchBagRelationships(List<Item> items) {
        return Optional.of(items).map(this::fetchWishlists).orElse(Collections.emptyList());
    }

    Item fetchWishlists(Item result) {
        return entityManager
            .createQuery("select item from Item item left join fetch item.wishlists where item.id = :id", Item.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Item> fetchWishlists(List<Item> items) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, items.size()).forEach(index -> order.put(items.get(index).getId(), index));
        List<Item> result = entityManager
            .createQuery("select item from Item item left join fetch item.wishlists where item in :items", Item.class)
            .setParameter(ITEMS_PARAMETER, items)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
