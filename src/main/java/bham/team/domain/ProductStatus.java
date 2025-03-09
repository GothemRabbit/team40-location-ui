package bham.team.domain;

import bham.team.domain.enumeration.ProductState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @JsonIgnoreProperties(value = { "images", "wishlists", "productStatus", "profileDetails", "likes", "seller" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Item item;

    @JsonIgnoreProperties(value = { "profileDetails", "productStatus", "messages", "participants" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "items", "wishlists", "locations", "likes", "reviews", "messages", "productStatuses", "conversations" },
        allowSetters = true
    )
    private ProfileDetails profileDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "productStatuses", "profileDetails", "users" }, allowSetters = true)
    private Location location;

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

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public ProductStatus updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
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

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ProductStatus item(Item item) {
        this.setItem(item);
        return this;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public ProductStatus conversation(Conversation conversation) {
        this.setConversation(conversation);
        return this;
    }

    public ProfileDetails getProfileDetails() {
        return this.profileDetails;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public ProductStatus profileDetails(ProfileDetails profileDetails) {
        this.setProfileDetails(profileDetails);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ProductStatus location(Location location) {
        this.setLocation(location);
        return this;
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
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
