package bham.team.service;

import bham.team.domain.ProductStatus;
import bham.team.domain.ProfileDetails;
import bham.team.repository.ProductStatusRepository;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.mapper.ProductStatusMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
        return repo.findByProfileDetailsId(getCurrentProfileId(), pageable).map(mapper::toDto);
    }

    /**
     * 查询单条记录：也只查当前用户ID
     */
    @Transactional(readOnly = true)
    public Optional<ProductStatusDTO> findOne(Long id) {
        LOG.debug("Request to get ProductStatus : {}", id);
        return repo.findByIdAndProfileDetailsId(id, getCurrentProfileId()).map(mapper::toDto);
    }

    /**
     * 删除：也只删当前用户ID下的订单
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductStatus : {}", id);
        int deleted = repo.deleteByIdAndProfileDetailsId(id, getCurrentProfileId());
        if (deleted == 0) {
            throw new EntityNotFoundException("ProductStatus not found or not owned by current user");
        }
    }
}
