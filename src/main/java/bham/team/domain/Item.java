package bham.team.domain;

import bham.team.domain.enumeration.Category;
import bham.team.domain.enumeration.Condition;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
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
    @Column(name = "item_title", nullable = false)
    private String itemTitle;

    @NotNull
    @Column(name = "item_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal itemPrice;

    @Column(name = "item_size")
    private String itemSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_condition", nullable = false)
    private Condition itemCondition;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_category", nullable = false)
    private Category itemCategory;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "item_colour")
    private String itemColour;

    @Lob
    @Column(name = "item_image", nullable = false)
    private byte[] itemImage;

    @NotNull
    @Column(name = "item_image_content_type", nullable = false)
    private String itemImageContentType;

    @NotNull
    @Column(name = "time_listed", nullable = false)
    private Instant timeListed;

    @Column(name = "item_like")
    private Boolean itemLike;

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

    public String getItemTitle() {
        return this.itemTitle;
    }

    public Item itemTitle(String itemTitle) {
        this.setItemTitle(itemTitle);
        return this;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public BigDecimal getItemPrice() {
        return this.itemPrice;
    }

    public Item itemPrice(BigDecimal itemPrice) {
        this.setItemPrice(itemPrice);
        return this;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemSize() {
        return this.itemSize;
    }

    public Item itemSize(String itemSize) {
        this.setItemSize(itemSize);
        return this;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public Condition getItemCondition() {
        return this.itemCondition;
    }

    public Item itemCondition(Condition itemCondition) {
        this.setItemCondition(itemCondition);
        return this;
    }

    public void setItemCondition(Condition itemCondition) {
        this.itemCondition = itemCondition;
    }

    public Category getItemCategory() {
        return this.itemCategory;
    }

    public Item itemCategory(Category itemCategory) {
        this.setItemCategory(itemCategory);
        return this;
    }

    public void setItemCategory(Category itemCategory) {
        this.itemCategory = itemCategory;
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

    public String getItemColour() {
        return this.itemColour;
    }

    public Item itemColour(String itemColour) {
        this.setItemColour(itemColour);
        return this;
    }

    public void setItemColour(String itemColour) {
        this.itemColour = itemColour;
    }

    public byte[] getItemImage() {
        return this.itemImage;
    }

    public Item itemImage(byte[] itemImage) {
        this.setItemImage(itemImage);
        return this;
    }

    public void setItemImage(byte[] itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemImageContentType() {
        return this.itemImageContentType;
    }

    public Item itemImageContentType(String itemImageContentType) {
        this.itemImageContentType = itemImageContentType;
        return this;
    }

    public void setItemImageContentType(String itemImageContentType) {
        this.itemImageContentType = itemImageContentType;
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

    public Boolean getItemLike() {
        return this.itemLike;
    }

    public Item itemLike(Boolean itemLike) {
        this.setItemLike(itemLike);
        return this;
    }

    public void setItemLike(Boolean itemLike) {
        this.itemLike = itemLike;
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
            ", itemTitle='" + getItemTitle() + "'" +
            ", itemPrice=" + getItemPrice() +
            ", itemSize='" + getItemSize() + "'" +
            ", itemCondition='" + getItemCondition() + "'" +
            ", itemCategory='" + getItemCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", itemColour='" + getItemColour() + "'" +
            ", itemImage='" + getItemImage() + "'" +
            ", itemImageContentType='" + getItemImageContentType() + "'" +
            ", timeListed='" + getTimeListed() + "'" +
            ", itemLike='" + getItemLike() + "'" +
            "}";
    }
}
