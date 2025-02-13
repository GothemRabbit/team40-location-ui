package bham.team.domain;

import bham.team.domain.enumeration.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
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
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "preferences")
    private String preferences;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "5")
    @Column(name = "rating", nullable = false)
    private Float rating;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

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

    public String getFirstName() {
        return this.firstName;
    }

    public UserDetails firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public UserDetails lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public UserDetails gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public String getEmail() {
        return this.email;
    }

    public UserDetails email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public UserDetails phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPreferences() {
        return this.preferences;
    }

    public UserDetails preferences(String preferences) {
        this.setPreferences(preferences);
        return this;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Float getRating() {
        return this.rating;
    }

    public UserDetails rating(Float rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return this.address;
    }

    public UserDetails address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
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
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", preferences='" + getPreferences() + "'" +
            ", rating=" + getRating() +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
