package bham.team.service.dto;

import bham.team.domain.enumeration.Category;
import bham.team.domain.enumeration.Condition;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link bham.team.domain.Item} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    @DecimalMin(value = "0")
    private Double price;

    @NotNull
    private Condition condition;

    @NotNull
    private Category category;

    @Lob
    private String description;

    private String brand;

    private String colour;

    @NotNull
    private Instant timeListed;

    private Set<WishlistDTO> wishlists = new HashSet<>();

    private UserDetailsDTO seller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Instant getTimeListed() {
        return timeListed;
    }

    public void setTimeListed(Instant timeListed) {
        this.timeListed = timeListed;
    }

    public Set<WishlistDTO> getWishlists() {
        return wishlists;
    }

    public void setWishlists(Set<WishlistDTO> wishlists) {
        this.wishlists = wishlists;
    }

    public UserDetailsDTO getSeller() {
        return seller;
    }

    public void setSeller(UserDetailsDTO seller) {
        this.seller = seller;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", condition='" + getCondition() + "'" +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", brand='" + getBrand() + "'" +
            ", colour='" + getColour() + "'" +
            ", timeListed='" + getTimeListed() + "'" +
            ", wishlists=" + getWishlists() +
            ", seller=" + getSeller() +
            "}";
    }
}
