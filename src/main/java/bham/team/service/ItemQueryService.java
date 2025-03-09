package bham.team.service;

import bham.team.domain.*; // for static metamodels
import bham.team.domain.Item;
import bham.team.repository.ItemRepository;
import bham.team.service.criteria.ItemCriteria;
import bham.team.service.dto.ItemDTO;
import bham.team.service.mapper.ItemMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Item} entities in the database.
 * The main input is a {@link ItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemQueryService extends QueryService<Item> {

    private static final Logger LOG = LoggerFactory.getLogger(ItemQueryService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemQueryService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Return a {@link Page} of {@link ItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findByCriteria(ItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.fetchBagRelationships(itemRepository.findAll(specification, page)).map(itemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Item> specification = createSpecification(criteria);
        return itemRepository.count(specification);
    }

    /**
     * Function to convert {@link ItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Item> createSpecification(ItemCriteria criteria) {
        Specification<Item> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Item_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Item_.title));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Item_.price));
            }
            if (criteria.getCondition() != null) {
                specification = specification.and(buildSpecification(criteria.getCondition(), Item_.condition));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getCategory(), Item_.category));
            }
            if (criteria.getBrand() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrand(), Item_.brand));
            }
            if (criteria.getColour() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColour(), Item_.colour));
            }
            if (criteria.getTimeListed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeListed(), Item_.timeListed));
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImagesId(), root -> root.join(Item_.images, JoinType.LEFT).get(Images_.id))
                );
            }
            if (criteria.getWishlistId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getWishlistId(), root -> root.join(Item_.wishlists, JoinType.LEFT).get(Wishlist_.id))
                );
            }
            if (criteria.getProductStatusId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductStatusId(), root ->
                        root.join(Item_.productStatus, JoinType.LEFT).get(ProductStatus_.id)
                    )
                );
            }
            if (criteria.getProfileDetailsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProfileDetailsId(), root ->
                        root.join(Item_.profileDetails, JoinType.LEFT).get(ProfileDetails_.id)
                    )
                );
            }
            if (criteria.getLikesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getLikesId(), root -> root.join(Item_.likes, JoinType.LEFT).get(Likes_.id))
                );
            }
            if (criteria.getSellerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSellerId(), root -> root.join(Item_.seller, JoinType.LEFT).get(UserDetails_.id))
                );
            }
        }
        return specification;
    }
}
