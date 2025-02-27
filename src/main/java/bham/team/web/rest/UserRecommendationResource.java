package bham.team.web.rest;

import bham.team.domain.UserRecommendation;
import bham.team.repository.UserRecommendationRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.UserRecommendation}.
 */
@RestController
@RequestMapping("/api/user-recommendations")
@Transactional
public class UserRecommendationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserRecommendationResource.class);

    private static final String ENTITY_NAME = "userRecommendation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserRecommendationRepository userRecommendationRepository;

    public UserRecommendationResource(UserRecommendationRepository userRecommendationRepository) {
        this.userRecommendationRepository = userRecommendationRepository;
    }

    /**
     * {@code POST  /user-recommendations} : Create a new userRecommendation.
     *
     * @param userRecommendation the userRecommendation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userRecommendation, or with status {@code 400 (Bad Request)} if the userRecommendation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserRecommendation> createUserRecommendation(@RequestBody UserRecommendation userRecommendation)
        throws URISyntaxException {
        LOG.debug("REST request to save UserRecommendation : {}", userRecommendation);
        if (userRecommendation.getId() != null) {
            throw new BadRequestAlertException("A new userRecommendation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userRecommendation = userRecommendationRepository.save(userRecommendation);
        return ResponseEntity.created(new URI("/api/user-recommendations/" + userRecommendation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userRecommendation.getId().toString()))
            .body(userRecommendation);
    }

    /**
     * {@code PUT  /user-recommendations/:id} : Updates an existing userRecommendation.
     *
     * @param id the id of the userRecommendation to save.
     * @param userRecommendation the userRecommendation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRecommendation,
     * or with status {@code 400 (Bad Request)} if the userRecommendation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userRecommendation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserRecommendation> updateUserRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRecommendation userRecommendation
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserRecommendation : {}, {}", id, userRecommendation);
        if (userRecommendation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRecommendation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRecommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userRecommendation = userRecommendationRepository.save(userRecommendation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userRecommendation.getId().toString()))
            .body(userRecommendation);
    }

    /**
     * {@code PATCH  /user-recommendations/:id} : Partial updates given fields of an existing userRecommendation, field will ignore if it is null
     *
     * @param id the id of the userRecommendation to save.
     * @param userRecommendation the userRecommendation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userRecommendation,
     * or with status {@code 400 (Bad Request)} if the userRecommendation is not valid,
     * or with status {@code 404 (Not Found)} if the userRecommendation is not found,
     * or with status {@code 500 (Internal Server Error)} if the userRecommendation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserRecommendation> partialUpdateUserRecommendation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserRecommendation userRecommendation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserRecommendation partially : {}, {}", id, userRecommendation);
        if (userRecommendation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userRecommendation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userRecommendationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserRecommendation> result = userRecommendationRepository
            .findById(userRecommendation.getId())
            .map(existingUserRecommendation -> {
                if (userRecommendation.getReason() != null) {
                    existingUserRecommendation.setReason(userRecommendation.getReason());
                }

                return existingUserRecommendation;
            })
            .map(userRecommendationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userRecommendation.getId().toString())
        );
    }

    /**
     * {@code GET  /user-recommendations} : get all the userRecommendations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userRecommendations in body.
     */
    @GetMapping("")
    public List<UserRecommendation> getAllUserRecommendations() {
        LOG.debug("REST request to get all UserRecommendations");
        return userRecommendationRepository.findAll();
    }

    /**
     * {@code GET  /user-recommendations/:id} : get the "id" userRecommendation.
     *
     * @param id the id of the userRecommendation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userRecommendation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserRecommendation> getUserRecommendation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserRecommendation : {}", id);
        Optional<UserRecommendation> userRecommendation = userRecommendationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userRecommendation);
    }

    /**
     * {@code DELETE  /user-recommendations/:id} : delete the "id" userRecommendation.
     *
     * @param id the id of the userRecommendation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRecommendation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserRecommendation : {}", id);
        userRecommendationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
