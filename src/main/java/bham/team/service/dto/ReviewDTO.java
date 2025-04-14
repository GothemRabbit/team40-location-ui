package bham.team.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link bham.team.domain.Review} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReviewDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer rating;

    @Lob
    private String comments;

    private LocalDate date;

    private ProfileDetailsDTO consumer;

    private ProfileDetailsDTO retailer;

    private UserDetailsDTO buyer;

    private UserDetailsDTO seller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ProfileDetailsDTO getConsumer() {
        return consumer;
    }

    public void setConsumer(ProfileDetailsDTO consumer) {
        this.consumer = consumer;
    }

    public ProfileDetailsDTO getRetailer() {
        return retailer;
    }

    public void setRetailer(ProfileDetailsDTO retailer) {
        this.retailer = retailer;
    }

    public UserDetailsDTO getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDetailsDTO buyer) {
        this.buyer = buyer;
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
        if (!(o instanceof ReviewDTO)) {
            return false;
        }

        ReviewDTO reviewDTO = (ReviewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reviewDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewDTO{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", comments='" + getComments() + "'" +
            ", date='" + getDate() + "'" +
            ", consumer=" + getConsumer() +
            ", retailer=" + getRetailer() +
            ", buyer=" + getBuyer() +
            ", seller=" + getSeller() +
            "}";
    }
}
