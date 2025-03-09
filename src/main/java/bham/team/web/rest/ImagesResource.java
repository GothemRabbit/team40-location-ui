package bham.team.web.rest;

import bham.team.repository.ImagesRepository;
import bham.team.service.ImagesService;
import bham.team.service.dto.ImagesDTO;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.Images}.
 */
@RestController
@RequestMapping("/api/images")
public class ImagesResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImagesResource.class);

    private static final String ENTITY_NAME = "images";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagesService imagesService;

    private final ImagesRepository imagesRepository;

    public ImagesResource(ImagesService imagesService, ImagesRepository imagesRepository) {
        this.imagesService = imagesService;
        this.imagesRepository = imagesRepository;
    }

    /**
     * {@code POST  /images} : Create a new images.
     *
     * @param imagesDTO the imagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagesDTO, or with status {@code 400 (Bad Request)} if the images has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImagesDTO> createImages(@Valid @RequestBody ImagesDTO imagesDTO) throws URISyntaxException {
        LOG.debug("REST request to save Images : {}", imagesDTO);
        if (imagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new images cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imagesDTO = imagesService.save(imagesDTO);
        return ResponseEntity.created(new URI("/api/images/" + imagesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, imagesDTO.getId().toString()))
            .body(imagesDTO);
    }

    /**
     * {@code PUT  /images/:id} : Updates an existing images.
     *
     * @param id the id of the imagesDTO to save.
     * @param imagesDTO the imagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagesDTO,
     * or with status {@code 400 (Bad Request)} if the imagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImagesDTO> updateImages(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImagesDTO imagesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Images : {}, {}", id, imagesDTO);
        if (imagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imagesDTO = imagesService.update(imagesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imagesDTO.getId().toString()))
            .body(imagesDTO);
    }

    /**
     * {@code PATCH  /images/:id} : Partial updates given fields of an existing images, field will ignore if it is null
     *
     * @param id the id of the imagesDTO to save.
     * @param imagesDTO the imagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagesDTO,
     * or with status {@code 400 (Bad Request)} if the imagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImagesDTO> partialUpdateImages(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImagesDTO imagesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Images partially : {}, {}", id, imagesDTO);
        if (imagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImagesDTO> result = imagesService.partialUpdate(imagesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imagesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /images} : get all the images.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of images in body.
     */
    @GetMapping("")
    public List<ImagesDTO> getAllImages() {
        LOG.debug("REST request to get all Images");
        return imagesService.findAll();
    }

    /**
     * {@code GET  /images/:id} : get the "id" images.
     *
     * @param id the id of the imagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImagesDTO> getImages(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Images : {}", id);
        Optional<ImagesDTO> imagesDTO = imagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imagesDTO);
    }

    /**
     * {@code DELETE  /images/:id} : delete the "id" images.
     *
     * @param id the id of the imagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImages(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Images : {}", id);
        imagesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
