package bham.team.web.rest;

import static bham.team.domain.ProductStatusAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static bham.team.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.ProductStatus;
import bham.team.domain.enumeration.ProductState;
import bham.team.repository.ProductStatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link ProductStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductStatusResourceIT {

    private static final ProductState DEFAULT_STATUS = ProductState.AVAILABLE;
    private static final ProductState UPDATED_STATUS = ProductState.PENDING;

    private static final Instant DEFAULT_MEETING_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MEETING_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MEETING_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_MEETING_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_CHAT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_CHAT_LINK = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/product-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductStatusMockMvc;

    private ProductStatus productStatus;

    private ProductStatus insertedProductStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStatus createEntity() {
        return new ProductStatus()
            .status(DEFAULT_STATUS)
            .meetingTime(DEFAULT_MEETING_TIME)
            .meetingLocation(DEFAULT_MEETING_LOCATION)
            .chatLink(DEFAULT_CHAT_LINK)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductStatus createUpdatedEntity() {
        return new ProductStatus()
            .status(UPDATED_STATUS)
            .meetingTime(UPDATED_MEETING_TIME)
            .meetingLocation(UPDATED_MEETING_LOCATION)
            .chatLink(UPDATED_CHAT_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        productStatus = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductStatus != null) {
            productStatusRepository.delete(insertedProductStatus);
            insertedProductStatus = null;
        }
    }

    @Test
    @Transactional
    void createProductStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductStatus
        var returnedProductStatus = om.readValue(
            restProductStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStatus)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductStatus.class
        );

        // Validate the ProductStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductStatusUpdatableFieldsEquals(returnedProductStatus, getPersistedProductStatus(returnedProductStatus));

        insertedProductStatus = returnedProductStatus;
    }

    @Test
    @Transactional
    void createProductStatusWithExistingId() throws Exception {
        // Create the ProductStatus with an existing ID
        productStatus.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStatus)))
            .andExpect(status().isBadRequest());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productStatus.setStatus(null);

        // Create the ProductStatus, which fails.

        restProductStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStatus)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productStatus.setCreatedAt(null);

        // Create the ProductStatus, which fails.

        restProductStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStatus)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductStatuses() throws Exception {
        // Initialize the database
        insertedProductStatus = productStatusRepository.saveAndFlush(productStatus);

        // Get all the productStatusList
        restProductStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].meetingTime").value(hasItem(DEFAULT_MEETING_TIME.toString())))
            .andExpect(jsonPath("$.[*].meetingLocation").value(hasItem(DEFAULT_MEETING_LOCATION)))
            .andExpect(jsonPath("$.[*].chatLink").value(hasItem(DEFAULT_CHAT_LINK)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getProductStatus() throws Exception {
        // Initialize the database
        insertedProductStatus = productStatusRepository.saveAndFlush(productStatus);

        // Get the productStatus
        restProductStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, productStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.meetingTime").value(DEFAULT_MEETING_TIME.toString()))
            .andExpect(jsonPath("$.meetingLocation").value(DEFAULT_MEETING_LOCATION))
            .andExpect(jsonPath("$.chatLink").value(DEFAULT_CHAT_LINK))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingProductStatus() throws Exception {
        // Get the productStatus
        restProductStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductStatus() throws Exception {
        // Initialize the database
        insertedProductStatus = productStatusRepository.saveAndFlush(productStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productStatus
        ProductStatus updatedProductStatus = productStatusRepository.findById(productStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductStatus are not directly saved in db
        em.detach(updatedProductStatus);
        updatedProductStatus
            .status(UPDATED_STATUS)
            .meetingTime(UPDATED_MEETING_TIME)
            .meetingLocation(UPDATED_MEETING_LOCATION)
            .chatLink(UPDATED_CHAT_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restProductStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductStatus))
            )
            .andExpect(status().isOk());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductStatusToMatchAllProperties(updatedProductStatus);
    }

    @Test
    @Transactional
    void putNonExistingProductStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStatus.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductStatusWithPatch() throws Exception {
        // Initialize the database
        insertedProductStatus = productStatusRepository.saveAndFlush(productStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productStatus using partial update
        ProductStatus partialUpdatedProductStatus = new ProductStatus();
        partialUpdatedProductStatus.setId(productStatus.getId());

        partialUpdatedProductStatus.status(UPDATED_STATUS).createdAt(UPDATED_CREATED_AT);

        restProductStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductStatus))
            )
            .andExpect(status().isOk());

        // Validate the ProductStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductStatus, productStatus),
            getPersistedProductStatus(productStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductStatusWithPatch() throws Exception {
        // Initialize the database
        insertedProductStatus = productStatusRepository.saveAndFlush(productStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productStatus using partial update
        ProductStatus partialUpdatedProductStatus = new ProductStatus();
        partialUpdatedProductStatus.setId(productStatus.getId());

        partialUpdatedProductStatus
            .status(UPDATED_STATUS)
            .meetingTime(UPDATED_MEETING_TIME)
            .meetingLocation(UPDATED_MEETING_LOCATION)
            .chatLink(UPDATED_CHAT_LINK)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restProductStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductStatus))
            )
            .andExpect(status().isOk());

        // Validate the ProductStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductStatusUpdatableFieldsEquals(partialUpdatedProductStatus, getPersistedProductStatus(partialUpdatedProductStatus));
    }

    @Test
    @Transactional
    void patchNonExistingProductStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStatus.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productStatus.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductStatus() throws Exception {
        // Initialize the database
        insertedProductStatus = productStatusRepository.saveAndFlush(productStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productStatus
        restProductStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, productStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productStatusRepository.count();
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

    protected ProductStatus getPersistedProductStatus(ProductStatus productStatus) {
        return productStatusRepository.findById(productStatus.getId()).orElseThrow();
    }

    protected void assertPersistedProductStatusToMatchAllProperties(ProductStatus expectedProductStatus) {
        assertProductStatusAllPropertiesEquals(expectedProductStatus, getPersistedProductStatus(expectedProductStatus));
    }

    protected void assertPersistedProductStatusToMatchUpdatableProperties(ProductStatus expectedProductStatus) {
        assertProductStatusAllUpdatablePropertiesEquals(expectedProductStatus, getPersistedProductStatus(expectedProductStatus));
    }
}
