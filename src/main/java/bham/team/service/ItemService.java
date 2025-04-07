package bham.team.service;

import bham.team.domain.Item;
import bham.team.domain.ProfileDetails;
import bham.team.domain.enumeration.ProductState;
import bham.team.repository.ImagesRepository;
import bham.team.repository.ItemRepository;
import bham.team.repository.LikesRepository;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.service.dto.ItemDTO;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.mapper.ItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.Item}.
 */
@Service
@Transactional
public class ItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final LikesRepository likesRepository;
    private final ProfileDetailsRepository profileDetailsRepository;

    // ① 新增依赖：需要在此处注入 ProductStatusService
    private final ProductStatusService productStatusService;

    // ② 在构造函数中，增加 productStatusService
    public ItemService(
        ItemRepository itemRepository,
        ItemMapper itemMapper,
        LikesRepository likesRepository,
        ProfileDetailsRepository profileDetailsRepository,
        ProductStatusService productStatusService // 新增
    ) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.likesRepository = likesRepository;
        this.profileDetailsRepository = profileDetailsRepository;
        this.productStatusService = productStatusService; // 赋值
    }

    /**
     * 上架商品：发布 Item，同时自动创建一条 "UNRESERVED" 的订单（ProductStatus）。
     */
    public ItemDTO save(ItemDTO itemDTO) {
        LOG.debug("Request to save Item : {}", itemDTO);

        // 1. 把前端传来的 ItemDTO 转为实体
        Item item = itemMapper.toEntity(itemDTO);

        // 2. 如果 itemDTO 里带了 profileDetails.id，则去数据库查出完整的 ProfileDetails
        if (itemDTO.getProfileDetails() != null && itemDTO.getProfileDetails().getId() != null) {
            LOG.info("Fetching ProfileDetails with ID: {}", itemDTO.getProfileDetails().getId());

            ProfileDetails profileDetails = profileDetailsRepository
                .findByIdWithUser(itemDTO.getProfileDetails().getId())
                .orElseThrow(() -> new RuntimeException("ProfileDetails not found"));

            item.setProfileDetails(profileDetails);
            LOG.info("ProfileDetails set on item: {}", item.getProfileDetails());
        } else {
            LOG.warn("ProfileDetails is NULL in itemDTO! Item might be saved without a profile.");
        }

        // 3. 先保存 item（即商品信息）
        item = itemRepository.save(item);
        LOG.info(
            "Item saved with ID: {}, ProfileDetails ID: {}",
            item.getId(),
            item.getProfileDetails() != null ? item.getProfileDetails().getId() : "NULL"
        );
        // 在 item 保存成功后，创建一个仅包含 item 编号的 ItemDTO 对象
        ItemDTO newItemRef = new ItemDTO();
        newItemRef.setId(item.getId());

        // 4. 自动在 ProductStatus 表里新增一条「UNRESERVED」订单
        ProductStatusDTO productStatusDTO = new ProductStatusDTO();
        productStatusDTO.setStatus(ProductState.RESERVED);
        productStatusDTO.setItem(newItemRef); // 将刚保存的 item 编号赋值给订单的 item 字段
        productStatusService.save(productStatusDTO);
        // 5. 返回结果
        return itemMapper.toDto(item);
    }

    public ItemDTO update(ItemDTO itemDTO) {
        LOG.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);

        if (itemDTO.getProfileDetails() != null && itemDTO.getProfileDetails().getId() != null) {
            ProfileDetails profileDetails = profileDetailsRepository
                .findByIdWithUser(itemDTO.getProfileDetails().getId())
                .orElseThrow(() -> new RuntimeException("ProfileDetails not found"));
            item.setProfileDetails(profileDetails);
        }

        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        LOG.debug("Request to partially update Item : {}", itemDTO);

        return itemRepository
            .findById(itemDTO.getId())
            .map(existingItem -> {
                itemMapper.partialUpdate(existingItem, itemDTO);
                return existingItem;
            })
            .map(itemRepository::save)
            .map(itemMapper::toDto);
    }

    public Page<ItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itemRepository.findAllWithEagerRelationships(pageable).map(itemMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findAllWhereProductStatusIsNull() {
        LOG.debug("Request to get all items where ProductStatus is null");
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
            .filter(item -> item.getProductStatus() == null)
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOneWithLikes(Long id, Long profileId) {
        return itemRepository
            .findById(id)
            .map(item -> {
                ItemDTO dto = itemMapper.toDto(item);
                dto.setLikeCount(likesRepository.countLikesByItemId(id));
                dto.setLikedByUser(likesRepository.existsByItemIdAndProfileId(id, profileId));
                return dto;
            });
    }

    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        LOG.debug("Request to get Item with images: {}", id);
        return itemRepository.findByIdWithImages(id).map(itemMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }

    public Optional<Item> getItemWithImages(Long itemId) {
        return itemRepository.findByIdWithImages(itemId);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findAllWithImages() {
        LOG.debug("Request to get all Items with images");
        return itemRepository.findAllWithImages().stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findAllItemsByProfile(Long profileId) {
        LOG.debug("Request to get all Items for ProfileDetails: {}", profileId);
        return itemRepository.findAllItemsByProfileId(profileId).stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOneWithProfileDetails(Long id) {
        LOG.debug("Request to get Item with profile details: {}", id);
        return itemRepository.findByIdWithProfileDetails(id).map(itemMapper::toDto);
    }
}
