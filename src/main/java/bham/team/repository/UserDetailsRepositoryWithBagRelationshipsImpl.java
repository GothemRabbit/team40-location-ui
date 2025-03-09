package bham.team.repository;

import bham.team.domain.UserDetails;
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
public class UserDetailsRepositoryWithBagRelationshipsImpl implements UserDetailsRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERDETAILS_PARAMETER = "userDetails";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserDetails> fetchBagRelationships(Optional<UserDetails> userDetails) {
        return userDetails.map(this::fetchMeetupLocations);
    }

    @Override
    public Page<UserDetails> fetchBagRelationships(Page<UserDetails> userDetails) {
        return new PageImpl<>(fetchBagRelationships(userDetails.getContent()), userDetails.getPageable(), userDetails.getTotalElements());
    }

    @Override
    public List<UserDetails> fetchBagRelationships(List<UserDetails> userDetails) {
        return Optional.of(userDetails).map(this::fetchMeetupLocations).orElse(Collections.emptyList());
    }

    UserDetails fetchMeetupLocations(UserDetails result) {
        return entityManager
            .createQuery(
                "select userDetails from UserDetails userDetails left join fetch userDetails.meetupLocations where userDetails.id = :id",
                UserDetails.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserDetails> fetchMeetupLocations(List<UserDetails> userDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userDetails.size()).forEach(index -> order.put(userDetails.get(index).getId(), index));
        List<UserDetails> result = entityManager
            .createQuery(
                "select userDetails from UserDetails userDetails left join fetch userDetails.meetupLocations where userDetails in :userDetails",
                UserDetails.class
            )
            .setParameter(USERDETAILS_PARAMETER, userDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
