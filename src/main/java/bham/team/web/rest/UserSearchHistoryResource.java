package bham.team.web.rest;

import bham.team.domain.UserSearchHistory;
import bham.team.repository.UserSearchHistoryRepository;
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
 * REST controller for managing {@link bham.team.domain.UserSearchHistory}.
 */
@RestController
@RequestMapping("/api/user-search-histories")
@Transactional
public class UserSearchHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserSearchHistoryResource.class);

    private static final String ENTITY_NAME = "userSearchHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSearchHistoryRepository userSearchHistoryRepository;

    public UserSearchHistoryResource(UserSearchHistoryRepository userSearchHistoryRepository) {
        this.userSearchHistoryRepository = userSearchHistoryRepository;
    }

    /**
     * {@code POST  /user-search-histories} : Create a new userSearchHistory.
     *
     * @param userSearchHistory the userSearchHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSearchHistory, or with status {@code 400 (Bad Request)} if the userSearchHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserSearchHistory> createUserSearchHistory(@Valid @RequestBody UserSearchHistory userSearchHistory)
        throws URISyntaxException {
        LOG.debug("REST request to save UserSearchHistory : {}", userSearchHistory);
        if (userSearchHistory.getId() != null) {
            throw new BadRequestAlertException("A new userSearchHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSearchHistory = userSearchHistoryRepository.save(userSearchHistory);
        return ResponseEntity.created(new URI("/api/user-search-histories/" + userSearchHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userSearchHistory.getId().toString()))
            .body(userSearchHistory);
    }

    /**
     * {@code PUT  /user-search-histories/:id} : Updates an existing userSearchHistory.
     *
     * @param id the id of the userSearchHistory to save.
     * @param userSearchHistory the userSearchHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSearchHistory,
     * or with status {@code 400 (Bad Request)} if the userSearchHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSearchHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSearchHistory> updateUserSearchHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSearchHistory userSearchHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserSearchHistory : {}, {}", id, userSearchHistory);
        if (userSearchHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSearchHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSearchHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userSearchHistory = userSearchHistoryRepository.save(userSearchHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSearchHistory.getId().toString()))
            .body(userSearchHistory);
    }

    /**
     * {@code PATCH  /user-search-histories/:id} : Partial updates given fields of an existing userSearchHistory, field will ignore if it is null
     *
     * @param id the id of the userSearchHistory to save.
     * @param userSearchHistory the userSearchHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSearchHistory,
     * or with status {@code 400 (Bad Request)} if the userSearchHistory is not valid,
     * or with status {@code 404 (Not Found)} if the userSearchHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSearchHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSearchHistory> partialUpdateUserSearchHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSearchHistory userSearchHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserSearchHistory partially : {}, {}", id, userSearchHistory);
        if (userSearchHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSearchHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSearchHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSearchHistory> result = userSearchHistoryRepository
            .findById(userSearchHistory.getId())
            .map(existingUserSearchHistory -> {
                if (userSearchHistory.getSearchQuery() != null) {
                    existingUserSearchHistory.setSearchQuery(userSearchHistory.getSearchQuery());
                }
                if (userSearchHistory.getFilters() != null) {
                    existingUserSearchHistory.setFilters(userSearchHistory.getFilters());
                }
                if (userSearchHistory.getSearchDate() != null) {
                    existingUserSearchHistory.setSearchDate(userSearchHistory.getSearchDate());
                }

                return existingUserSearchHistory;
            })
            .map(userSearchHistoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSearchHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /user-search-histories} : get all the userSearchHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSearchHistories in body.
     */
    @GetMapping("")
    public List<UserSearchHistory> getAllUserSearchHistories() {
        LOG.debug("REST request to get all UserSearchHistories");
        return userSearchHistoryRepository.findAll();
    }

    /**
     * {@code GET  /user-search-histories/:id} : get the "id" userSearchHistory.
     *
     * @param id the id of the userSearchHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSearchHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSearchHistory> getUserSearchHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserSearchHistory : {}", id);
        Optional<UserSearchHistory> userSearchHistory = userSearchHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userSearchHistory);
    }

    /**
     * {@code DELETE  /user-search-histories/:id} : delete the "id" userSearchHistory.
     *
     * @param id the id of the userSearchHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSearchHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserSearchHistory : {}", id);
        userSearchHistoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
