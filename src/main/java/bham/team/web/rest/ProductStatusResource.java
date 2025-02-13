package bham.team.web.rest;

import bham.team.domain.ProductStatus;
import bham.team.repository.ProductStatusRepository;
import bham.team.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.ProductStatus}.
 */
@RestController
@RequestMapping("/api/product-statuses")
@Transactional
public class ProductStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductStatusResource.class);

    private static final String ENTITY_NAME = "productStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductStatusRepository productStatusRepository;

    public ProductStatusResource(ProductStatusRepository productStatusRepository) {
        this.productStatusRepository = productStatusRepository;
    }

    /**
     * {@code POST  /product-statuses} : Create a new productStatus.
     *
     * @param productStatus the productStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productStatus, or with status {@code 400 (Bad Request)} if the productStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductStatus> createProductStatus(@Valid @RequestBody ProductStatus productStatus) throws URISyntaxException {
        LOG.debug("REST request to save ProductStatus : {}", productStatus);
        if (productStatus.getId() != null) {
            throw new BadRequestAlertException("A new productStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productStatus = productStatusRepository.save(productStatus);
        return ResponseEntity.created(new URI("/api/product-statuses/" + productStatus.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, productStatus.getId().toString()))
            .body(productStatus);
    }

    /**
     * {@code PUT  /product-statuses/:id} : Updates an existing productStatus.
     *
     * @param id the id of the productStatus to save.
     * @param productStatus the productStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productStatus,
     * or with status {@code 400 (Bad Request)} if the productStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductStatus> updateProductStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductStatus productStatus
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductStatus : {}, {}", id, productStatus);
        if (productStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productStatus = productStatusRepository.save(productStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productStatus.getId().toString()))
            .body(productStatus);
    }

    /**
     * {@code PATCH  /product-statuses/:id} : Partial updates given fields of an existing productStatus, field will ignore if it is null
     *
     * @param id the id of the productStatus to save.
     * @param productStatus the productStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productStatus,
     * or with status {@code 400 (Bad Request)} if the productStatus is not valid,
     * or with status {@code 404 (Not Found)} if the productStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the productStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductStatus> partialUpdateProductStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductStatus productStatus
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductStatus partially : {}, {}", id, productStatus);
        if (productStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductStatus> result = productStatusRepository
            .findById(productStatus.getId())
            .map(existingProductStatus -> {
                if (productStatus.getStatus() != null) {
                    existingProductStatus.setStatus(productStatus.getStatus());
                }
                if (productStatus.getMeetingTime() != null) {
                    existingProductStatus.setMeetingTime(productStatus.getMeetingTime());
                }
                if (productStatus.getMeetingLocation() != null) {
                    existingProductStatus.setMeetingLocation(productStatus.getMeetingLocation());
                }
                if (productStatus.getChatLink() != null) {
                    existingProductStatus.setChatLink(productStatus.getChatLink());
                }
                if (productStatus.getCreatedAt() != null) {
                    existingProductStatus.setCreatedAt(productStatus.getCreatedAt());
                }
                if (productStatus.getUpdatedAt() != null) {
                    existingProductStatus.setUpdatedAt(productStatus.getUpdatedAt());
                }

                return existingProductStatus;
            })
            .map(productStatusRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /product-statuses} : get all the productStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productStatuses in body.
     */
    @GetMapping("")
    public List<ProductStatus> getAllProductStatuses() {
        LOG.debug("REST request to get all ProductStatuses");
        return productStatusRepository.findAll();
    }

    /**
     * {@code GET  /product-statuses/:id} : get the "id" productStatus.
     *
     * @param id the id of the productStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductStatus> getProductStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductStatus : {}", id);
        Optional<ProductStatus> productStatus = productStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productStatus);
    }

    /**
     * {@code DELETE  /product-statuses/:id} : delete the "id" productStatus.
     *
     * @param id the id of the productStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductStatus : {}", id);
        productStatusRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
