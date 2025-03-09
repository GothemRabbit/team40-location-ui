package bham.team.service.dto;

import bham.team.domain.enumeration.ProductState;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link bham.team.domain.ProductStatus} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductStatusDTO implements Serializable {

    private Long id;

    @NotNull
    private ProductState status;

    private Instant meetingTime;

    private Instant updatedAt;

    private ZonedDateTime createdAt;

    private ItemDTO item;

    private ConversationDTO conversation;

    private UserDetailsDTO buyer;

    private UserDetailsDTO seller;

    private LocationDTO meetingLocation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductState getStatus() {
        return status;
    }

    public void setStatus(ProductState status) {
        this.status = status;
    }

    public Instant getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Instant meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public ConversationDTO getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDTO conversation) {
        this.conversation = conversation;
    }

    public UserDetailsDTO getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDetailsDTO buyer) {
        this.buyer = buyer;
    }

    public UserDetailsDTO getSeller() {
        return seller;
    }

    public void setSeller(UserDetailsDTO seller) {
        this.seller = seller;
    }

    public LocationDTO getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(LocationDTO meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductStatusDTO)) {
            return false;
        }

        ProductStatusDTO productStatusDTO = (ProductStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductStatusDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", meetingTime='" + getMeetingTime() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", item=" + getItem() +
            ", conversation=" + getConversation() +
            ", buyer=" + getBuyer() +
            ", seller=" + getSeller() +
            ", meetingLocation=" + getMeetingLocation() +
            "}";
    }
}
