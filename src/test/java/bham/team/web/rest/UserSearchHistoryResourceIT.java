package bham.team.web.rest;

import static bham.team.domain.UserSearchHistoryAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.UserSearchHistory;
import bham.team.repository.UserSearchHistoryRepository;
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
 * Integration tests for the {@link UserSearchHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSearchHistoryResourceIT {

    private static final String DEFAULT_SEARCH_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_SEARCH_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_FILTERS = "AAAAAAAAAA";
    private static final String UPDATED_FILTERS = "BBBBBBBBBB";

    private static final Instant DEFAULT_SEARCH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEARCH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-search-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSearchHistoryRepository userSearchHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSearchHistoryMockMvc;

    private UserSearchHistory userSearchHistory;

    private UserSearchHistory insertedUserSearchHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSearchHistory createEntity() {
        return new UserSearchHistory().searchQuery(DEFAULT_SEARCH_QUERY).filters(DEFAULT_FILTERS).searchDate(DEFAULT_SEARCH_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSearchHistory createUpdatedEntity() {
        return new UserSearchHistory().searchQuery(UPDATED_SEARCH_QUERY).filters(UPDATED_FILTERS).searchDate(UPDATED_SEARCH_DATE);
    }

    @BeforeEach
    public void initTest() {
        userSearchHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserSearchHistory != null) {
            userSearchHistoryRepository.delete(insertedUserSearchHistory);
            insertedUserSearchHistory = null;
        }
    }

    @Test
    @Transactional
    void createUserSearchHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserSearchHistory
        var returnedUserSearchHistory = om.readValue(
            restUserSearchHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSearchHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserSearchHistory.class
        );

        // Validate the UserSearchHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserSearchHistoryUpdatableFieldsEquals(returnedUserSearchHistory, getPersistedUserSearchHistory(returnedUserSearchHistory));

        insertedUserSearchHistory = returnedUserSearchHistory;
    }

    @Test
    @Transactional
    void createUserSearchHistoryWithExistingId() throws Exception {
        // Create the UserSearchHistory with an existing ID
        userSearchHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSearchHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSearchHistory)))
            .andExpect(status().isBadRequest());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSearchQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSearchHistory.setSearchQuery(null);

        // Create the UserSearchHistory, which fails.

        restUserSearchHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSearchHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSearchDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSearchHistory.setSearchDate(null);

        // Create the UserSearchHistory, which fails.

        restUserSearchHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSearchHistory)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSearchHistories() throws Exception {
        // Initialize the database
        insertedUserSearchHistory = userSearchHistoryRepository.saveAndFlush(userSearchHistory);

        // Get all the userSearchHistoryList
        restUserSearchHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSearchHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchQuery").value(hasItem(DEFAULT_SEARCH_QUERY)))
            .andExpect(jsonPath("$.[*].filters").value(hasItem(DEFAULT_FILTERS)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserSearchHistory() throws Exception {
        // Initialize the database
        insertedUserSearchHistory = userSearchHistoryRepository.saveAndFlush(userSearchHistory);

        // Get the userSearchHistory
        restUserSearchHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, userSearchHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSearchHistory.getId().intValue()))
            .andExpect(jsonPath("$.searchQuery").value(DEFAULT_SEARCH_QUERY))
            .andExpect(jsonPath("$.filters").value(DEFAULT_FILTERS))
            .andExpect(jsonPath("$.searchDate").value(DEFAULT_SEARCH_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserSearchHistory() throws Exception {
        // Get the userSearchHistory
        restUserSearchHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSearchHistory() throws Exception {
        // Initialize the database
        insertedUserSearchHistory = userSearchHistoryRepository.saveAndFlush(userSearchHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSearchHistory
        UserSearchHistory updatedUserSearchHistory = userSearchHistoryRepository.findById(userSearchHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSearchHistory are not directly saved in db
        em.detach(updatedUserSearchHistory);
        updatedUserSearchHistory.searchQuery(UPDATED_SEARCH_QUERY).filters(UPDATED_FILTERS).searchDate(UPDATED_SEARCH_DATE);

        restUserSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserSearchHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserSearchHistory))
            )
            .andExpect(status().isOk());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSearchHistoryToMatchAllProperties(updatedUserSearchHistory);
    }

    @Test
    @Transactional
    void putNonExistingUserSearchHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSearchHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSearchHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSearchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSearchHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSearchHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSearchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSearchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSearchHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSearchHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSearchHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSearchHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSearchHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedUserSearchHistory = userSearchHistoryRepository.saveAndFlush(userSearchHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSearchHistory using partial update
        UserSearchHistory partialUpdatedUserSearchHistory = new UserSearchHistory();
        partialUpdatedUserSearchHistory.setId(userSearchHistory.getId());

        partialUpdatedUserSearchHistory.searchQuery(UPDATED_SEARCH_QUERY);

        restUserSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSearchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSearchHistory))
            )
            .andExpect(status().isOk());

        // Validate the UserSearchHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSearchHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSearchHistory, userSearchHistory),
            getPersistedUserSearchHistory(userSearchHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserSearchHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedUserSearchHistory = userSearchHistoryRepository.saveAndFlush(userSearchHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSearchHistory using partial update
        UserSearchHistory partialUpdatedUserSearchHistory = new UserSearchHistory();
        partialUpdatedUserSearchHistory.setId(userSearchHistory.getId());

        partialUpdatedUserSearchHistory.searchQuery(UPDATED_SEARCH_QUERY).filters(UPDATED_FILTERS).searchDate(UPDATED_SEARCH_DATE);

        restUserSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSearchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSearchHistory))
            )
            .andExpect(status().isOk());

        // Validate the UserSearchHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSearchHistoryUpdatableFieldsEquals(
            partialUpdatedUserSearchHistory,
            getPersistedUserSearchHistory(partialUpdatedUserSearchHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserSearchHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSearchHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSearchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSearchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSearchHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSearchHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSearchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSearchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSearchHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSearchHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSearchHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userSearchHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSearchHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSearchHistory() throws Exception {
        // Initialize the database
        insertedUserSearchHistory = userSearchHistoryRepository.saveAndFlush(userSearchHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userSearchHistory
        restUserSearchHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSearchHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userSearchHistoryRepository.count();
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

    protected UserSearchHistory getPersistedUserSearchHistory(UserSearchHistory userSearchHistory) {
        return userSearchHistoryRepository.findById(userSearchHistory.getId()).orElseThrow();
    }

    protected void assertPersistedUserSearchHistoryToMatchAllProperties(UserSearchHistory expectedUserSearchHistory) {
        assertUserSearchHistoryAllPropertiesEquals(expectedUserSearchHistory, getPersistedUserSearchHistory(expectedUserSearchHistory));
    }

    protected void assertPersistedUserSearchHistoryToMatchUpdatableProperties(UserSearchHistory expectedUserSearchHistory) {
        assertUserSearchHistoryAllUpdatablePropertiesEquals(
            expectedUserSearchHistory,
            getPersistedUserSearchHistory(expectedUserSearchHistory)
        );
    }
}
