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

    @Lob
    @Column(name = "bio_image")
    private byte[] bioImage;

    @Column(name = "bio_image_content_type")
    private String bioImageContentType;

    @NotNull
    @Pattern(regexp = "^\\S+$")
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seller")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "wishlists", "productStatus", "seller" }, allowSetters = true)
    private Set<Item> itemsOnSales = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userDetails", "items" }, allowSetters = true)
    private Set<Wishlist> wishlists = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_details__meetup_locations",
        joinColumns = @JoinColumn(name = "user_details_id"),
        inverseJoinColumns = @JoinColumn(name = "meetup_locations_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Set<Location> meetupLocations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "buyer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "buyer", "seller" }, allowSetters = true)
    private Set<Review> buyersReviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "seller")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "buyer", "seller" }, allowSetters = true)
    private Set<Review> reviewsOfSellers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_details__chats",
        joinColumns = @JoinColumn(name = "user_details_id"),
        inverseJoinColumns = @JoinColumn(name = "conversation_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productStatus", "messages" }, allowSetters = true)
    private Set<Conversation> chats = new HashSet<>();

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

    public byte[] getBioImage() {
        return this.bioImage;
    }

    public UserDetails bioImage(byte[] bioImage) {
        this.setBioImage(bioImage);
        return this;
    }

    public void setBioImage(byte[] bioImage) {
        this.bioImage = bioImage;
    }

    public String getBioImageContentType() {
        return this.bioImageContentType;
    }

    public UserDetails bioImageContentType(String bioImageContentType) {
        this.bioImageContentType = bioImageContentType;
        return this;
    }

    public void setBioImageContentType(String bioImageContentType) {
        this.bioImageContentType = bioImageContentType;
    }

    public String getUserName() {
        return this.userName;
    }

    public UserDetails userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public UserDetails birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetails user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Item> getItemsOnSales() {
        return this.itemsOnSales;
    }

    public void setItemsOnSales(Set<Item> items) {
        if (this.itemsOnSales != null) {
            this.itemsOnSales.forEach(i -> i.setSeller(null));
        }
        if (items != null) {
            items.forEach(i -> i.setSeller(this));
        }
        this.itemsOnSales = items;
    }

    public UserDetails itemsOnSales(Set<Item> items) {
        this.setItemsOnSales(items);
        return this;
    }

    public UserDetails addItemsOnSale(Item item) {
        this.itemsOnSales.add(item);
        item.setSeller(this);
        return this;
    }

    public UserDetails removeItemsOnSale(Item item) {
        this.itemsOnSales.remove(item);
        item.setSeller(null);
        return this;
    }

    public Set<Wishlist> getWishlists() {
        return this.wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        if (this.wishlists != null) {
            this.wishlists.forEach(i -> i.setUserDetails(null));
        }
        if (wishlists != null) {
            wishlists.forEach(i -> i.setUserDetails(this));
        }
        this.wishlists = wishlists;
    }

    public UserDetails wishlists(Set<Wishlist> wishlists) {
        this.setWishlists(wishlists);
        return this;
    }

    public UserDetails addWishlist(Wishlist wishlist) {
        this.wishlists.add(wishlist);
        wishlist.setUserDetails(this);
        return this;
    }

    public UserDetails removeWishlist(Wishlist wishlist) {
        this.wishlists.remove(wishlist);
        wishlist.setUserDetails(null);
        return this;
    }

    public Set<Location> getMeetupLocations() {
        return this.meetupLocations;
    }

    public void setMeetupLocations(Set<Location> locations) {
        this.meetupLocations = locations;
    }

    public UserDetails meetupLocations(Set<Location> locations) {
        this.setMeetupLocations(locations);
        return this;
    }

    public UserDetails addMeetupLocations(Location location) {
        this.meetupLocations.add(location);
        return this;
    }

    public UserDetails removeMeetupLocations(Location location) {
        this.meetupLocations.remove(location);
        return this;
    }

    public Set<Review> getBuyersReviews() {
        return this.buyersReviews;
    }

    public void setBuyersReviews(Set<Review> reviews) {
        if (this.buyersReviews != null) {
            this.buyersReviews.forEach(i -> i.setBuyer(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setBuyer(this));
        }
        this.buyersReviews = reviews;
    }

    public UserDetails buyersReviews(Set<Review> reviews) {
        this.setBuyersReviews(reviews);
        return this;
    }

    public UserDetails addBuyersReview(Review review) {
        this.buyersReviews.add(review);
        review.setBuyer(this);
        return this;
    }

    public UserDetails removeBuyersReview(Review review) {
        this.buyersReviews.remove(review);
        review.setBuyer(null);
        return this;
    }

    public Set<Review> getReviewsOfSellers() {
        return this.reviewsOfSellers;
    }

    public void setReviewsOfSellers(Set<Review> reviews) {
        if (this.reviewsOfSellers != null) {
            this.reviewsOfSellers.forEach(i -> i.setSeller(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setSeller(this));
        }
        this.reviewsOfSellers = reviews;
    }

    public UserDetails reviewsOfSellers(Set<Review> reviews) {
        this.setReviewsOfSellers(reviews);
        return this;
    }

    public UserDetails addReviewsOfSeller(Review review) {
        this.reviewsOfSellers.add(review);
        review.setSeller(this);
        return this;
    }

    public UserDetails removeReviewsOfSeller(Review review) {
        this.reviewsOfSellers.remove(review);
        review.setSeller(null);
        return this;
    }

    public Set<Conversation> getChats() {
        return this.chats;
    }

    public void setChats(Set<Conversation> conversations) {
        this.chats = conversations;
    }

    public UserDetails chats(Set<Conversation> conversations) {
        this.setChats(conversations);
        return this;
    }

    public UserDetails addChats(Conversation conversation) {
        this.chats.add(conversation);
        return this;
    }

    public UserDetails removeChats(Conversation conversation) {
        this.chats.remove(conversation);
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
            ", bioImage='" + getBioImage() + "'" +
            ", bioImageContentType='" + getBioImageContentType() + "'" +
            ", userName='" + getUserName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
