package bham.team.service.dto;

import bham.team.domain.ProfileDetails;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link bham.team.domain.ProfileDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileDetailsDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] bioImage;

    private String bioImageContentType;

    @NotNull
    @Pattern(regexp = "^\\S+$")
    private String userName;

    private LocalDate birthDate;

    private UserDTO user;

    private Set<LocationDTO> locations = new HashSet<>();

    private Set<ConversationDTO> conversations = new HashSet<>();

    private Set<ItemDTO> items = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBioImage() {
        return bioImage;
    }

    public void setBioImage(byte[] bioImage) {
        this.bioImage = bioImage;
    }

    public String getBioImageContentType() {
        return bioImageContentType;
    }

    public void setBioImageContentType(String bioImageContentType) {
        this.bioImageContentType = bioImageContentType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(Set<LocationDTO> locations) {
        this.locations = locations;
    }

    public Set<ConversationDTO> getConversations() {
        return conversations;
    }

    public void setConversations(Set<ConversationDTO> conversations) {
        this.conversations = conversations;
    }

    public Set<ItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDetailsDTO)) {
            return false;
        }

        ProfileDetailsDTO profileDetailsDTO = (ProfileDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDetailsDTO{" +
            "id=" + getId() +
            ", bioImage='" + getBioImage() + "'" +
            ", userName='" + getUserName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", user=" + getUser() +
            ", locations=" + getLocations() +
            ", conversations=" + getConversations() +
            "}";
    }
}
