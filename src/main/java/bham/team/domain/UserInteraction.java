package bham.team.domain;

import bham.team.domain.enumeration.InteractionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserInteraction.
 */
@Entity
@Table(name = "user_interaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserInteraction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InteractionType type;

    @Column(name = "details")
    private String details;

    @NotNull
    @Column(name = "interaction_date", nullable = false)
    private Instant interactionDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserInteraction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InteractionType getType() {
        return this.type;
    }

    public UserInteraction type(InteractionType type) {
        this.setType(type);
        return this;
    }

    public void setType(InteractionType type) {
        this.type = type;
    }

    public String getDetails() {
        return this.details;
    }

    public UserInteraction details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getInteractionDate() {
        return this.interactionDate;
    }

    public UserInteraction interactionDate(Instant interactionDate) {
        this.setInteractionDate(interactionDate);
        return this;
    }

    public void setInteractionDate(Instant interactionDate) {
        this.interactionDate = interactionDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInteraction)) {
            return false;
        }
        return getId() != null && getId().equals(((UserInteraction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInteraction{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", details='" + getDetails() + "'" +
            ", interactionDate='" + getInteractionDate() + "'" +
            "}";
    }
}
