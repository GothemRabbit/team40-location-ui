package bham.team.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conversation.
 */
@Entity
@Table(name = "conversation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_conversation__profile_details",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "profile_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "user", "items", "wishlists", "locations", "likes", "reviews", "messages", "productStatuses", "conversations" },
        allowSetters = true
    )
    private Set<ProfileDetails> profileDetails = new HashSet<>();

    @JsonIgnoreProperties(value = { "item", "conversation", "profileDetails", "location" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "conversation")
    private ProductStatus productStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversation", "profileDetails" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_conversation__participants",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "participants_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "user", "items", "wishlists", "locations", "likes", "reviews", "messages", "productStatuses", "conversations" },
        allowSetters = true
    )
    private Set<ProfileDetails> participants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conversation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Conversation dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<ProfileDetails> getProfileDetails() {
        return this.profileDetails;
    }

    public void setProfileDetails(Set<ProfileDetails> profileDetails) {
        this.profileDetails = profileDetails;
    }

    public Conversation profileDetails(Set<ProfileDetails> profileDetails) {
        this.setProfileDetails(profileDetails);
        return this;
    }

    public Conversation addProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails.add(profileDetails);
        return this;
    }

    public Conversation removeProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails.remove(profileDetails);
        return this;
    }

    public ProductStatus getProductStatus() {
        return this.productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        if (this.productStatus != null) {
            this.productStatus.setConversation(null);
        }
        if (productStatus != null) {
            productStatus.setConversation(this);
        }
        this.productStatus = productStatus;
    }

    public Conversation productStatus(ProductStatus productStatus) {
        this.setProductStatus(productStatus);
        return this;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setConversation(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setConversation(this));
        }
        this.messages = messages;
    }

    public Conversation messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public Conversation addMessage(Message message) {
        this.messages.add(message);
        message.setConversation(this);
        return this;
    }

    public Conversation removeMessage(Message message) {
        this.messages.remove(message);
        message.setConversation(null);
        return this;
    }

    public Set<ProfileDetails> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<ProfileDetails> profileDetails) {
        this.participants = profileDetails;
    }

    public Conversation participants(Set<ProfileDetails> profileDetails) {
        this.setParticipants(profileDetails);
        return this;
    }

    public Conversation addParticipants(ProfileDetails profileDetails) {
        this.participants.add(profileDetails);
        return this;
    }

    public Conversation removeParticipants(ProfileDetails profileDetails) {
        this.participants.remove(profileDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversation)) {
            return false;
        }
        return getId() != null && getId().equals(((Conversation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conversation{" +
            "id=" + getId() +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
