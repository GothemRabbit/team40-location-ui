package bham.team.repository;

import bham.team.domain.UserDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserDetailsRepositoryWithBagRelationships {
    Optional<UserDetails> fetchBagRelationships(Optional<UserDetails> userDetails);

    List<UserDetails> fetchBagRelationships(List<UserDetails> userDetails);

    Page<UserDetails> fetchBagRelationships(Page<UserDetails> userDetails);
}
