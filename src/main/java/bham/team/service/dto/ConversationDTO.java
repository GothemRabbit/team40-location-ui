package bham.team.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link bham.team.domain.Conversation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConversationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateCreated;

    private Set<ProfileDetailsDTO> profileDetails = new HashSet<>();

    private Set<UserDetailsDTO> participants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<ProfileDetailsDTO> getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(Set<ProfileDetailsDTO> profileDetails) {
        this.profileDetails = profileDetails;
    }

    public Set<UserDetailsDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserDetailsDTO> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConversationDTO)) {
            return false;
        }

        ConversationDTO conversationDTO = (ConversationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, conversationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConversationDTO{" +
            "id=" + getId() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", profileDetails=" + getProfileDetails() +
            ", participants=" + getParticipants() +
            "}";
    }
}
