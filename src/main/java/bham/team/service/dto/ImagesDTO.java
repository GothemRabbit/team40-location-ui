package bham.team.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link bham.team.domain.Images} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesDTO implements Serializable {

    private Long id;

    @Lob
    private byte[] images;

    private String imagesContentType;

    private ItemDTO item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImages() {
        return images;
    }

    public void setImages(byte[] images) {
        this.images = images;
    }

    public String getImagesContentType() {
        return imagesContentType;
    }

    public void setImagesContentType(String imagesContentType) {
        this.imagesContentType = imagesContentType;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagesDTO)) {
            return false;
        }

        ImagesDTO imagesDTO = (ImagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesDTO{" +
            "id=" + getId() +
            ", images='" + getImages() + "'" +
            ", item=" + getItem() +
            "}";
    }
}
