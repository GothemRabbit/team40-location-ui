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
 * A ProfileDetails.
 */
@Entity
@Table(name = "profile_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProfileDetails implements Serializable {

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "wishlists", "productStatus", "profileDetails", "likes", "seller" }, allowSetters = true)
    private Set<Item> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "profileDetails", "items", "userDetails" }, allowSetters = true)
    private Set<Wishlist> wishlists = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_profile_details__location",
        joinColumns = @JoinColumn(name = "profile_details_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "productStatuses", "profileDetails", "users" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "profileDetails" }, allowSetters = true)
    private Set<Likes> likes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "profileDetails", "buyer", "seller" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "conversation", "profileDetails" }, allowSetters = true)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "conversation", "profileDetails", "location" }, allowSetters = true)
    private Set<ProductStatus> productStatuses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "profileDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "profileDetails", "productStatus", "messages", "participants" }, allowSetters = true)
    private Set<Conversation> conversations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProfileDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBioImage() {
        return this.bioImage;
    }

    public ProfileDetails bioImage(byte[] bioImage) {
        this.setBioImage(bioImage);
        return this;
    }

    public void setBioImage(byte[] bioImage) {
        this.bioImage = bioImage;
    }

    public String getBioImageContentType() {
        return this.bioImageContentType;
    }

    public ProfileDetails bioImageContentType(String bioImageContentType) {
        this.bioImageContentType = bioImageContentType;
        return this;
    }

    public void setBioImageContentType(String bioImageContentType) {
        this.bioImageContentType = bioImageContentType;
    }

    public String getUserName() {
        return this.userName;
    }

    public ProfileDetails userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public ProfileDetails birthDate(LocalDate birthDate) {
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

    public ProfileDetails user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        if (this.items != null) {
            this.items.forEach(i -> i.setProfileDetails(null));
        }
        if (items != null) {
            items.forEach(i -> i.setProfileDetails(this));
        }
        this.items = items;
    }

    public ProfileDetails items(Set<Item> items) {
        this.setItems(items);
        return this;
    }

    public ProfileDetails addItem(Item item) {
        this.items.add(item);
        item.setProfileDetails(this);
        return this;
    }

    public ProfileDetails removeItem(Item item) {
        this.items.remove(item);
        item.setProfileDetails(null);
        return this;
    }

    public Set<Wishlist> getWishlists() {
        return this.wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        if (this.wishlists != null) {
            this.wishlists.forEach(i -> i.setProfileDetails(null));
        }
        if (wishlists != null) {
            wishlists.forEach(i -> i.setProfileDetails(this));
        }
        this.wishlists = wishlists;
    }

    public ProfileDetails wishlists(Set<Wishlist> wishlists) {
        this.setWishlists(wishlists);
        return this;
    }

    public ProfileDetails addWishlist(Wishlist wishlist) {
        this.wishlists.add(wishlist);
        wishlist.setProfileDetails(this);
        return this;
    }

    public ProfileDetails removeWishlist(Wishlist wishlist) {
        this.wishlists.remove(wishlist);
        wishlist.setProfileDetails(null);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public ProfileDetails locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public ProfileDetails addLocation(Location location) {
        this.locations.add(location);
        return this;
    }

    public ProfileDetails removeLocation(Location location) {
        this.locations.remove(location);
        return this;
    }

    public Set<Likes> getLikes() {
        return this.likes;
    }

    public void setLikes(Set<Likes> likes) {
        if (this.likes != null) {
            this.likes.forEach(i -> i.setProfileDetails(null));
        }
        if (likes != null) {
            likes.forEach(i -> i.setProfileDetails(this));
        }
        this.likes = likes;
    }

    public ProfileDetails likes(Set<Likes> likes) {
        this.setLikes(likes);
        return this;
    }

    public ProfileDetails addLikes(Likes likes) {
        this.likes.add(likes);
        likes.setProfileDetails(this);
        return this;
    }

    public ProfileDetails removeLikes(Likes likes) {
        this.likes.remove(likes);
        likes.setProfileDetails(null);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setProfileDetails(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setProfileDetails(this));
        }
        this.reviews = reviews;
    }

    public ProfileDetails reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public ProfileDetails addReview(Review review) {
        this.reviews.add(review);
        review.setProfileDetails(this);
        return this;
    }

    public ProfileDetails removeReview(Review review) {
        this.reviews.remove(review);
        review.setProfileDetails(null);
        return this;
    }

    public Set<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Message> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setProfileDetails(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setProfileDetails(this));
        }
        this.messages = messages;
    }

    public ProfileDetails messages(Set<Message> messages) {
        this.setMessages(messages);
        return this;
    }

    public ProfileDetails addMessage(Message message) {
        this.messages.add(message);
        message.setProfileDetails(this);
        return this;
    }

    public ProfileDetails removeMessage(Message message) {
        this.messages.remove(message);
        message.setProfileDetails(null);
        return this;
    }

    public Set<ProductStatus> getProductStatuses() {
        return this.productStatuses;
    }

    public void setProductStatuses(Set<ProductStatus> productStatuses) {
        if (this.productStatuses != null) {
            this.productStatuses.forEach(i -> i.setProfileDetails(null));
        }
        if (productStatuses != null) {
            productStatuses.forEach(i -> i.setProfileDetails(this));
        }
        this.productStatuses = productStatuses;
    }

    public ProfileDetails productStatuses(Set<ProductStatus> productStatuses) {
        this.setProductStatuses(productStatuses);
        return this;
    }

    public ProfileDetails addProductStatus(ProductStatus productStatus) {
        this.productStatuses.add(productStatus);
        productStatus.setProfileDetails(this);
        return this;
    }

    public ProfileDetails removeProductStatus(ProductStatus productStatus) {
        this.productStatuses.remove(productStatus);
        productStatus.setProfileDetails(null);
        return this;
    }

    public Set<Conversation> getConversations() {
        return this.conversations;
    }

    public void setConversations(Set<Conversation> conversations) {
        if (this.conversations != null) {
            this.conversations.forEach(i -> i.removeProfileDetails(this));
        }
        if (conversations != null) {
            conversations.forEach(i -> i.addProfileDetails(this));
        }
        this.conversations = conversations;
    }

    public ProfileDetails conversations(Set<Conversation> conversations) {
        this.setConversations(conversations);
        return this;
    }

    public ProfileDetails addConversation(Conversation conversation) {
        this.conversations.add(conversation);
        conversation.getProfileDetails().add(this);
        return this;
    }

    public ProfileDetails removeConversation(Conversation conversation) {
        this.conversations.remove(conversation);
        conversation.getProfileDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfileDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDetails{" +
            "id=" + getId() +
            ", bioImage='" + getBioImage() + "'" +
            ", bioImageContentType='" + getBioImageContentType() + "'" +
            ", userName='" + getUserName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            "}";
    }
}
