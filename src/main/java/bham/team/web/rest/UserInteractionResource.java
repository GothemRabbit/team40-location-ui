package bham.team.web.rest;

import bham.team.domain.UserInteraction;
import bham.team.repository.UserInteractionRepository;
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
 * REST controller for managing {@link bham.team.domain.UserInteraction}.
 */
@RestController
@RequestMapping("/api/user-interactions")
@Transactional
public class UserInteractionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserInteractionResource.class);

    private static final String ENTITY_NAME = "userInteraction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserInteractionRepository userInteractionRepository;

    public UserInteractionResource(UserInteractionRepository userInteractionRepository) {
        this.userInteractionRepository = userInteractionRepository;
    }

    /**
     * {@code POST  /user-interactions} : Create a new userInteraction.
     *
     * @param userInteraction the userInteraction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userInteraction, or with status {@code 400 (Bad Request)} if the userInteraction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserInteraction> createUserInteraction(@Valid @RequestBody UserInteraction userInteraction)
        throws URISyntaxException {
        LOG.debug("REST request to save UserInteraction : {}", userInteraction);
        if (userInteraction.getId() != null) {
            throw new BadRequestAlertException("A new userInteraction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userInteraction = userInteractionRepository.save(userInteraction);
        return ResponseEntity.created(new URI("/api/user-interactions/" + userInteraction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userInteraction.getId().toString()))
            .body(userInteraction);
    }

    /**
     * {@code PUT  /user-interactions/:id} : Updates an existing userInteraction.
     *
     * @param id the id of the userInteraction to save.
     * @param userInteraction the userInteraction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userInteraction,
     * or with status {@code 400 (Bad Request)} if the userInteraction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userInteraction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserInteraction> updateUserInteraction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserInteraction userInteraction
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserInteraction : {}, {}", id, userInteraction);
        if (userInteraction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userInteraction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userInteractionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userInteraction = userInteractionRepository.save(userInteraction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userInteraction.getId().toString()))
            .body(userInteraction);
    }

    /**
     * {@code PATCH  /user-interactions/:id} : Partial updates given fields of an existing userInteraction, field will ignore if it is null
     *
     * @param id the id of the userInteraction to save.
     * @param userInteraction the userInteraction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userInteraction,
     * or with status {@code 400 (Bad Request)} if the userInteraction is not valid,
     * or with status {@code 404 (Not Found)} if the userInteraction is not found,
     * or with status {@code 500 (Internal Server Error)} if the userInteraction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserInteraction> partialUpdateUserInteraction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserInteraction userInteraction
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserInteraction partially : {}, {}", id, userInteraction);
        if (userInteraction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userInteraction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userInteractionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserInteraction> result = userInteractionRepository
            .findById(userInteraction.getId())
            .map(existingUserInteraction -> {
                if (userInteraction.getType() != null) {
                    existingUserInteraction.setType(userInteraction.getType());
                }
                if (userInteraction.getDetails() != null) {
                    existingUserInteraction.setDetails(userInteraction.getDetails());
                }
                if (userInteraction.getInteractionDate() != null) {
                    existingUserInteraction.setInteractionDate(userInteraction.getInteractionDate());
                }

                return existingUserInteraction;
            })
            .map(userInteractionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userInteraction.getId().toString())
        );
    }

    /**
     * {@code GET  /user-interactions} : get all the userInteractions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userInteractions in body.
     */
    @GetMapping("")
    public List<UserInteraction> getAllUserInteractions() {
        LOG.debug("REST request to get all UserInteractions");
        return userInteractionRepository.findAll();
    }

    /**
     * {@code GET  /user-interactions/:id} : get the "id" userInteraction.
     *
     * @param id the id of the userInteraction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userInteraction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserInteraction> getUserInteraction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserInteraction : {}", id);
        Optional<UserInteraction> userInteraction = userInteractionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userInteraction);
    }

    /**
     * {@code DELETE  /user-interactions/:id} : delete the "id" userInteraction.
     *
     * @param id the id of the userInteraction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserInteraction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserInteraction : {}", id);
        userInteractionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
