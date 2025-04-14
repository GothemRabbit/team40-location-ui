package bham.team.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bham.team.domain.Likes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikesDTO implements Serializable {

    private Long id;

    private ItemDTO item;

    private ProfileDetailsDTO profileDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public ProfileDetailsDTO getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ProfileDetailsDTO profileDetails) {
        this.profileDetails = profileDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LikesDTO)) {
            return false;
        }

        LikesDTO likesDTO = (LikesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, likesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LikesDTO{" +
            "id=" + getId() +
            ", item=" + getItem() +
            ", profileDetails=" + getProfileDetails() +
            "}";
    }
}
