package bham.team.service;

import bham.team.domain.ProductStatus;
import bham.team.domain.ProfileDetails;
import bham.team.repository.ProductStatusRepository;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.mapper.ProductStatusMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ProductStatusService(ProductStatusRepository repo, ProductStatusMapper mapper, ProfileDetailsService profileDetailsService) {
        this.repo = repo;
        this.mapper = mapper;
        this.profileDetailsService = profileDetailsService;
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

        // 先检查是否确实属于当前用户
        repo
            .findByIdAndProfileDetailsId(dto.getId(), currentProfileId)
            .orElseThrow(() -> new EntityNotFoundException("ProductStatus not found or not owned by current user"));

        // 映射并强制改为当前用户
        ProductStatus entity = mapper.toEntity(dto);
        entity.setProfileDetails(new ProfileDetails());
        entity.getProfileDetails().setId(currentProfileId);

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
            .findByIdAndProfileDetailsId(dto.getId(), currentProfileId)
            .map(existingEntity -> {
                mapper.partialUpdate(existingEntity, dto);
                return existingEntity;
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

        // 4) 因为在内存过滤后，原先数据库分页失效了，只能自己封装为一个“无分页”或“本地分页”结果

        // 如果你想直接返回一个 PageImpl：
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

        // 1) 把所有都查出来
        List<ProductStatus> all = repo.findAll();

        // 2) 在内存中找出与 id 匹配、且卖家或买家是当前用户的
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

    /**
     * 删除：内存过滤 + repo.findAll()
     * 卖家或买家等于当前用户
     */
    public void delete(Long id) {
        LOG.debug("Stealth approach to delete ProductStatus as buyer or seller");
        Long currentProfileId = getCurrentProfileId();

        // 1) 把所有查出来
        List<ProductStatus> all = repo.findAll();

        // 2) 找到 id 匹配、且卖家或买家是当前用户的那一条
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

        // 3) 真正执行删除
        repo.delete(found.get());
    }
}
