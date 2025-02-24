package bham.team.web.rest;

import static bham.team.domain.UserInteractionAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.UserInteraction;
import bham.team.domain.enumeration.InteractionType;
import bham.team.repository.UserInteractionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserInteractionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserInteractionResourceIT {

    private static final InteractionType DEFAULT_TYPE = InteractionType.SEARCH;
    private static final InteractionType UPDATED_TYPE = InteractionType.RECOMMENDATION;

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_INTERACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INTERACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-interactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserInteractionRepository userInteractionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserInteractionMockMvc;

    private UserInteraction userInteraction;

    private UserInteraction insertedUserInteraction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInteraction createEntity() {
        return new UserInteraction().type(DEFAULT_TYPE).details(DEFAULT_DETAILS).interactionDate(DEFAULT_INTERACTION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInteraction createUpdatedEntity() {
        return new UserInteraction().type(UPDATED_TYPE).details(UPDATED_DETAILS).interactionDate(UPDATED_INTERACTION_DATE);
    }

    @BeforeEach
    public void initTest() {
        userInteraction = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserInteraction != null) {
            userInteractionRepository.delete(insertedUserInteraction);
            insertedUserInteraction = null;
        }
    }

    @Test
    @Transactional
    void createUserInteraction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserInteraction
        var returnedUserInteraction = om.readValue(
            restUserInteractionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userInteraction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserInteraction.class
        );

        // Validate the UserInteraction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserInteractionUpdatableFieldsEquals(returnedUserInteraction, getPersistedUserInteraction(returnedUserInteraction));

        insertedUserInteraction = returnedUserInteraction;
    }

    @Test
    @Transactional
    void createUserInteractionWithExistingId() throws Exception {
        // Create the UserInteraction with an existing ID
        userInteraction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userInteraction)))
            .andExpect(status().isBadRequest());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userInteraction.setType(null);

        // Create the UserInteraction, which fails.

        restUserInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userInteraction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInteractionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userInteraction.setInteractionDate(null);

        // Create the UserInteraction, which fails.

        restUserInteractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userInteraction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserInteractions() throws Exception {
        // Initialize the database
        insertedUserInteraction = userInteractionRepository.saveAndFlush(userInteraction);

        // Get all the userInteractionList
        restUserInteractionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInteraction.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].interactionDate").value(hasItem(DEFAULT_INTERACTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserInteraction() throws Exception {
        // Initialize the database
        insertedUserInteraction = userInteractionRepository.saveAndFlush(userInteraction);

        // Get the userInteraction
        restUserInteractionMockMvc
            .perform(get(ENTITY_API_URL_ID, userInteraction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userInteraction.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.interactionDate").value(DEFAULT_INTERACTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserInteraction() throws Exception {
        // Get the userInteraction
        restUserInteractionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserInteraction() throws Exception {
        // Initialize the database
        insertedUserInteraction = userInteractionRepository.saveAndFlush(userInteraction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userInteraction
        UserInteraction updatedUserInteraction = userInteractionRepository.findById(userInteraction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserInteraction are not directly saved in db
        em.detach(updatedUserInteraction);
        updatedUserInteraction.type(UPDATED_TYPE).details(UPDATED_DETAILS).interactionDate(UPDATED_INTERACTION_DATE);

        restUserInteractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserInteraction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserInteraction))
            )
            .andExpect(status().isOk());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserInteractionToMatchAllProperties(updatedUserInteraction);
    }

    @Test
    @Transactional
    void putNonExistingUserInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userInteraction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInteractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userInteraction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userInteraction))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userInteraction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInteractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userInteraction))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userInteraction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInteractionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userInteraction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserInteractionWithPatch() throws Exception {
        // Initialize the database
        insertedUserInteraction = userInteractionRepository.saveAndFlush(userInteraction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userInteraction using partial update
        UserInteraction partialUpdatedUserInteraction = new UserInteraction();
        partialUpdatedUserInteraction.setId(userInteraction.getId());

        partialUpdatedUserInteraction.type(UPDATED_TYPE);

        restUserInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserInteraction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserInteraction))
            )
            .andExpect(status().isOk());

        // Validate the UserInteraction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserInteractionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserInteraction, userInteraction),
            getPersistedUserInteraction(userInteraction)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserInteractionWithPatch() throws Exception {
        // Initialize the database
        insertedUserInteraction = userInteractionRepository.saveAndFlush(userInteraction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userInteraction using partial update
        UserInteraction partialUpdatedUserInteraction = new UserInteraction();
        partialUpdatedUserInteraction.setId(userInteraction.getId());

        partialUpdatedUserInteraction.type(UPDATED_TYPE).details(UPDATED_DETAILS).interactionDate(UPDATED_INTERACTION_DATE);

        restUserInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserInteraction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserInteraction))
            )
            .andExpect(status().isOk());

        // Validate the UserInteraction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserInteractionUpdatableFieldsEquals(
            partialUpdatedUserInteraction,
            getPersistedUserInteraction(partialUpdatedUserInteraction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userInteraction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userInteraction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userInteraction))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userInteraction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInteractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userInteraction))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserInteraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userInteraction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserInteractionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userInteraction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserInteraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserInteraction() throws Exception {
        // Initialize the database
        insertedUserInteraction = userInteractionRepository.saveAndFlush(userInteraction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userInteraction
        restUserInteractionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userInteraction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userInteractionRepository.count();
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

    protected UserInteraction getPersistedUserInteraction(UserInteraction userInteraction) {
        return userInteractionRepository.findById(userInteraction.getId()).orElseThrow();
    }

    protected void assertPersistedUserInteractionToMatchAllProperties(UserInteraction expectedUserInteraction) {
        assertUserInteractionAllPropertiesEquals(expectedUserInteraction, getPersistedUserInteraction(expectedUserInteraction));
    }

    protected void assertPersistedUserInteractionToMatchUpdatableProperties(UserInteraction expectedUserInteraction) {
        assertUserInteractionAllUpdatablePropertiesEquals(expectedUserInteraction, getPersistedUserInteraction(expectedUserInteraction));
    }
}
