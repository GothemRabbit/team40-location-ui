package bham.team.service;

import bham.team.domain.Item;
import bham.team.domain.ProductStatus;
import bham.team.domain.ProfileDetails;
import bham.team.domain.enumeration.ProductState;
import bham.team.repository.ItemRepository;
import bham.team.repository.ProductStatusRepository;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.mapper.ProductStatusMapper;
import bham.team.service.mapper.ProductStatusMapperImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.ProductStatus}.
 */
@Service
@Transactional
public class ProductStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductStatusService.class);

    private final ProductStatusRepository repo;
    private final ProductStatusMapper mapper;
    private final ProfileDetailsService profileDetailsService;
    private final ItemRepository itemRepository;
    private final ProductStatusRepository productStatusRepository;
    private final ProfileDetailsRepository profileDetailsRepository;
    private final ProductStatusMapperImpl productStatusMapper;

    public ProductStatusService(
        ProductStatusRepository repo,
        ProductStatusMapper mapper,
        @Lazy ProfileDetailsService profileDetailsService,
        ItemRepository itemRepository,
        ProductStatusRepository productStatusRepository,
        ProfileDetailsRepository profileDetailsRepository,
        ProductStatusMapperImpl productStatusMapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
        this.profileDetailsService = profileDetailsService;
        this.itemRepository = itemRepository;
        this.productStatusRepository = productStatusRepository;
        this.profileDetailsRepository = profileDetailsRepository;
        this.productStatusMapper = productStatusMapper;
    }

    /**
     * 获取当前登录用户的profileDetailsId
     */
    private Long getCurrentProfileId() {
        return profileDetailsService
            .getCurrentUserProfile()
            .orElseThrow(() -> new RuntimeException("User not logged in or has no ProfileDetails"))
            .getId();
    }

    /**
     * 新增 ProductStatus
     */
    public ProductStatusDTO save(ProductStatusDTO dto) {
        LOG.debug("Request to save ProductStatus : {}", dto);

        // 忽略前端传进来的 profileDetailsId，全部覆盖为当前登录用户
        ProductStatus entity = mapper.toEntity(dto);
        entity.setProfileDetails(new ProfileDetails());
        entity.getProfileDetails().setId(getCurrentProfileId());

        // 保存数据库
        entity = repo.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * 全量更新
     */
    public ProductStatusDTO update(ProductStatusDTO dto) {
        LOG.debug("Request to update ProductStatus : {}", dto);
        Long currentProfileId = getCurrentProfileId();
        ProductStatus existingEntity = repo.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("ProductStatus not found"));
        if (
            !existingEntity.getProfileDetails().getId().equals(currentProfileId) &&
            !existingEntity.getProfileDetails1().getId().equals(currentProfileId)
        ) {
            throw new EntityNotFoundException("ProductStatus not owned by current user");
        }
        ProductStatus entity = mapper.toEntity(dto);
        entity.setProfileDetails(existingEntity.getProfileDetails());
        entity.setProfileDetails1(existingEntity.getProfileDetails1());

        entity = repo.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * 局部更新
     */
    public Optional<ProductStatusDTO> partialUpdate(ProductStatusDTO dto) {
        LOG.debug("Request to partially update ProductStatus : {}", dto);
        Long currentProfileId = getCurrentProfileId();
        return repo
            .findById(dto.getId())
            .map(existingEntity -> {
                Long sellerId = existingEntity.getProfileDetails() != null ? existingEntity.getProfileDetails().getId() : null;
                Long buyerId = existingEntity.getProfileDetails1() != null ? existingEntity.getProfileDetails1().getId() : null;

                if (currentProfileId.equals(sellerId) || currentProfileId.equals(buyerId)) {
                    mapper.partialUpdate(existingEntity, dto);
                    return existingEntity;
                } else {
                    throw new EntityNotFoundException("ProductStatus not owned by current user");
                }
            })
            .map(repo::save)
            .map(mapper::toDto);
    }

    /**
     * 分页查询：只查询当前用户ID下的订单
     */
    @Transactional(readOnly = true)
    public Page<ProductStatusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProductStatuses for current user");
        Long currentProfileId = getCurrentProfileId();

        // 1) 先查出所有 ProductStatus（不带任何 where 条件）
        Page<ProductStatus> pageAll = repo.findAll(pageable);

        // 2) 在内存中过滤出“profileDetails 或 profileDetails1 等于当前用户ID”的记录
        List<ProductStatus> filteredList = pageAll
            .getContent()
            .stream()
            .filter(ps -> {
                Long sellerId = (ps.getProfileDetails() == null) ? null : ps.getProfileDetails().getId();
                Long buyerId = (ps.getProfileDetails1() == null) ? null : ps.getProfileDetails1().getId();
                return currentProfileId.equals(sellerId) || currentProfileId.equals(buyerId);
            })
            .toList();

        // 3) 把过滤后的实体转换为 DTO
        List<ProductStatusDTO> dtoList = filteredList.stream().map(mapper::toDto).toList();
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    /**
     * 查询单条记录：内存过滤 + repo.findAll()
     * 卖家或买家等于当前用户
     */
    @Transactional(readOnly = true)
    public Optional<ProductStatusDTO> findOne(Long id) {
        LOG.debug("Stealth approach to get single ProductStatus as buyer or seller");
        Long currentProfileId = getCurrentProfileId();

        List<ProductStatus> all = repo.findAll();
        return all
            .stream()
            .filter(ps -> ps.getId().equals(id))
            .filter(ps -> {
                Long sellerId = (ps.getProfileDetails() == null) ? null : ps.getProfileDetails().getId();
                Long buyerId = (ps.getProfileDetails1() == null) ? null : ps.getProfileDetails1().getId();
                return currentProfileId.equals(sellerId) || currentProfileId.equals(buyerId);
            })
            .findFirst()
            .map(mapper::toDto);
    }

    public ProductStatusDTO reserveItemInProductStatus(Long itemId, Long buyerProfileId) {
        LOG.debug("Request to reserve ProductStatus for Item ID: {}", itemId);

        Optional<ProductStatus> productStatusOpt = productStatusRepository.findByItemIdAndStatus(itemId, ProductState.UNRESERVED);
        if (!productStatusOpt.isPresent()) {
            throw new EntityNotFoundException("ProductStatus not found for this item or the item is already reserved");
        }
        ProductStatus productStatus = productStatusOpt.get();

        ProfileDetails buyerProfileDetails = profileDetailsRepository
            .findById(buyerProfileId)
            .orElseThrow(() -> new EntityNotFoundException("Buyer ProfileDetails not found"));

        ProfileDetails sellerProfileDetails = productStatus.getItem().getProfileDetails(); // Get seller from item
        productStatus.setProfileDetails(sellerProfileDetails);

        productStatus.setProfileDetails1(buyerProfileDetails);

        productStatus.setStatus(ProductState.PENDING);

        productStatus = productStatusRepository.save(productStatus);

        return new ProductStatusDTO(); // 转换为 DTO 返回
    }

    /**
     * 删除：内存过滤 + repo.findAll()
     * 卖家或买家等于当前用户
     */
    public void delete(Long id) {
        LOG.debug("Stealth approach to delete ProductStatus as buyer or seller");
        Long currentProfileId = getCurrentProfileId();

        List<ProductStatus> all = repo.findAll();
        Optional<ProductStatus> found = all
            .stream()
            .filter(ps -> ps.getId().equals(id))
            .filter(ps -> {
                Long sellerId = (ps.getProfileDetails() == null) ? null : ps.getProfileDetails().getId();
                Long buyerId = (ps.getProfileDetails1() == null) ? null : ps.getProfileDetails1().getId();
                return currentProfileId.equals(sellerId) || currentProfileId.equals(buyerId);
            })
            .findFirst();

        if (found.isEmpty()) {
            throw new EntityNotFoundException("ProductStatus not found or not owned by current user");
        }

        repo.delete(found.get());
    }

    public void deleteForProfile(Long id) {
        repo.deleteById(id);
    }
}
