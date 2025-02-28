package bham.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
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
    private ZonedDateTime dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserDetails userOne;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserDetails userTwo;

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

    public ZonedDateTime getDateCreated() {
        return this.dateCreated;
    }

    public Conversation dateCreated(ZonedDateTime dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public UserDetails getUserOne() {
        return this.userOne;
    }

    public void setUserOne(UserDetails userDetails) {
        this.userOne = userDetails;
    }

    public Conversation userOne(UserDetails userDetails) {
        this.setUserOne(userDetails);
        return this;
    }

    public UserDetails getUserTwo() {
        return this.userTwo;
    }

    public void setUserTwo(UserDetails userDetails) {
        this.userTwo = userDetails;
    }

    public Conversation userTwo(UserDetails userDetails) {
        this.setUserTwo(userDetails);
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
