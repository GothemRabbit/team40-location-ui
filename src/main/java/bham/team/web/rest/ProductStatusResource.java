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

    /**
     * {@code POST  /product-statuses} : Create a new productStatus.
     *
     * @param productStatusDTO the productStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productStatusDTO, or with status {@code 400 (Bad Request)} if the productStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductStatusDTO> createProductStatus(@Valid @RequestBody ProductStatusDTO productStatusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductStatus : {}", productStatusDTO);
        if (productStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new productStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productStatusDTO = productStatusService.save(productStatusDTO);
        return ResponseEntity.created(new URI("/api/product-statuses/" + productStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, productStatusDTO.getId().toString()))
            .body(productStatusDTO);
    }

    /**
     * {@code PUT  /product-statuses/:id} : Updates an existing productStatus.
     *
     * @param id the id of the productStatusDTO to save.
     * @param productStatusDTO the productStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productStatusDTO,
     * or with status {@code 400 (Bad Request)} if the productStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

        productStatusDTO = productStatusService.update(productStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, productStatusDTO.getId().toString()))
            .body(productStatusDTO);
    }

    /**
     * {@code PATCH  /product-statuses/:id} : Partial updates given fields of an existing productStatus, field will ignore if it is null
     *
     * @param id the id of the productStatusDTO to save.
     * @param productStatusDTO the productStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productStatusDTO,
     * or with status {@code 400 (Bad Request)} if the productStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
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

    /**
     * {@code GET  /product-statuses} : get all the productStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductStatusDTO>> getAllProductStatuses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ProductStatuses");
        Page<ProductStatusDTO> page = productStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-statuses/:id} : get the "id" productStatus.
     *
     * @param id the id of the productStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductStatusDTO> getProductStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductStatus : {}", id);
        Optional<ProductStatusDTO> productStatusDTO = productStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productStatusDTO);
    }

    /**
     * {@code DELETE  /product-statuses/:id} : delete the "id" productStatus.
     *
     * @param id the id of the productStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductStatus : {}", id);
        productStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
