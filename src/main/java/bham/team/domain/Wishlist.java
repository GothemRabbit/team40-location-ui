package bham.team.domain;

import bham.team.domain.enumeration.VisibilityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Wishlist.
 */
@Entity
@Table(name = "wishlist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Wishlist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private VisibilityType visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "itemsOnSales", "wishlists", "meetupLocations", "buyersReviews", "reviewsOfSellers", "chats" },
        allowSetters = true
    )
    private UserDetails userDetails;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "wishlists")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "wishlists", "productStatus", "seller" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Wishlist id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Wishlist name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VisibilityType getVisibility() {
        return this.visibility;
    }

    public Wishlist visibility(VisibilityType visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public UserDetails getUserDetails() {
        return this.userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Wishlist userDetails(UserDetails userDetails) {
        this.setUserDetails(userDetails);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.removeWishlist(this));
        }
        if (items != null) {
            items.forEach(i -> i.addWishlist(this));
        }
        this.items = items;
    }

    public Wishlist items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public Wishlist addItems(Item item) {
        this.items.add(item);
        item.getWishlists().add(this);
        return this;
    }

    public Wishlist removeItems(Item item) {
        this.items.remove(item);
        item.getWishlists().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wishlist)) {
            return false;
        }
        return getId() != null && getId().equals(((Wishlist) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wishlist{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", visibility='" + getVisibility() + "'" +
            "}";
    }
}
