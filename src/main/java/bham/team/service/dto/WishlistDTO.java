package bham.team.service.dto;

import bham.team.domain.enumeration.VisibilityType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link bham.team.domain.Wishlist} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WishlistDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private VisibilityType visibility;

    private ProfileDetailsDTO profileDetails;

    private Set<ItemDTO> items = new HashSet<>();

    private UserDetailsDTO userDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public ProfileDetailsDTO getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ProfileDetailsDTO profileDetails) {
        this.profileDetails = profileDetails;
    }

    public Set<ItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<ItemDTO> items) {
        this.items = items;
    }

    public UserDetailsDTO getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsDTO userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WishlistDTO)) {
            return false;
        }

        WishlistDTO wishlistDTO = (WishlistDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, wishlistDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WishlistDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", profileDetails=" + getProfileDetails() +
            ", items=" + getItems() +
            ", userDetails=" + getUserDetails() +
            "}";
    }
}
