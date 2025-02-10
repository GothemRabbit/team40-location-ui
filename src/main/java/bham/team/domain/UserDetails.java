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
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "profile_pic")
    private byte[] profilePic;

    @Column(name = "profile_pic_content_type")
    private String profilePicContentType;

    @NotNull
    @Column(name = "last_active", nullable = false)
    private Instant lastActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userDetails", "conversation" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userDetails", "messages" }, allowSetters = true)
    private Set<Conversation> conversations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public UserDetails firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserDetails lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public UserDetails email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getProfilePic() {
        return this.profilePic;
    }

    public UserDetails profilePic(byte[] profilePic) {
        this.setProfilePic(profilePic);
        return this;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicContentType() {
        return this.profilePicContentType;
    }

    public UserDetails profilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
        return this;
    }

    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }

    public Instant getLastActive() {
        return this.lastActive;
    }

    public UserDetails lastActive(Instant lastActive) {
        this.setLastActive(lastActive);
        return this;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setUserDetails(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setUserDetails(this));
        }
        this.messages = messages;
    }

    public UserDetails messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public UserDetails addMessage(Message message) {
        this.messages.add(message);
        message.setUserDetails(this);
        return this;
    }

    public UserDetails removeMessage(Message message) {
        this.messages.remove(message);
        message.setUserDetails(null);
        return this;
    }

    public Set<Conversation> getConversations() {
        return this.conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        if (this.conversations != null) {
            this.conversations.forEach(i -> i.removeUserDetails(this));
        }
        if (conversations != null) {
            conversations.forEach(i -> i.addUserDetails(this));
        }
        this.conversations = conversations;
    }

    public UserDetails conversations(Set<Conversation> conversations) {
        this.setConversations(conversations);
        return this;
    }

    public UserDetails addConversation(Conversation conversation) {
        this.conversations.add(conversation);
        conversation.getUserDetails().add(this);
        return this;
    }

    public UserDetails removeConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        conversation.getUserDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((UserDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", profilePic='" + getProfilePic() + "'" +
            ", profilePicContentType='" + getProfilePicContentType() + "'" +
            ", lastActive='" + getLastActive() + "'" +
            "}";
    }
}
