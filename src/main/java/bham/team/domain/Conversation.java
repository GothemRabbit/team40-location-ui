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
        name = "rel_conversation__participants",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "participants_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "user", "itemsOnSales", "wishlists", "meetupLocations", "buyersReviews", "reviewsOfSellers", "chats" },
        allowSetters = true
    )
    private Set<UserDetails> participants = new HashSet<>();

    @JsonIgnoreProperties(value = { "item", "conversation", "buyer", "seller", "meetingLocation" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "conversation")
    private ProductStatus productStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "convo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "convo", "sender" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

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

    public Set<UserDetails> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<UserDetails> userDetails) {
        this.participants = userDetails;
    }

    public Conversation participants(Set<UserDetails> userDetails) {
        this.setParticipants(userDetails);
        return this;
    }

    public Conversation addParticipants(UserDetails userDetails) {
        this.participants.add(userDetails);
        return this;
    }

    public Conversation removeParticipants(UserDetails userDetails) {
        this.participants.remove(userDetails);
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
            this.messages.forEach(i -> i.setConvo(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setConvo(this));
        }
        this.messages = messages;
    }

    public Conversation messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public Conversation addMessages(Message message) {
        this.messages.add(message);
        message.setConvo(this);
        return this;
    }

    public Conversation removeMessages(Message message) {
        this.messages.remove(message);
        message.setConvo(null);
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
