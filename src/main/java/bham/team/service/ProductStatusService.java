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

    // 依赖注入：用来获取当前用户Profile
    private final ProfileDetailsService profileDetailsService;

    public ProductStatusService(ProductStatusRepository repo, ProductStatusMapper mapper, ProfileDetailsService profileDetailsService) {
        this.repo = repo;
        this.mapper = mapper;
        this.profileDetailsService = profileDetailsService;
    }

    private Long getCurrentProfileId() {
        return profileDetailsService
            .getCurrentUserProfile() // 参考你队友的代码
            .orElseThrow(() -> new RuntimeException("User not logged in or has no ProfileDetails"))
            .getId();
    }

    public ProductStatusDTO save(ProductStatusDTO dto) {
        LOG.debug("Request to save ProductStatus : {}", dto);
        ProductStatus entity = mapper.toEntity(dto);
        // 无论前端传什么profileDetailsId，我们都覆盖为当前用户的
        entity.setProfileDetails(new ProfileDetails());
        entity.getProfileDetails().setId(getCurrentProfileId());

        entity = repo.save(entity);
        return mapper.toDto(entity);
    }

    public ProductStatusDTO update(ProductStatusDTO dto) {
        LOG.debug("Request to update ProductStatus : {}", dto);
        Long currentProfileId = getCurrentProfileId();

        // 检查数据库中是否确实是当前用户的记录
        repo
            .findByIdAndProfileDetailsId(dto.getId(), currentProfileId)
            .orElseThrow(() -> new EntityNotFoundException("ProductStatus not found or not owned by current user"));

        ProductStatus entity = mapper.toEntity(dto);
        // 再次强制当前用户
        entity.setProfileDetails(new ProfileDetails());
        entity.getProfileDetails().setId(currentProfileId);

        entity = repo.save(entity);
        return mapper.toDto(entity);
    }

    public Optional<ProductStatusDTO> partialUpdate(ProductStatusDTO dto) {
        LOG.debug("Request to partially update ProductStatus : {}", dto);
        Long currentProfileId = getCurrentProfileId();

        return repo
            .findByIdAndProfileDetailsId(dto.getId(), currentProfileId)
            .map(existingEntity -> {
                // 使用MapStruct的partialUpdate(...)
                mapper.partialUpdate(existingEntity, dto);
                return existingEntity;
            })
            .map(repo::save)
            .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductStatusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProductStatuses for current user");
        Long currentProfileId = getCurrentProfileId();
        return repo.findByProfileDetailsId(currentProfileId, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ProductStatusDTO> findOne(Long id) {
        LOG.debug("Request to get ProductStatus : {}", id);
        return repo.findByIdAndProfileDetailsId(id, getCurrentProfileId()).map(mapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete ProductStatus : {}", id);
        int deleted = repo.deleteByIdAndProfileDetailsId(id, getCurrentProfileId());
        if (deleted == 0) {
            throw new EntityNotFoundException("ProductStatus not found or not owned by current user");
        }
    }
}
