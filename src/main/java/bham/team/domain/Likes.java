package bham.team.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Likes.
 */
@Entity
@Table(name = "likes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Likes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "liked")
    private Boolean liked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "wishlists", "productStatus", "seller" }, allowSetters = true)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "itemsOnSales", "wishlists", "meetupLocations", "buyersReviews", "reviewsOfSellers", "chats" },
        allowSetters = true
    )
    private UserDetails user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Likes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return this.liked;
    }

    public Likes liked(Boolean liked) {
        this.setLiked(liked);
        return this;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Likes item(Item item) {
        this.setItem(item);
        return this;
    }

    public UserDetails getUser() {
        return this.user;
    }

    public void setUser(UserDetails userDetails) {
        this.user = userDetails;
    }

    public Likes user(UserDetails userDetails) {
        this.setUser(userDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Likes)) {
            return false;
        }
        return getId() != null && getId().equals(((Likes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Likes{" +
            "id=" + getId() +
            ", liked='" + getLiked() + "'" +
            "}";
    }
}
