package bham.team.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Column(name = "postcode", nullable = false)
    private String postcode;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "meetupLocations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "user", "itemsOnSales", "wishlists", "meetupLocations", "buyersReviews", "reviewsOfSellers", "chats" },
        allowSetters = true
    )
    private Set<UserDetails> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public Location address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Location latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Location longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public Location postcode(String postcode) {
        this.setPostcode(postcode);
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public Set<UserDetails> getUsers() {
        return this.users;
    }

    public void setUsers(Set<UserDetails> userDetails) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeMeetupLocations(this));
        }
        if (userDetails != null) {
            userDetails.forEach(i -> i.addMeetupLocations(this));
        }
        this.users = userDetails;
    }

    public Location users(Set<UserDetails> userDetails) {
        this.setUsers(userDetails);
        return this;
    }

    public Location addUsers(UserDetails userDetails) {
        this.users.add(userDetails);
        userDetails.getMeetupLocations().add(this);
        return this;
    }

    public Location removeUsers(UserDetails userDetails) {
        this.users.remove(userDetails);
        userDetails.getMeetupLocations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return getId() != null && getId().equals(((Location) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", postcode='" + getPostcode() + "'" +
            "}";
    }
}
