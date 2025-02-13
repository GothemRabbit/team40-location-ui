package bham.team.web.rest;

import static bham.team.domain.UserDetailsAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.UserDetails;
import bham.team.domain.enumeration.Gender;
import bham.team.repository.UserDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDetailsResourceIT {

    private static final byte[] DEFAULT_BIO_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BIO_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BIO_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BIO_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCES = "BBBBBBBBBB";

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDetailsMockMvc;

    private UserDetails userDetails;

    private UserDetails insertedUserDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createEntity() {
        return new UserDetails()
            .bioImage(DEFAULT_BIO_IMAGE)
            .bioImageContentType(DEFAULT_BIO_IMAGE_CONTENT_TYPE)
            .userName(DEFAULT_USER_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .preferences(DEFAULT_PREFERENCES)
            .rating(DEFAULT_RATING)
            .address(DEFAULT_ADDRESS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetails createUpdatedEntity() {
        return new UserDetails()
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .preferences(UPDATED_PREFERENCES)
            .rating(UPDATED_RATING)
            .address(UPDATED_ADDRESS);
    }

    @BeforeEach
    public void initTest() {
        userDetails = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserDetails != null) {
            userDetailsRepository.delete(insertedUserDetails);
            insertedUserDetails = null;
        }
    }

    @Test
    @Transactional
    void createUserDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserDetails
        var returnedUserDetails = om.readValue(
            restUserDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDetails.class
        );

        // Validate the UserDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserDetailsUpdatableFieldsEquals(returnedUserDetails, getPersistedUserDetails(returnedUserDetails));

        insertedUserDetails = returnedUserDetails;
    }

    @Test
    @Transactional
    void createUserDetailsWithExistingId() throws Exception {
        // Create the UserDetails with an existing ID
        userDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setUserName(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setFirstName(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setLastName(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setEmail(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setPhoneNumber(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setRating(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userDetails.setAddress(null);

        // Create the UserDetails, which fails.

        restUserDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserDetails() throws Exception {
        // Initialize the database
        insertedUserDetails = userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].bioImageContentType").value(hasItem(DEFAULT_BIO_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bioImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BIO_IMAGE))))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].preferences").value(hasItem(DEFAULT_PREFERENCES)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getUserDetails() throws Exception {
        // Initialize the database
        insertedUserDetails = userDetailsRepository.saveAndFlush(userDetails);

        // Get the userDetails
        restUserDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDetails.getId().intValue()))
            .andExpect(jsonPath("$.bioImageContentType").value(DEFAULT_BIO_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.bioImage").value(Base64.getEncoder().encodeToString(DEFAULT_BIO_IMAGE)))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.preferences").value(DEFAULT_PREFERENCES))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingUserDetails() throws Exception {
        // Get the userDetails
        restUserDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserDetails() throws Exception {
        // Initialize the database
        insertedUserDetails = userDetailsRepository.saveAndFlush(userDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findById(userDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserDetails are not directly saved in db
        em.detach(updatedUserDetails);
        updatedUserDetails
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .preferences(UPDATED_PREFERENCES)
            .rating(UPDATED_RATING)
            .address(UPDATED_ADDRESS);

        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserDetailsToMatchAllProperties(updatedUserDetails);
    }

    @Test
    @Transactional
    void putNonExistingUserDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedUserDetails = userDetailsRepository.saveAndFlush(userDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDetails using partial update
        UserDetails partialUpdatedUserDetails = new UserDetails();
        partialUpdatedUserDetails.setId(userDetails.getId());

        partialUpdatedUserDetails
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserDetails, userDetails),
            getPersistedUserDetails(userDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedUserDetails = userDetailsRepository.saveAndFlush(userDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDetails using partial update
        UserDetails partialUpdatedUserDetails = new UserDetails();
        partialUpdatedUserDetails.setId(userDetails.getId());

        partialUpdatedUserDetails
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .userName(UPDATED_USER_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .preferences(UPDATED_PREFERENCES)
            .rating(UPDATED_RATING)
            .address(UPDATED_ADDRESS);

        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDetails))
            )
            .andExpect(status().isOk());

        // Validate the UserDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDetailsUpdatableFieldsEquals(partialUpdatedUserDetails, getPersistedUserDetails(partialUpdatedUserDetails));
    }

    @Test
    @Transactional
    void patchNonExistingUserDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserDetails() throws Exception {
        // Initialize the database
        insertedUserDetails = userDetailsRepository.saveAndFlush(userDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userDetails
        restUserDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userDetailsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UserDetails getPersistedUserDetails(UserDetails userDetails) {
        return userDetailsRepository.findById(userDetails.getId()).orElseThrow();
    }

    protected void assertPersistedUserDetailsToMatchAllProperties(UserDetails expectedUserDetails) {
        assertUserDetailsAllPropertiesEquals(expectedUserDetails, getPersistedUserDetails(expectedUserDetails));
    }

    protected void assertPersistedUserDetailsToMatchUpdatableProperties(UserDetails expectedUserDetails) {
        assertUserDetailsAllUpdatablePropertiesEquals(expectedUserDetails, getPersistedUserDetails(expectedUserDetails));
    }
}
