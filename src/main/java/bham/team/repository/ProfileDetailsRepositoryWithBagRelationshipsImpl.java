package bham.team.repository;

import bham.team.domain.ProfileDetails;
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
public class ProfileDetailsRepositoryWithBagRelationshipsImpl implements ProfileDetailsRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PROFILEDETAILS_PARAMETER = "profileDetails";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<ProfileDetails> fetchBagRelationships(Optional<ProfileDetails> profileDetails) {
        return profileDetails.map(this::fetchLocations);
    }

    @Override
    public Page<ProfileDetails> fetchBagRelationships(Page<ProfileDetails> profileDetails) {
        return new PageImpl<>(
            fetchBagRelationships(profileDetails.getContent()),
            profileDetails.getPageable(),
            profileDetails.getTotalElements()
        );
    }

    @Override
    public List<ProfileDetails> fetchBagRelationships(List<ProfileDetails> profileDetails) {
        return Optional.of(profileDetails).map(this::fetchLocations).orElse(Collections.emptyList());
    }

    ProfileDetails fetchLocations(ProfileDetails result) {
        return entityManager
            .createQuery(
                "select profileDetails from ProfileDetails profileDetails left join fetch profileDetails.locations where profileDetails.id = :id",
                ProfileDetails.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<ProfileDetails> fetchLocations(List<ProfileDetails> profileDetails) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, profileDetails.size()).forEach(index -> order.put(profileDetails.get(index).getId(), index));
        List<ProfileDetails> result = entityManager
            .createQuery(
                "select profileDetails from ProfileDetails profileDetails left join fetch profileDetails.locations where profileDetails in :profileDetails",
                ProfileDetails.class
            )
            .setParameter(PROFILEDETAILS_PARAMETER, profileDetails)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
