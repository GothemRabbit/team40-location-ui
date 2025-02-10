package bham.team.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate dateCreated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_conversation__user_details",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "user_details_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messages", "conversations" }, allowSetters = true)
    private Set<UserDetails> userDetails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userDetails", "conversation" }, allowSetters = true)
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

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public Conversation dateCreated(LocalDate dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<UserDetails> getUserDetails() {
        return this.userDetails;
    }

    public void setUserDetails(Set<UserDetails> userDetails) {
        this.userDetails = userDetails;
    }

    public Conversation userDetails(Set<UserDetails> userDetails) {
        this.setUserDetails(userDetails);
        return this;
    }

    public Conversation addUserDetails(UserDetails userDetails) {
        this.userDetails.add(userDetails);
        return this;
    }

    public Conversation removeUserDetails(UserDetails userDetails) {
        this.userDetails.remove(userDetails);
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
