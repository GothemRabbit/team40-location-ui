package bham.team.domain;

import bham.team.domain.enumeration.ProductState;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductStatus.
 */
@Entity
@Table(name = "product_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductState status;

    @Column(name = "meeting_time")
    private Instant meetingTime;

    @Column(name = "meeting_location")
    private String meetingLocation;

    @Column(name = "chat_link")
    private String chatLink;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductState getStatus() {
        return this.status;
    }

    public ProductStatus status(ProductState status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductState status) {
        this.status = status;
    }

    public Instant getMeetingTime() {
        return this.meetingTime;
    }

    public ProductStatus meetingTime(Instant meetingTime) {
        this.setMeetingTime(meetingTime);
        return this;
    }

    public void setMeetingTime(Instant meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingLocation() {
        return this.meetingLocation;
    }

    public ProductStatus meetingLocation(String meetingLocation) {
        this.setMeetingLocation(meetingLocation);
        return this;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getChatLink() {
        return this.chatLink;
    }

    public ProductStatus chatLink(String chatLink) {
        this.setChatLink(chatLink);
        return this;
    }

    public void setChatLink(String chatLink) {
        this.chatLink = chatLink;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public ProductStatus createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public ProductStatus updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductStatus)) {
            return false;
        }
        return getId() != null && getId().equals(((ProductStatus) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", meetingTime='" + getMeetingTime() + "'" +
            ", meetingLocation='" + getMeetingLocation() + "'" +
            ", chatLink='" + getChatLink() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
