package bham.team.repository;

import bham.team.domain.ProfileDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProfileDetailsRepositoryWithBagRelationships {
    Optional<ProfileDetails> fetchBagRelationships(Optional<ProfileDetails> profileDetails);

    List<ProfileDetails> fetchBagRelationships(List<ProfileDetails> profileDetails);

    Page<ProfileDetails> fetchBagRelationships(Page<ProfileDetails> profileDetails);
}
