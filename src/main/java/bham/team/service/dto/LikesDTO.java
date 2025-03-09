package bham.team.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bham.team.domain.Likes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LikesDTO implements Serializable {

    private Long id;

    private Boolean liked;

    private ItemDTO item;

    private UserDetailsDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public UserDetailsDTO getUser() {
        return user;
    }

    public void setUser(UserDetailsDTO user) {
        this.user = user;
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
            ", liked='" + getLiked() + "'" +
            ", item=" + getItem() +
            ", user=" + getUser() +
            "}";
    }
}
