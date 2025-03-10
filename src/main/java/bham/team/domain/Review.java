package bham.team.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Review.
 */
@Entity
@Table(name = "review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "comments", nullable = false)
    private String comments;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "items", "wishlists", "locations", "likes", "reviews", "messages", "productStatuses", "conversations" },
        allowSetters = true
    )
    private ProfileDetails profileDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "itemsOnSales", "wishlists", "meetupLocations", "buyersReviews", "reviewsOfSellers", "chats" },
        allowSetters = true
    )
    private UserDetails buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "itemsOnSales", "wishlists", "meetupLocations", "buyersReviews", "reviewsOfSellers", "chats" },
        allowSetters = true
    )
    private UserDetails seller;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Review id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Review rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return this.comments;
    }

    public Review comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Review date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ProfileDetails getProfileDetails() {
        return this.profileDetails;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public Review profileDetails(ProfileDetails profileDetails) {
        this.setProfileDetails(profileDetails);
        return this;
    }

    public UserDetails getBuyer() {
        return this.buyer;
    }

    public void setBuyer(UserDetails userDetails) {
        this.buyer = userDetails;
    }

    public Review buyer(UserDetails userDetails) {
        this.setBuyer(userDetails);
        return this;
    }

    public UserDetails getSeller() {
        return this.seller;
    }

    public void setSeller(UserDetails userDetails) {
        this.seller = userDetails;
    }

    public Review seller(UserDetails userDetails) {
        this.setSeller(userDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return getId() != null && getId().equals(((Review) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comments='" + getComments() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
