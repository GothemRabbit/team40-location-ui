package bham.team.web.rest;

import static bham.team.domain.WishlistAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.Wishlist;
import bham.team.domain.enumeration.VisibilityType;
import bham.team.repository.WishlistRepository;
import bham.team.service.dto.WishlistDTO;
import bham.team.service.mapper.WishlistMapper;
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
 * Integration tests for the {@link WishlistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WishlistResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final VisibilityType DEFAULT_VISIBILITY = VisibilityType.PRIVATE;
    private static final VisibilityType UPDATED_VISIBILITY = VisibilityType.PUBLIC;

    private static final String ENTITY_API_URL = "/api/wishlists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private WishlistMapper wishlistMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishlistMockMvc;

    private Wishlist wishlist;

    private Wishlist insertedWishlist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wishlist createEntity() {
        return new Wishlist().name(DEFAULT_NAME).visibility(DEFAULT_VISIBILITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wishlist createUpdatedEntity() {
        return new Wishlist().name(UPDATED_NAME).visibility(UPDATED_VISIBILITY);
    }

    @BeforeEach
    public void initTest() {
        wishlist = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWishlist != null) {
            wishlistRepository.delete(insertedWishlist);
            insertedWishlist = null;
        }
    }

    @Test
    @Transactional
    void createWishlist() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);
        var returnedWishlistDTO = om.readValue(
            restWishlistMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WishlistDTO.class
        );

        // Validate the Wishlist in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWishlist = wishlistMapper.toEntity(returnedWishlistDTO);
        assertWishlistUpdatableFieldsEquals(returnedWishlist, getPersistedWishlist(returnedWishlist));

        insertedWishlist = returnedWishlist;
    }

    @Test
    @Transactional
    void createWishlistWithExistingId() throws Exception {
        // Create the Wishlist with an existing ID
        wishlist.setId(1L);
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wishlist.setName(null);

        // Create the Wishlist, which fails.
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        restWishlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisibilityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        wishlist.setVisibility(null);

        // Create the Wishlist, which fails.
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        restWishlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWishlists() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get all the wishlistList
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wishlist.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    @Test
    @Transactional
    void getWishlist() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        // Get the wishlist
        restWishlistMockMvc
            .perform(get(ENTITY_API_URL_ID, wishlist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wishlist.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWishlist() throws Exception {
        // Get the wishlist
        restWishlistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWishlist() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishlist
        Wishlist updatedWishlist = wishlistRepository.findById(wishlist.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWishlist are not directly saved in db
        em.detach(updatedWishlist);
        updatedWishlist.name(UPDATED_NAME).visibility(UPDATED_VISIBILITY);
        WishlistDTO wishlistDTO = wishlistMapper.toDto(updatedWishlist);

        restWishlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishlistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isOk());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWishlistToMatchAllProperties(updatedWishlist);
    }

    @Test
    @Transactional
    void putNonExistingWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wishlistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWishlistWithPatch() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishlist using partial update
        Wishlist partialUpdatedWishlist = new Wishlist();
        partialUpdatedWishlist.setId(wishlist.getId());

        partialUpdatedWishlist.visibility(UPDATED_VISIBILITY);

        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWishlist))
            )
            .andExpect(status().isOk());

        // Validate the Wishlist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWishlistUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWishlist, wishlist), getPersistedWishlist(wishlist));
    }

    @Test
    @Transactional
    void fullUpdateWishlistWithPatch() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the wishlist using partial update
        Wishlist partialUpdatedWishlist = new Wishlist();
        partialUpdatedWishlist.setId(wishlist.getId());

        partialUpdatedWishlist.name(UPDATED_NAME).visibility(UPDATED_VISIBILITY);

        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWishlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWishlist))
            )
            .andExpect(status().isOk());

        // Validate the Wishlist in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWishlistUpdatableFieldsEquals(partialUpdatedWishlist, getPersistedWishlist(partialUpdatedWishlist));
    }

    @Test
    @Transactional
    void patchNonExistingWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wishlistDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(wishlistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWishlist() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        wishlist.setId(longCount.incrementAndGet());

        // Create the Wishlist
        WishlistDTO wishlistDTO = wishlistMapper.toDto(wishlist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishlistMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(wishlistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wishlist in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWishlist() throws Exception {
        // Initialize the database
        insertedWishlist = wishlistRepository.saveAndFlush(wishlist);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the wishlist
        restWishlistMockMvc
            .perform(delete(ENTITY_API_URL_ID, wishlist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return wishlistRepository.count();
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

    protected Wishlist getPersistedWishlist(Wishlist wishlist) {
        return wishlistRepository.findById(wishlist.getId()).orElseThrow();
    }

    protected void assertPersistedWishlistToMatchAllProperties(Wishlist expectedWishlist) {
        assertWishlistAllPropertiesEquals(expectedWishlist, getPersistedWishlist(expectedWishlist));
    }

    protected void assertPersistedWishlistToMatchUpdatableProperties(Wishlist expectedWishlist) {
        assertWishlistAllUpdatablePropertiesEquals(expectedWishlist, getPersistedWishlist(expectedWishlist));
    }
}
