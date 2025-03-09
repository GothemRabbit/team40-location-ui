package bham.team.service.criteria;

import bham.team.domain.enumeration.Category;
import bham.team.domain.enumeration.Condition;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link bham.team.domain.Item} entity. This class is used
 * in {@link bham.team.web.rest.ItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Condition
     */
    public static class ConditionFilter extends Filter<Condition> {

        public ConditionFilter() {}

        public ConditionFilter(ConditionFilter filter) {
            super(filter);
        }

        @Override
        public ConditionFilter copy() {
            return new ConditionFilter(this);
        }
    }

    /**
     * Class for filtering Category
     */
    public static class CategoryFilter extends Filter<Category> {

        public CategoryFilter() {}

        public CategoryFilter(CategoryFilter filter) {
            super(filter);
        }

        @Override
        public CategoryFilter copy() {
            return new CategoryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private DoubleFilter price;

    private ConditionFilter condition;

    private CategoryFilter category;

    private StringFilter brand;

    private StringFilter colour;

    private InstantFilter timeListed;

    private LongFilter imagesId;

    private LongFilter wishlistId;

    private LongFilter productStatusId;

    private LongFilter profileDetailsId;

    private LongFilter likesId;

    private LongFilter sellerId;

    private Boolean distinct;

    public ItemCriteria() {}

    public ItemCriteria(ItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(DoubleFilter::copy).orElse(null);
        this.condition = other.optionalCondition().map(ConditionFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(CategoryFilter::copy).orElse(null);
        this.brand = other.optionalBrand().map(StringFilter::copy).orElse(null);
        this.colour = other.optionalColour().map(StringFilter::copy).orElse(null);
        this.timeListed = other.optionalTimeListed().map(InstantFilter::copy).orElse(null);
        this.imagesId = other.optionalImagesId().map(LongFilter::copy).orElse(null);
        this.wishlistId = other.optionalWishlistId().map(LongFilter::copy).orElse(null);
        this.productStatusId = other.optionalProductStatusId().map(LongFilter::copy).orElse(null);
        this.profileDetailsId = other.optionalProfileDetailsId().map(LongFilter::copy).orElse(null);
        this.likesId = other.optionalLikesId().map(LongFilter::copy).orElse(null);
        this.sellerId = other.optionalSellerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ItemCriteria copy() {
        return new ItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public Optional<DoubleFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public DoubleFilter price() {
        if (price == null) {
            setPrice(new DoubleFilter());
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public ConditionFilter getCondition() {
        return condition;
    }

    public Optional<ConditionFilter> optionalCondition() {
        return Optional.ofNullable(condition);
    }

    public ConditionFilter condition() {
        if (condition == null) {
            setCondition(new ConditionFilter());
        }
        return condition;
    }

    public void setCondition(ConditionFilter condition) {
        this.condition = condition;
    }

    public CategoryFilter getCategory() {
        return category;
    }

    public Optional<CategoryFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public CategoryFilter category() {
        if (category == null) {
            setCategory(new CategoryFilter());
        }
        return category;
    }

    public void setCategory(CategoryFilter category) {
        this.category = category;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public Optional<StringFilter> optionalBrand() {
        return Optional.ofNullable(brand);
    }

    public StringFilter brand() {
        if (brand == null) {
            setBrand(new StringFilter());
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getColour() {
        return colour;
    }

    public Optional<StringFilter> optionalColour() {
        return Optional.ofNullable(colour);
    }

    public StringFilter colour() {
        if (colour == null) {
            setColour(new StringFilter());
        }
        return colour;
    }

    public void setColour(StringFilter colour) {
        this.colour = colour;
    }

    public InstantFilter getTimeListed() {
        return timeListed;
    }

    public Optional<InstantFilter> optionalTimeListed() {
        return Optional.ofNullable(timeListed);
    }

    public InstantFilter timeListed() {
        if (timeListed == null) {
            setTimeListed(new InstantFilter());
        }
        return timeListed;
    }

    public void setTimeListed(InstantFilter timeListed) {
        this.timeListed = timeListed;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public Optional<LongFilter> optionalImagesId() {
        return Optional.ofNullable(imagesId);
    }

    public LongFilter imagesId() {
        if (imagesId == null) {
            setImagesId(new LongFilter());
        }
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public LongFilter getWishlistId() {
        return wishlistId;
    }

    public Optional<LongFilter> optionalWishlistId() {
        return Optional.ofNullable(wishlistId);
    }

    public LongFilter wishlistId() {
        if (wishlistId == null) {
            setWishlistId(new LongFilter());
        }
        return wishlistId;
    }

    public void setWishlistId(LongFilter wishlistId) {
        this.wishlistId = wishlistId;
    }

    public LongFilter getProductStatusId() {
        return productStatusId;
    }

    public Optional<LongFilter> optionalProductStatusId() {
        return Optional.ofNullable(productStatusId);
    }

    public LongFilter productStatusId() {
        if (productStatusId == null) {
            setProductStatusId(new LongFilter());
        }
        return productStatusId;
    }

    public void setProductStatusId(LongFilter productStatusId) {
        this.productStatusId = productStatusId;
    }

    public LongFilter getProfileDetailsId() {
        return profileDetailsId;
    }

    public Optional<LongFilter> optionalProfileDetailsId() {
        return Optional.ofNullable(profileDetailsId);
    }

    public LongFilter profileDetailsId() {
        if (profileDetailsId == null) {
            setProfileDetailsId(new LongFilter());
        }
        return profileDetailsId;
    }

    public void setProfileDetailsId(LongFilter profileDetailsId) {
        this.profileDetailsId = profileDetailsId;
    }

    public LongFilter getLikesId() {
        return likesId;
    }

    public Optional<LongFilter> optionalLikesId() {
        return Optional.ofNullable(likesId);
    }

    public LongFilter likesId() {
        if (likesId == null) {
            setLikesId(new LongFilter());
        }
        return likesId;
    }

    public void setLikesId(LongFilter likesId) {
        this.likesId = likesId;
    }

    public LongFilter getSellerId() {
        return sellerId;
    }

    public Optional<LongFilter> optionalSellerId() {
        return Optional.ofNullable(sellerId);
    }

    public LongFilter sellerId() {
        if (sellerId == null) {
            setSellerId(new LongFilter());
        }
        return sellerId;
    }

    public void setSellerId(LongFilter sellerId) {
        this.sellerId = sellerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ItemCriteria that = (ItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(price, that.price) &&
            Objects.equals(condition, that.condition) &&
            Objects.equals(category, that.category) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(colour, that.colour) &&
            Objects.equals(timeListed, that.timeListed) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(wishlistId, that.wishlistId) &&
            Objects.equals(productStatusId, that.productStatusId) &&
            Objects.equals(profileDetailsId, that.profileDetailsId) &&
            Objects.equals(likesId, that.likesId) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            price,
            condition,
            category,
            brand,
            colour,
            timeListed,
            imagesId,
            wishlistId,
            productStatusId,
            profileDetailsId,
            likesId,
            sellerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalCondition().map(f -> "condition=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalBrand().map(f -> "brand=" + f + ", ").orElse("") +
            optionalColour().map(f -> "colour=" + f + ", ").orElse("") +
            optionalTimeListed().map(f -> "timeListed=" + f + ", ").orElse("") +
            optionalImagesId().map(f -> "imagesId=" + f + ", ").orElse("") +
            optionalWishlistId().map(f -> "wishlistId=" + f + ", ").orElse("") +
            optionalProductStatusId().map(f -> "productStatusId=" + f + ", ").orElse("") +
            optionalProfileDetailsId().map(f -> "profileDetailsId=" + f + ", ").orElse("") +
            optionalLikesId().map(f -> "likesId=" + f + ", ").orElse("") +
            optionalSellerId().map(f -> "sellerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
