package bham.team.domain;

import bham.team.domain.enumeration.Category;
import bham.team.domain.enumeration.Condition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "condition", nullable = false)
    private Condition condition;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "size_item")
    private String sizeItem;

    @Column(name = "brand")
    private String brand;

    @Column(name = "colour")
    private String colour;

    @NotNull
    @Column(name = "time_listed", nullable = false)
    private Instant timeListed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item" }, allowSetters = true)
    private Set<Images> images = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_item__wishlist",
        joinColumns = @JoinColumn(name = "item_id"),
        inverseJoinColumns = @JoinColumn(name = "wishlist_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "profileDetails", "items", "userDetails" }, allowSetters = true)
    private Set<Wishlist> wishlists = new HashSet<>();

    @JsonIgnoreProperties(value = { "item", "conversation", "profileDetails", "location" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_details_id", nullable = false)
    @JsonIgnoreProperties(
        value = { "user", "items", "wishlists", "locations", "likes", "reviews", "messages", "productStatuses", "conversations" },
        allowSetters = true
    )
    private ProfileDetails profileDetails;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "profileDetails" }, allowSetters = true)
    private Set<Likes> likes = new HashSet<>();

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

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Item title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return this.price;
    }

    public Item price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Condition getCondition() {
        return this.condition;
    }

    public Item condition(Condition condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Category getCategory() {
        return this.category;
    }

    public Item category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public Item description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSizeItem() {
        return this.sizeItem;
    }

    public Item sizeItem(String sizeItem) {
        this.setSizeItem(sizeItem);
        return this;
    }

    public void setSizeItem(String sizeItem) {
        this.sizeItem = sizeItem;
    }

    public String getBrand() {
        return this.brand;
    }

    public Item brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColour() {
        return this.colour;
    }

    public Item colour(String colour) {
        this.setColour(colour);
        return this;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Instant getTimeListed() {
        return this.timeListed;
    }

    public Item timeListed(Instant timeListed) {
        this.setTimeListed(timeListed);
        return this;
    }

    public void setTimeListed(Instant timeListed) {
        this.timeListed = timeListed;
    }

    public Set<Images> getImages() {
        return this.images;
    }

    public void setImages(Set<Images> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setItem(null));
        }
        if (images != null) {
            images.forEach(i -> i.setItem(this));
        }
        this.images = images;
    }

    public Item images(Set<Images> images) {
        this.setImages(images);
        return this;
    }

    public Item addImages(Images images) {
        this.images.add(images);
        images.setItem(this);
        return this;
    }

    public Item removeImages(Images images) {
        this.images.remove(images);
        images.setItem(null);
        return this;
    }

    public Set<Wishlist> getWishlists() {
        return this.wishlists;
    }

    public void setWishlists(Set<Wishlist> wishlists) {
        this.wishlists = wishlists;
    }

    public Item wishlists(Set<Wishlist> wishlists) {
        this.setWishlists(wishlists);
        return this;
    }

    public Item addWishlist(Wishlist wishlist) {
        this.wishlists.add(wishlist);
        return this;
    }

    public Item removeWishlist(Wishlist wishlist) {
        this.wishlists.remove(wishlist);
        return this;
    }

    public ProductStatus getProductStatus() {
        return this.productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        if (this.productStatus != null) {
            this.productStatus.setItem(null);
        }
        if (productStatus != null) {
            productStatus.setItem(this);
        }
        this.productStatus = productStatus;
    }

    public Item productStatus(ProductStatus productStatus) {
        this.setProductStatus(productStatus);
        return this;
    }

    public ProfileDetails getProfileDetails() {
        return this.profileDetails;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public Item profileDetails(ProfileDetails profileDetails) {
        this.setProfileDetails(profileDetails);
        return this;
    }

    public Set<Likes> getLikes() {
        return this.likes;
    }

    public void setLikes(Set<Likes> likes) {
        this.likes = likes;
    }

    public Item likes(Set<Likes> likes) {
        this.setLikes(likes);
        return this;
    }

    public Item addLikes(Likes likes) {
        this.likes.add(likes);
        likes.setItem(this);
        return this;
    }

    public Item removeLikes(Likes likes) {
        this.likes.remove(likes);
        likes.setItem(null);
        return this;
    }

    public UserDetails getSeller() {
        return this.seller;
    }

    public void setSeller(UserDetails userDetails) {
        this.seller = userDetails;
    }

    public Item seller(UserDetails userDetails) {
        this.setSeller(userDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return getId() != null && getId().equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", condition='" + getCondition() + "'" +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", sizeItem='" + getSizeItem() + "'" +
            ", brand='" + getBrand() + "'" +
            ", colour='" + getColour() + "'" +
            ", timeListed='" + getTimeListed() + "'" +
            "}";
    }
}
