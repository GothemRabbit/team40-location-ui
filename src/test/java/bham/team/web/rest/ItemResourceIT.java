package bham.team.web.rest;

import static bham.team.domain.ItemAsserts.*;
import static bham.team.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bham.team.IntegrationTest;
import bham.team.domain.Item;
import bham.team.domain.UserDetails;
import bham.team.domain.Wishlist;
import bham.team.domain.enumeration.Category;
import bham.team.domain.enumeration.Condition;
import bham.team.repository.ItemRepository;
import bham.team.service.ItemService;
import bham.team.service.dto.ItemDTO;
import bham.team.service.mapper.ItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;
    private static final Double SMALLER_PRICE = 0D - 1D;

    private static final Condition DEFAULT_CONDITION = Condition.BRANDNEW;
    private static final Condition UPDATED_CONDITION = Condition.LIKENEW;

    private static final Category DEFAULT_CATEGORY = Category.FOOTWEAR;
    private static final Category UPDATED_CATEGORY = Category.CLOTHING;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String DEFAULT_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_COLOUR = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME_LISTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_LISTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Autowired
    private ItemMapper itemMapper;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    private Item insertedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity() {
        return new Item()
            .title(DEFAULT_TITLE)
            .price(DEFAULT_PRICE)
            .condition(DEFAULT_CONDITION)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .brand(DEFAULT_BRAND)
            .colour(DEFAULT_COLOUR)
            .timeListed(DEFAULT_TIME_LISTED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity() {
        return new Item()
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .condition(UPDATED_CONDITION)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .brand(UPDATED_BRAND)
            .colour(UPDATED_COLOUR)
            .timeListed(UPDATED_TIME_LISTED);
    }

    @BeforeEach
    public void initTest() {
        item = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedItem != null) {
            itemRepository.delete(insertedItem);
            insertedItem = null;
        }
    }

    @Test
    @Transactional
    void createItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        var returnedItemDTO = om.readValue(
            restItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemDTO.class
        );

        // Validate the Item in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedItem = itemMapper.toEntity(returnedItemDTO);
        assertItemUpdatableFieldsEquals(returnedItem, getPersistedItem(returnedItem));

        insertedItem = returnedItem;
    }

    @Test
    @Transactional
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        item.setTitle(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        item.setPrice(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConditionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        item.setCondition(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        item.setCategory(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeListedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        item.setTimeListed(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItems() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR)))
            .andExpect(jsonPath("$.[*].timeListed").value(hasItem(DEFAULT_TIME_LISTED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(itemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND))
            .andExpect(jsonPath("$.colour").value(DEFAULT_COLOUR))
            .andExpect(jsonPath("$.timeListed").value(DEFAULT_TIME_LISTED.toString()));
    }

    @Test
    @Transactional
    void getItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        Long id = item.getId();

        defaultItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllItemsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where title equals to
        defaultItemFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where title in
        defaultItemFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where title is not null
        defaultItemFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where title contains
        defaultItemFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where title does not contain
        defaultItemFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price equals to
        defaultItemFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price in
        defaultItemFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price is not null
        defaultItemFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price is greater than or equal to
        defaultItemFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price is less than or equal to
        defaultItemFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price is less than
        defaultItemFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where price is greater than
        defaultItemFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllItemsByConditionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where condition equals to
        defaultItemFiltering("condition.equals=" + DEFAULT_CONDITION, "condition.equals=" + UPDATED_CONDITION);
    }

    @Test
    @Transactional
    void getAllItemsByConditionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where condition in
        defaultItemFiltering("condition.in=" + DEFAULT_CONDITION + "," + UPDATED_CONDITION, "condition.in=" + UPDATED_CONDITION);
    }

    @Test
    @Transactional
    void getAllItemsByConditionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where condition is not null
        defaultItemFiltering("condition.specified=true", "condition.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where category equals to
        defaultItemFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllItemsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where category in
        defaultItemFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllItemsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where category is not null
        defaultItemFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where brand equals to
        defaultItemFiltering("brand.equals=" + DEFAULT_BRAND, "brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllItemsByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where brand in
        defaultItemFiltering("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND, "brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllItemsByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where brand is not null
        defaultItemFiltering("brand.specified=true", "brand.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByBrandContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where brand contains
        defaultItemFiltering("brand.contains=" + DEFAULT_BRAND, "brand.contains=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllItemsByBrandNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where brand does not contain
        defaultItemFiltering("brand.doesNotContain=" + UPDATED_BRAND, "brand.doesNotContain=" + DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void getAllItemsByColourIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where colour equals to
        defaultItemFiltering("colour.equals=" + DEFAULT_COLOUR, "colour.equals=" + UPDATED_COLOUR);
    }

    @Test
    @Transactional
    void getAllItemsByColourIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where colour in
        defaultItemFiltering("colour.in=" + DEFAULT_COLOUR + "," + UPDATED_COLOUR, "colour.in=" + UPDATED_COLOUR);
    }

    @Test
    @Transactional
    void getAllItemsByColourIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where colour is not null
        defaultItemFiltering("colour.specified=true", "colour.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByColourContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where colour contains
        defaultItemFiltering("colour.contains=" + DEFAULT_COLOUR, "colour.contains=" + UPDATED_COLOUR);
    }

    @Test
    @Transactional
    void getAllItemsByColourNotContainsSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where colour does not contain
        defaultItemFiltering("colour.doesNotContain=" + UPDATED_COLOUR, "colour.doesNotContain=" + DEFAULT_COLOUR);
    }

    @Test
    @Transactional
    void getAllItemsByTimeListedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where timeListed equals to
        defaultItemFiltering("timeListed.equals=" + DEFAULT_TIME_LISTED, "timeListed.equals=" + UPDATED_TIME_LISTED);
    }

    @Test
    @Transactional
    void getAllItemsByTimeListedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where timeListed in
        defaultItemFiltering("timeListed.in=" + DEFAULT_TIME_LISTED + "," + UPDATED_TIME_LISTED, "timeListed.in=" + UPDATED_TIME_LISTED);
    }

    @Test
    @Transactional
    void getAllItemsByTimeListedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        // Get all the itemList where timeListed is not null
        defaultItemFiltering("timeListed.specified=true", "timeListed.specified=false");
    }

    @Test
    @Transactional
    void getAllItemsByWishlistIsEqualToSomething() throws Exception {
        Wishlist wishlist;
        if (TestUtil.findAll(em, Wishlist.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            wishlist = WishlistResourceIT.createEntity();
        } else {
            wishlist = TestUtil.findAll(em, Wishlist.class).get(0);
        }
        em.persist(wishlist);
        em.flush();
        item.addWishlist(wishlist);
        itemRepository.saveAndFlush(item);
        Long wishlistId = wishlist.getId();
        // Get all the itemList where wishlist equals to wishlistId
        defaultItemShouldBeFound("wishlistId.equals=" + wishlistId);

        // Get all the itemList where wishlist equals to (wishlistId + 1)
        defaultItemShouldNotBeFound("wishlistId.equals=" + (wishlistId + 1));
    }

    @Test
    @Transactional
    void getAllItemsBySellerIsEqualToSomething() throws Exception {
        UserDetails seller;
        if (TestUtil.findAll(em, UserDetails.class).isEmpty()) {
            itemRepository.saveAndFlush(item);
            seller = UserDetailsResourceIT.createEntity();
        } else {
            seller = TestUtil.findAll(em, UserDetails.class).get(0);
        }
        em.persist(seller);
        em.flush();
        item.setSeller(seller);
        itemRepository.saveAndFlush(item);
        Long sellerId = seller.getId();
        // Get all the itemList where seller equals to sellerId
        defaultItemShouldBeFound("sellerId.equals=" + sellerId);

        // Get all the itemList where seller equals to (sellerId + 1)
        defaultItemShouldNotBeFound("sellerId.equals=" + (sellerId + 1));
    }

    private void defaultItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultItemShouldBeFound(shouldBeFound);
        defaultItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultItemShouldBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)))
            .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR)))
            .andExpect(jsonPath("$.[*].timeListed").value(hasItem(DEFAULT_TIME_LISTED.toString())));

        // Check, that the count call also returns 1
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultItemShouldNotBeFound(String filter) throws Exception {
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItem are not directly saved in db
        em.detach(updatedItem);
        updatedItem
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .condition(UPDATED_CONDITION)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .brand(UPDATED_BRAND)
            .colour(UPDATED_COLOUR)
            .timeListed(UPDATED_TIME_LISTED);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc
            .perform(put(ENTITY_API_URL_ID, itemDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isOk());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemToMatchAllProperties(updatedItem);
    }

    @Test
    @Transactional
    void putNonExistingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL_ID, itemDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.price(UPDATED_PRICE);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedItem, item), getPersistedItem(item));
    }

    @Test
    @Transactional
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .condition(UPDATED_CONDITION)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .brand(UPDATED_BRAND)
            .colour(UPDATED_COLOUR)
            .timeListed(UPDATED_TIME_LISTED);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemUpdatableFieldsEquals(partialUpdatedItem, getPersistedItem(partialUpdatedItem));
    }

    @Test
    @Transactional
    void patchNonExistingItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        item.setId(longCount.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItem() throws Exception {
        // Initialize the database
        insertedItem = itemRepository.saveAndFlush(item);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return itemRepository.count();
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

    protected Item getPersistedItem(Item item) {
        return itemRepository.findById(item.getId()).orElseThrow();
    }

    protected void assertPersistedItemToMatchAllProperties(Item expectedItem) {
        assertItemAllPropertiesEquals(expectedItem, getPersistedItem(expectedItem));
    }

    protected void assertPersistedItemToMatchUpdatableProperties(Item expectedItem) {
        assertItemAllUpdatablePropertiesEquals(expectedItem, getPersistedItem(expectedItem));
    }
}
