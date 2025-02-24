package bham.team.web.rest;

import static bham.team.domain.AuthenticationAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.Authentication;
import bham.team.repository.AuthenticationRepository;
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
 * Integration tests for the {@link AuthenticationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuthenticationResourceIT {

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/authentications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthenticationMockMvc;

    private Authentication authentication;

    private Authentication insertedAuthentication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authentication createEntity() {
        return new Authentication().password(DEFAULT_PASSWORD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authentication createUpdatedEntity() {
        return new Authentication().password(UPDATED_PASSWORD);
    }

    @BeforeEach
    public void initTest() {
        authentication = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAuthentication != null) {
            authenticationRepository.delete(insertedAuthentication);
            insertedAuthentication = null;
        }
    }

    @Test
    @Transactional
    void createAuthentication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Authentication
        var returnedAuthentication = om.readValue(
            restAuthenticationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentication)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Authentication.class
        );

        // Validate the Authentication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAuthenticationUpdatableFieldsEquals(returnedAuthentication, getPersistedAuthentication(returnedAuthentication));

        insertedAuthentication = returnedAuthentication;
    }

    @Test
    @Transactional
    void createAuthenticationWithExistingId() throws Exception {
        // Create the Authentication with an existing ID
        authentication.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthenticationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentication)))
            .andExpect(status().isBadRequest());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        authentication.setPassword(null);

        // Create the Authentication, which fails.

        restAuthenticationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentication)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuthentications() throws Exception {
        // Initialize the database
        insertedAuthentication = authenticationRepository.saveAndFlush(authentication);

        // Get all the authenticationList
        restAuthenticationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authentication.getId().intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getAuthentication() throws Exception {
        // Initialize the database
        insertedAuthentication = authenticationRepository.saveAndFlush(authentication);

        // Get the authentication
        restAuthenticationMockMvc
            .perform(get(ENTITY_API_URL_ID, authentication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authentication.getId().intValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingAuthentication() throws Exception {
        // Get the authentication
        restAuthenticationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuthentication() throws Exception {
        // Initialize the database
        insertedAuthentication = authenticationRepository.saveAndFlush(authentication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authentication
        Authentication updatedAuthentication = authenticationRepository.findById(authentication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAuthentication are not directly saved in db
        em.detach(updatedAuthentication);
        updatedAuthentication.password(UPDATED_PASSWORD);

        restAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAuthentication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAuthentication))
            )
            .andExpect(status().isOk());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuthenticationToMatchAllProperties(updatedAuthentication);
    }

    @Test
    @Transactional
    void putNonExistingAuthentication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authentication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthentication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthenticationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(authentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthentication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthenticationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(authentication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthenticationWithPatch() throws Exception {
        // Initialize the database
        insertedAuthentication = authenticationRepository.saveAndFlush(authentication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authentication using partial update
        Authentication partialUpdatedAuthentication = new Authentication();
        partialUpdatedAuthentication.setId(authentication.getId());

        restAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthentication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthentication))
            )
            .andExpect(status().isOk());

        // Validate the Authentication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthenticationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAuthentication, authentication),
            getPersistedAuthentication(authentication)
        );
    }

    @Test
    @Transactional
    void fullUpdateAuthenticationWithPatch() throws Exception {
        // Initialize the database
        insertedAuthentication = authenticationRepository.saveAndFlush(authentication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the authentication using partial update
        Authentication partialUpdatedAuthentication = new Authentication();
        partialUpdatedAuthentication.setId(authentication.getId());

        partialUpdatedAuthentication.password(UPDATED_PASSWORD);

        restAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthentication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuthentication))
            )
            .andExpect(status().isOk());

        // Validate the Authentication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuthenticationUpdatableFieldsEquals(partialUpdatedAuthentication, getPersistedAuthentication(partialUpdatedAuthentication));
    }

    @Test
    @Transactional
    void patchNonExistingAuthentication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authentication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthentication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthenticationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(authentication))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthentication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        authentication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthenticationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(authentication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authentication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthentication() throws Exception {
        // Initialize the database
        insertedAuthentication = authenticationRepository.saveAndFlush(authentication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the authentication
        restAuthenticationMockMvc
            .perform(delete(ENTITY_API_URL_ID, authentication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return authenticationRepository.count();
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

    protected Authentication getPersistedAuthentication(Authentication authentication) {
        return authenticationRepository.findById(authentication.getId()).orElseThrow();
    }

    protected void assertPersistedAuthenticationToMatchAllProperties(Authentication expectedAuthentication) {
        assertAuthenticationAllPropertiesEquals(expectedAuthentication, getPersistedAuthentication(expectedAuthentication));
    }

    protected void assertPersistedAuthenticationToMatchUpdatableProperties(Authentication expectedAuthentication) {
        assertAuthenticationAllUpdatablePropertiesEquals(expectedAuthentication, getPersistedAuthentication(expectedAuthentication));
    }
}
