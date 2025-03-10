package bham.team.web.rest;

import static bham.team.domain.ImagesAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.Images;
import bham.team.repository.ImagesRepository;
import bham.team.service.dto.ImagesDTO;
import bham.team.service.mapper.ImagesMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ImagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImagesResourceIT {

    private static final byte[] DEFAULT_IMAGES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGES_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImagesRepository imagesRepository;

    @Autowired
    private ImagesMapper imagesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagesMockMvc;

    private Images images;

    private Images insertedImages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createEntity() {
        return new Images().images(DEFAULT_IMAGES).imagesContentType(DEFAULT_IMAGES_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Images createUpdatedEntity() {
        return new Images().images(UPDATED_IMAGES).imagesContentType(UPDATED_IMAGES_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        images = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedImages != null) {
            imagesRepository.delete(insertedImages);
            insertedImages = null;
        }
    }

    @Test
    @Transactional
    void createImages() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);
        var returnedImagesDTO = om.readValue(
            restImagesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImagesDTO.class
        );

        // Validate the Images in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImages = imagesMapper.toEntity(returnedImagesDTO);
        assertImagesUpdatableFieldsEquals(returnedImages, getPersistedImages(returnedImages));

        insertedImages = returnedImages;
    }

    @Test
    @Transactional
    void createImagesWithExistingId() throws Exception {
        // Create the Images with an existing ID
        images.setId(1L);
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImages() throws Exception {
        // Initialize the database
        insertedImages = imagesRepository.saveAndFlush(images);

        // Get all the imagesList
        restImagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(images.getId().intValue())))
            .andExpect(jsonPath("$.[*].imagesContentType").value(hasItem(DEFAULT_IMAGES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].images").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGES))));
    }

    @Test
    @Transactional
    void getImages() throws Exception {
        // Initialize the database
        insertedImages = imagesRepository.saveAndFlush(images);

        // Get the images
        restImagesMockMvc
            .perform(get(ENTITY_API_URL_ID, images.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(images.getId().intValue()))
            .andExpect(jsonPath("$.imagesContentType").value(DEFAULT_IMAGES_CONTENT_TYPE))
            .andExpect(jsonPath("$.images").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGES)));
    }

    @Test
    @Transactional
    void getNonExistingImages() throws Exception {
        // Get the images
        restImagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImages() throws Exception {
        // Initialize the database
        insertedImages = imagesRepository.saveAndFlush(images);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the images
        Images updatedImages = imagesRepository.findById(images.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImages are not directly saved in db
        em.detach(updatedImages);
        updatedImages.images(UPDATED_IMAGES).imagesContentType(UPDATED_IMAGES_CONTENT_TYPE);
        ImagesDTO imagesDTO = imagesMapper.toDto(updatedImages);

        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImagesToMatchAllProperties(updatedImages);
    }

    @Test
    @Transactional
    void putNonExistingImages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagesDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        insertedImages = imagesRepository.saveAndFlush(images);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImagesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedImages, images), getPersistedImages(images));
    }

    @Test
    @Transactional
    void fullUpdateImagesWithPatch() throws Exception {
        // Initialize the database
        insertedImages = imagesRepository.saveAndFlush(images);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the images using partial update
        Images partialUpdatedImages = new Images();
        partialUpdatedImages.setId(images.getId());

        partialUpdatedImages.images(UPDATED_IMAGES).imagesContentType(UPDATED_IMAGES_CONTENT_TYPE);

        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImages))
            )
            .andExpect(status().isOk());

        // Validate the Images in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImagesUpdatableFieldsEquals(partialUpdatedImages, getPersistedImages(partialUpdatedImages));
    }

    @Test
    @Transactional
    void patchNonExistingImages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        images.setId(longCount.incrementAndGet());

        // Create the Images
        ImagesDTO imagesDTO = imagesMapper.toDto(images);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imagesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Images in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImages() throws Exception {
        // Initialize the database
        insertedImages = imagesRepository.saveAndFlush(images);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the images
        restImagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, images.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return imagesRepository.count();
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

    protected Images getPersistedImages(Images images) {
        return imagesRepository.findById(images.getId()).orElseThrow();
    }

    protected void assertPersistedImagesToMatchAllProperties(Images expectedImages) {
        assertImagesAllPropertiesEquals(expectedImages, getPersistedImages(expectedImages));
    }

    protected void assertPersistedImagesToMatchUpdatableProperties(Images expectedImages) {
        assertImagesAllUpdatablePropertiesEquals(expectedImages, getPersistedImages(expectedImages));
    }
}
