package bham.team.web.rest;

import static bham.team.domain.UserRecommendationAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.UserRecommendation;
import bham.team.repository.UserRecommendationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link UserRecommendationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRecommendationResourceIT {

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-recommendations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRecommendationRepository userRecommendationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRecommendationMockMvc;

    private UserRecommendation userRecommendation;

    private UserRecommendation insertedUserRecommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRecommendation createEntity() {
        return new UserRecommendation().reason(DEFAULT_REASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRecommendation createUpdatedEntity() {
        return new UserRecommendation().reason(UPDATED_REASON);
    }

    @BeforeEach
    public void initTest() {
        userRecommendation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserRecommendation != null) {
            userRecommendationRepository.delete(insertedUserRecommendation);
            insertedUserRecommendation = null;
        }
    }

    @Test
    @Transactional
    void createUserRecommendation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserRecommendation
        var returnedUserRecommendation = om.readValue(
            restUserRecommendationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRecommendation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserRecommendation.class
        );

        // Validate the UserRecommendation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserRecommendationUpdatableFieldsEquals(
            returnedUserRecommendation,
            getPersistedUserRecommendation(returnedUserRecommendation)
        );

        insertedUserRecommendation = returnedUserRecommendation;
    }

    @Test
    @Transactional
    void createUserRecommendationWithExistingId() throws Exception {
        // Create the UserRecommendation with an existing ID
        userRecommendation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRecommendationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRecommendation)))
            .andExpect(status().isBadRequest());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserRecommendations() throws Exception {
        // Initialize the database
        insertedUserRecommendation = userRecommendationRepository.saveAndFlush(userRecommendation);

        // Get all the userRecommendationList
        restUserRecommendationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRecommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)));
    }

    @Test
    @Transactional
    void getUserRecommendation() throws Exception {
        // Initialize the database
        insertedUserRecommendation = userRecommendationRepository.saveAndFlush(userRecommendation);

        // Get the userRecommendation
        restUserRecommendationMockMvc
            .perform(get(ENTITY_API_URL_ID, userRecommendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRecommendation.getId().intValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON));
    }

    @Test
    @Transactional
    void getNonExistingUserRecommendation() throws Exception {
        // Get the userRecommendation
        restUserRecommendationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRecommendation() throws Exception {
        // Initialize the database
        insertedUserRecommendation = userRecommendationRepository.saveAndFlush(userRecommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRecommendation
        UserRecommendation updatedUserRecommendation = userRecommendationRepository.findById(userRecommendation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserRecommendation are not directly saved in db
        em.detach(updatedUserRecommendation);
        updatedUserRecommendation.reason(UPDATED_REASON);

        restUserRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserRecommendation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserRecommendationToMatchAllProperties(updatedUserRecommendation);
    }

    @Test
    @Transactional
    void putNonExistingUserRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRecommendation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRecommendation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRecommendation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRecommendation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecommendationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userRecommendation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRecommendation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecommendationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userRecommendation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedUserRecommendation = userRecommendationRepository.saveAndFlush(userRecommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRecommendation using partial update
        UserRecommendation partialUpdatedUserRecommendation = new UserRecommendation();
        partialUpdatedUserRecommendation.setId(userRecommendation.getId());

        restUserRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the UserRecommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRecommendationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserRecommendation, userRecommendation),
            getPersistedUserRecommendation(userRecommendation)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserRecommendationWithPatch() throws Exception {
        // Initialize the database
        insertedUserRecommendation = userRecommendationRepository.saveAndFlush(userRecommendation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userRecommendation using partial update
        UserRecommendation partialUpdatedUserRecommendation = new UserRecommendation();
        partialUpdatedUserRecommendation.setId(userRecommendation.getId());

        partialUpdatedUserRecommendation.reason(UPDATED_REASON);

        restUserRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserRecommendation))
            )
            .andExpect(status().isOk());

        // Validate the UserRecommendation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserRecommendationUpdatableFieldsEquals(
            partialUpdatedUserRecommendation,
            getPersistedUserRecommendation(partialUpdatedUserRecommendation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRecommendation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRecommendation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRecommendation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRecommendation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecommendationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userRecommendation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRecommendation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userRecommendation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRecommendationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userRecommendation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRecommendation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRecommendation() throws Exception {
        // Initialize the database
        insertedUserRecommendation = userRecommendationRepository.saveAndFlush(userRecommendation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userRecommendation
        restUserRecommendationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRecommendation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userRecommendationRepository.count();
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

    protected UserRecommendation getPersistedUserRecommendation(UserRecommendation userRecommendation) {
        return userRecommendationRepository.findById(userRecommendation.getId()).orElseThrow();
    }

    protected void assertPersistedUserRecommendationToMatchAllProperties(UserRecommendation expectedUserRecommendation) {
        assertUserRecommendationAllPropertiesEquals(expectedUserRecommendation, getPersistedUserRecommendation(expectedUserRecommendation));
    }

    protected void assertPersistedUserRecommendationToMatchUpdatableProperties(UserRecommendation expectedUserRecommendation) {
        assertUserRecommendationAllUpdatablePropertiesEquals(
            expectedUserRecommendation,
            getPersistedUserRecommendation(expectedUserRecommendation)
        );
    }
}
