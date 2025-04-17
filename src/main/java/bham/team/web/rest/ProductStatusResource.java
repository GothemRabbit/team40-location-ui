package bham.team.web.rest;

import bham.team.repository.ProductStatusRepository;
import bham.team.service.ProductStatusService;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.ProductStatus}.
 */
@RestController
@RequestMapping("/api/product-statuses")
public class ProductStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductStatusResource.class);

    private static final String ENTITY_NAME = "productStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductStatusService productStatusService;
    private final ProductStatusRepository productStatusRepository;

    public ProductStatusResource(ProductStatusService productStatusService, ProductStatusRepository productStatusRepository) {
        this.productStatusService = productStatusService;
        this.productStatusRepository = productStatusRepository;
    }

    @PostMapping("")
    public ResponseEntity<ProductStatusDTO> createProductStatus(@Valid @RequestBody ProductStatusDTO productStatusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductStatus : {}", productStatusDTO);
        if (productStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new productStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductStatusDTO result = productStatusService.save(productStatusDTO);
        return ResponseEntity.created(new URI("/api/product-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductStatusDTO> updateProductStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductStatusDTO productStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductStatus : {}, {}", id, productStatusDTO);
        if (productStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductStatusDTO result = productStatusService.update(productStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productStatusDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductStatusDTO> partialUpdateProductStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductStatusDTO productStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductStatus partially : {}, {}", id, productStatusDTO);
        if (productStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductStatusDTO> result = productStatusService.partialUpdate(productStatusDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productStatusDTO.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<ProductStatusDTO>> getAllProductStatuses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ProductStatuses");
        Page<ProductStatusDTO> page = productStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductStatusDTO> getProductStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductStatus : {}", id);
        Optional<ProductStatusDTO> productStatusDTO = productStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productStatusDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductStatus : {}", id);
        productStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/{itemId}/reserve")
    public ResponseEntity<ProductStatusDTO> reserveItem(@PathVariable Long itemId, @RequestBody Map<String, Long> request) {
        Long buyerProfileId = request.get("buyerProfileId");
        ProductStatusDTO result = productStatusService.reserveItemInProductStatus(itemId, buyerProfileId);
        return ResponseEntity.ok(result);
    }
}
