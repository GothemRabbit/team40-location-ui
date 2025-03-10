package bham.team.web.rest;

import static bham.team.domain.ProfileDetailsAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.ProfileDetails;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.repository.UserRepository;
import bham.team.service.ProfileDetailsService;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.mapper.ProfileDetailsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProfileDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfileDetailsResourceIT {

    private static final byte[] DEFAULT_BIO_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BIO_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BIO_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BIO_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/profile-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfileDetailsRepository profileDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ProfileDetailsRepository profileDetailsRepositoryMock;

    @Autowired
    private ProfileDetailsMapper profileDetailsMapper;

    @Mock
    private ProfileDetailsService profileDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileDetailsMockMvc;

    private ProfileDetails profileDetails;

    private ProfileDetails insertedProfileDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfileDetails createEntity() {
        return new ProfileDetails()
            .bioImage(DEFAULT_BIO_IMAGE)
            .bioImageContentType(DEFAULT_BIO_IMAGE_CONTENT_TYPE)
            .userName(DEFAULT_USER_NAME)
            .birthDate(DEFAULT_BIRTH_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfileDetails createUpdatedEntity() {
        return new ProfileDetails()
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .userName(UPDATED_USER_NAME)
            .birthDate(UPDATED_BIRTH_DATE);
    }

    @BeforeEach
    public void initTest() {
        profileDetails = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProfileDetails != null) {
            profileDetailsRepository.delete(insertedProfileDetails);
            insertedProfileDetails = null;
        }
    }

    @Test
    @Transactional
    void createProfileDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);
        var returnedProfileDetailsDTO = om.readValue(
            restProfileDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDetailsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfileDetailsDTO.class
        );

        // Validate the ProfileDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfileDetails = profileDetailsMapper.toEntity(returnedProfileDetailsDTO);
        assertProfileDetailsUpdatableFieldsEquals(returnedProfileDetails, getPersistedProfileDetails(returnedProfileDetails));

        insertedProfileDetails = returnedProfileDetails;
    }

    @Test
    @Transactional
    void createProfileDetailsWithExistingId() throws Exception {
        // Create the ProfileDetails with an existing ID
        profileDetails.setId(1L);
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profileDetails.setUserName(null);

        // Create the ProfileDetails, which fails.
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        restProfileDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDetailsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfileDetails() throws Exception {
        // Initialize the database
        insertedProfileDetails = profileDetailsRepository.saveAndFlush(profileDetails);

        // Get all the profileDetailsList
        restProfileDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profileDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].bioImageContentType").value(hasItem(DEFAULT_BIO_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].bioImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BIO_IMAGE))))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfileDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(profileDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfileDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(profileDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfileDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(profileDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfileDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(profileDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfileDetails() throws Exception {
        // Initialize the database
        insertedProfileDetails = profileDetailsRepository.saveAndFlush(profileDetails);

        // Get the profileDetails
        restProfileDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, profileDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profileDetails.getId().intValue()))
            .andExpect(jsonPath("$.bioImageContentType").value(DEFAULT_BIO_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.bioImage").value(Base64.getEncoder().encodeToString(DEFAULT_BIO_IMAGE)))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProfileDetails() throws Exception {
        // Get the profileDetails
        restProfileDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfileDetails() throws Exception {
        // Initialize the database
        insertedProfileDetails = profileDetailsRepository.saveAndFlush(profileDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profileDetails
        ProfileDetails updatedProfileDetails = profileDetailsRepository.findById(profileDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfileDetails are not directly saved in db
        em.detach(updatedProfileDetails);
        updatedProfileDetails
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .userName(UPDATED_USER_NAME)
            .birthDate(UPDATED_BIRTH_DATE);
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(updatedProfileDetails);

        restProfileDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profileDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfileDetailsToMatchAllProperties(updatedProfileDetails);
    }

    @Test
    @Transactional
    void putNonExistingProfileDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDetails.setId(longCount.incrementAndGet());

        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profileDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profileDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfileDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDetails.setId(longCount.incrementAndGet());

        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profileDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfileDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDetails.setId(longCount.incrementAndGet());

        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profileDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfileDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedProfileDetails = profileDetailsRepository.saveAndFlush(profileDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profileDetails using partial update
        ProfileDetails partialUpdatedProfileDetails = new ProfileDetails();
        partialUpdatedProfileDetails.setId(profileDetails.getId());

        partialUpdatedProfileDetails
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .birthDate(UPDATED_BIRTH_DATE);

        restProfileDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfileDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfileDetails))
            )
            .andExpect(status().isOk());

        // Validate the ProfileDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfileDetails, profileDetails),
            getPersistedProfileDetails(profileDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfileDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedProfileDetails = profileDetailsRepository.saveAndFlush(profileDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profileDetails using partial update
        ProfileDetails partialUpdatedProfileDetails = new ProfileDetails();
        partialUpdatedProfileDetails.setId(profileDetails.getId());

        partialUpdatedProfileDetails
            .bioImage(UPDATED_BIO_IMAGE)
            .bioImageContentType(UPDATED_BIO_IMAGE_CONTENT_TYPE)
            .userName(UPDATED_USER_NAME)
            .birthDate(UPDATED_BIRTH_DATE);

        restProfileDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfileDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfileDetails))
            )
            .andExpect(status().isOk());

        // Validate the ProfileDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfileDetailsUpdatableFieldsEquals(partialUpdatedProfileDetails, getPersistedProfileDetails(partialUpdatedProfileDetails));
    }

    @Test
    @Transactional
    void patchNonExistingProfileDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDetails.setId(longCount.incrementAndGet());

        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profileDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profileDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfileDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDetails.setId(longCount.incrementAndGet());

        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profileDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfileDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profileDetails.setId(longCount.incrementAndGet());

        // Create the ProfileDetails
        ProfileDetailsDTO profileDetailsDTO = profileDetailsMapper.toDto(profileDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfileDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profileDetailsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfileDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfileDetails() throws Exception {
        // Initialize the database
        insertedProfileDetails = profileDetailsRepository.saveAndFlush(profileDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profileDetails
        restProfileDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, profileDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profileDetailsRepository.count();
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

    protected ProfileDetails getPersistedProfileDetails(ProfileDetails profileDetails) {
        return profileDetailsRepository.findById(profileDetails.getId()).orElseThrow();
    }

    protected void assertPersistedProfileDetailsToMatchAllProperties(ProfileDetails expectedProfileDetails) {
        assertProfileDetailsAllPropertiesEquals(expectedProfileDetails, getPersistedProfileDetails(expectedProfileDetails));
    }

    protected void assertPersistedProfileDetailsToMatchUpdatableProperties(ProfileDetails expectedProfileDetails) {
        assertProfileDetailsAllUpdatablePropertiesEquals(expectedProfileDetails, getPersistedProfileDetails(expectedProfileDetails));
    }
}
