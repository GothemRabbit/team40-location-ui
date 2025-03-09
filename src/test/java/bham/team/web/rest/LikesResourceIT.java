package bham.team.web.rest;

import static bham.team.domain.LikesAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.Likes;
import bham.team.repository.LikesRepository;
import bham.team.service.dto.LikesDTO;
import bham.team.service.mapper.LikesMapper;
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
 * Integration tests for the {@link LikesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikesResourceIT {

    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    private static final String ENTITY_API_URL = "/api/likes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikesMockMvc;

    private Likes likes;

    private Likes insertedLikes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Likes createEntity() {
        return new Likes().liked(DEFAULT_LIKED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Likes createUpdatedEntity() {
        return new Likes().liked(UPDATED_LIKED);
    }

    @BeforeEach
    public void initTest() {
        likes = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLikes != null) {
            likesRepository.delete(insertedLikes);
            insertedLikes = null;
        }
    }

    @Test
    @Transactional
    void createLikes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);
        var returnedLikesDTO = om.readValue(
            restLikesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LikesDTO.class
        );

        // Validate the Likes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLikes = likesMapper.toEntity(returnedLikesDTO);
        assertLikesUpdatableFieldsEquals(returnedLikes, getPersistedLikes(returnedLikes));

        insertedLikes = returnedLikes;
    }

    @Test
    @Transactional
    void createLikesWithExistingId() throws Exception {
        // Create the Likes with an existing ID
        likes.setId(1L);
        LikesDTO likesDTO = likesMapper.toDto(likes);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLikes() throws Exception {
        // Initialize the database
        insertedLikes = likesRepository.saveAndFlush(likes);

        // Get all the likesList
        restLikesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likes.getId().intValue())))
            .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())));
    }

    @Test
    @Transactional
    void getLikes() throws Exception {
        // Initialize the database
        insertedLikes = likesRepository.saveAndFlush(likes);

        // Get the likes
        restLikesMockMvc
            .perform(get(ENTITY_API_URL_ID, likes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likes.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLikes() throws Exception {
        // Get the likes
        restLikesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikes() throws Exception {
        // Initialize the database
        insertedLikes = likesRepository.saveAndFlush(likes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likes
        Likes updatedLikes = likesRepository.findById(likes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLikes are not directly saved in db
        em.detach(updatedLikes);
        updatedLikes.liked(UPDATED_LIKED);
        LikesDTO likesDTO = likesMapper.toDto(updatedLikes);

        restLikesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likesDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLikesToMatchAllProperties(updatedLikes);
    }

    @Test
    @Transactional
    void putNonExistingLikes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likes.setId(longCount.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likesDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likes.setId(longCount.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likes.setId(longCount.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(likesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikesWithPatch() throws Exception {
        // Initialize the database
        insertedLikes = likesRepository.saveAndFlush(likes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likes using partial update
        Likes partialUpdatedLikes = new Likes();
        partialUpdatedLikes.setId(likes.getId());

        partialUpdatedLikes.liked(UPDATED_LIKED);

        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLikes))
            )
            .andExpect(status().isOk());

        // Validate the Likes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLikes, likes), getPersistedLikes(likes));
    }

    @Test
    @Transactional
    void fullUpdateLikesWithPatch() throws Exception {
        // Initialize the database
        insertedLikes = likesRepository.saveAndFlush(likes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the likes using partial update
        Likes partialUpdatedLikes = new Likes();
        partialUpdatedLikes.setId(likes.getId());

        partialUpdatedLikes.liked(UPDATED_LIKED);

        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLikes))
            )
            .andExpect(status().isOk());

        // Validate the Likes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLikesUpdatableFieldsEquals(partialUpdatedLikes, getPersistedLikes(partialUpdatedLikes));
    }

    @Test
    @Transactional
    void patchNonExistingLikes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likes.setId(longCount.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likes.setId(longCount.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(likesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        likes.setId(longCount.incrementAndGet());

        // Create the Likes
        LikesDTO likesDTO = likesMapper.toDto(likes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(likesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Likes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikes() throws Exception {
        // Initialize the database
        insertedLikes = likesRepository.saveAndFlush(likes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the likes
        restLikesMockMvc
            .perform(delete(ENTITY_API_URL_ID, likes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return likesRepository.count();
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

    protected Likes getPersistedLikes(Likes likes) {
        return likesRepository.findById(likes.getId()).orElseThrow();
    }

    protected void assertPersistedLikesToMatchAllProperties(Likes expectedLikes) {
        assertLikesAllPropertiesEquals(expectedLikes, getPersistedLikes(expectedLikes));
    }

    protected void assertPersistedLikesToMatchUpdatableProperties(Likes expectedLikes) {
        assertLikesAllUpdatablePropertiesEquals(expectedLikes, getPersistedLikes(expectedLikes));
    }
}
