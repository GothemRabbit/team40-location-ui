package bham.team.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserSearchHistory.
 */
@Entity
@Table(name = "user_search_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSearchHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "search_query", nullable = false)
    private String searchQuery;

    @Column(name = "filters")
    private String filters;

    @NotNull
    @Column(name = "search_date", nullable = false)
    private Instant searchDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSearchHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchQuery() {
        return this.searchQuery;
    }

    public UserSearchHistory searchQuery(String searchQuery) {
        this.setSearchQuery(searchQuery);
        return this;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getFilters() {
        return this.filters;
    }

    public UserSearchHistory filters(String filters) {
        this.setFilters(filters);
        return this;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Instant getSearchDate() {
        return this.searchDate;
    }

    public UserSearchHistory searchDate(Instant searchDate) {
        this.setSearchDate(searchDate);
        return this;
    }

    public void setSearchDate(Instant searchDate) {
        this.searchDate = searchDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSearchHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSearchHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSearchHistory{" +
            "id=" + getId() +
            ", searchQuery='" + getSearchQuery() + "'" +
            ", filters='" + getFilters() + "'" +
            ", searchDate='" + getSearchDate() + "'" +
            "}";
    }
}
