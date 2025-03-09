package bham.team.service;

import bham.team.domain.ProductStatus;
import bham.team.repository.ProductStatusRepository;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.mapper.ProductStatusMapper;
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

    private final ProductStatusRepository productStatusRepository;

    private final ProductStatusMapper productStatusMapper;

    public ProductStatusService(ProductStatusRepository productStatusRepository, ProductStatusMapper productStatusMapper) {
        this.productStatusRepository = productStatusRepository;
        this.productStatusMapper = productStatusMapper;
    }

    /**
     * Save a productStatus.
     *
     * @param productStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductStatusDTO save(ProductStatusDTO productStatusDTO) {
        LOG.debug("Request to save ProductStatus : {}", productStatusDTO);
        ProductStatus productStatus = productStatusMapper.toEntity(productStatusDTO);
        productStatus = productStatusRepository.save(productStatus);
        return productStatusMapper.toDto(productStatus);
    }

    /**
     * Update a productStatus.
     *
     * @param productStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductStatusDTO update(ProductStatusDTO productStatusDTO) {
        LOG.debug("Request to update ProductStatus : {}", productStatusDTO);
        ProductStatus productStatus = productStatusMapper.toEntity(productStatusDTO);
        productStatus = productStatusRepository.save(productStatus);
        return productStatusMapper.toDto(productStatus);
    }

    /**
     * Partially update a productStatus.
     *
     * @param productStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductStatusDTO> partialUpdate(ProductStatusDTO productStatusDTO) {
        LOG.debug("Request to partially update ProductStatus : {}", productStatusDTO);

        return productStatusRepository
            .findById(productStatusDTO.getId())
            .map(existingProductStatus -> {
                productStatusMapper.partialUpdate(existingProductStatus, productStatusDTO);

                return existingProductStatus;
            })
            .map(productStatusRepository::save)
            .map(productStatusMapper::toDto);
    }

    /**
     * Get all the productStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductStatusDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProductStatuses");
        return productStatusRepository.findAll(pageable).map(productStatusMapper::toDto);
    }

    /**
     * Get one productStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductStatusDTO> findOne(Long id) {
        LOG.debug("Request to get ProductStatus : {}", id);
        return productStatusRepository.findById(id).map(productStatusMapper::toDto);
    }

    /**
     * Delete the productStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProductStatus : {}", id);
        productStatusRepository.deleteById(id);
    }
}
