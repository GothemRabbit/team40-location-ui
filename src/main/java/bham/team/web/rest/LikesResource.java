package bham.team.web.rest;

import bham.team.repository.LikesRepository;
import bham.team.service.LikesService;
import bham.team.service.dto.LikesDTO;
import bham.team.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link bham.team.domain.Likes}.
 */
@RestController
@RequestMapping("/api/likes")
public class LikesResource {

    private static final Logger LOG = LoggerFactory.getLogger(LikesResource.class);

    private static final String ENTITY_NAME = "likes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikesService likesService;

    private final LikesRepository likesRepository;

    public LikesResource(LikesService likesService, LikesRepository likesRepository) {
        this.likesService = likesService;
        this.likesRepository = likesRepository;
    }

    /**
     * {@code POST  /likes} : Create a new likes.
     *
     * @param likesDTO the likesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likesDTO, or with status {@code 400 (Bad Request)} if the likes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LikesDTO> createLikes(@RequestBody LikesDTO likesDTO) throws URISyntaxException {
        LOG.debug("REST request to save Likes : {}", likesDTO);
        if (likesDTO.getId() != null) {
            throw new BadRequestAlertException("A new likes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        likesDTO = likesService.save(likesDTO);
        return ResponseEntity.created(new URI("/api/likes/" + likesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, likesDTO.getId().toString()))
            .body(likesDTO);
    }

    /**
     * {@code PUT  /likes/:id} : Updates an existing likes.
     *
     * @param id the id of the likesDTO to save.
     * @param likesDTO the likesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likesDTO,
     * or with status {@code 400 (Bad Request)} if the likesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LikesDTO> updateLikes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikesDTO likesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Likes : {}, {}", id, likesDTO);
        if (likesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        likesDTO = likesService.update(likesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, likesDTO.getId().toString()))
            .body(likesDTO);
    }

    /**
     * {@code PATCH  /likes/:id} : Partial updates given fields of an existing likes, field will ignore if it is null
     *
     * @param id the id of the likesDTO to save.
     * @param likesDTO the likesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likesDTO,
     * or with status {@code 400 (Bad Request)} if the likesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikesDTO> partialUpdateLikes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikesDTO likesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Likes partially : {}, {}", id, likesDTO);
        if (likesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikesDTO> result = likesService.partialUpdate(likesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, likesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /likes} : get all the likes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likes in body.
     */
    @GetMapping("")
    public List<LikesDTO> getAllLikes() {
        LOG.debug("REST request to get all Likes");
        return likesService.findAll();
    }

    /**
     * {@code GET  /likes/:id} : get the "id" likes.
     *
     * @param id the id of the likesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LikesDTO> getLikes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Likes : {}", id);
        Optional<LikesDTO> likesDTO = likesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likesDTO);
    }

    /**
     * {@code DELETE  /likes/:id} : delete the "id" likes.
     *
     * @param id the id of the likesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLikes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Likes : {}", id);
        likesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("api/likes")
    public ResponseEntity<LikesDTO> toggleLike(@RequestParam Long itemId, @RequestParam Long profileId) {
        LOG.debug("REST request to toggle Like for item: {}, profile: {}", itemId, profileId);
        LikesDTO updatedLike = likesService.toggleLike(itemId, profileId);
        return ResponseEntity.ok(updatedLike);
    }
}
