package bham.team.web.rest;

import bham.team.domain.UserDetails;
import bham.team.repository.UserDetailsRepository;
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
 * REST controller for managing {@link bham.team.domain.UserDetails}.
 */
@RestController
@RequestMapping("/api/user-details")
@Transactional
public class UserDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsResource.class);

    private static final String ENTITY_NAME = "userDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsResource(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * {@code POST  /user-details} : Create a new userDetails.
     *
     * @param userDetails the userDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDetails, or with status {@code 400 (Bad Request)} if the userDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserDetails> createUserDetails(@Valid @RequestBody UserDetails userDetails) throws URISyntaxException {
        LOG.debug("REST request to save UserDetails : {}", userDetails);
        if (userDetails.getId() != null) {
            throw new BadRequestAlertException("A new userDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userDetails = userDetailsRepository.save(userDetails);
        return ResponseEntity.created(new URI("/api/user-details/" + userDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userDetails.getId().toString()))
            .body(userDetails);
    }

    /**
     * {@code PUT  /user-details/:id} : Updates an existing userDetails.
     *
     * @param id the id of the userDetails to save.
     * @param userDetails the userDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetails,
     * or with status {@code 400 (Bad Request)} if the userDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDetails> updateUserDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserDetails userDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserDetails : {}, {}", id, userDetails);
        if (userDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userDetails = userDetailsRepository.save(userDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDetails.getId().toString()))
            .body(userDetails);
    }

    /**
     * {@code PATCH  /user-details/:id} : Partial updates given fields of an existing userDetails, field will ignore if it is null
     *
     * @param id the id of the userDetails to save.
     * @param userDetails the userDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetails,
     * or with status {@code 400 (Bad Request)} if the userDetails is not valid,
     * or with status {@code 404 (Not Found)} if the userDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserDetails> partialUpdateUserDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserDetails userDetails
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserDetails partially : {}, {}", id, userDetails);
        if (userDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDetails> result = userDetailsRepository
            .findById(userDetails.getId())
            .map(existingUserDetails -> {
                if (userDetails.getBioImage() != null) {
                    existingUserDetails.setBioImage(userDetails.getBioImage());
                }
                if (userDetails.getBioImageContentType() != null) {
                    existingUserDetails.setBioImageContentType(userDetails.getBioImageContentType());
                }
                if (userDetails.getUserName() != null) {
                    existingUserDetails.setUserName(userDetails.getUserName());
                }
                if (userDetails.getFirstName() != null) {
                    existingUserDetails.setFirstName(userDetails.getFirstName());
                }
                if (userDetails.getLastName() != null) {
                    existingUserDetails.setLastName(userDetails.getLastName());
                }
                if (userDetails.getGender() != null) {
                    existingUserDetails.setGender(userDetails.getGender());
                }
                if (userDetails.getBirthDate() != null) {
                    existingUserDetails.setBirthDate(userDetails.getBirthDate());
                }
                if (userDetails.getEmail() != null) {
                    existingUserDetails.setEmail(userDetails.getEmail());
                }
                if (userDetails.getPhoneNumber() != null) {
                    existingUserDetails.setPhoneNumber(userDetails.getPhoneNumber());
                }
                if (userDetails.getPreferences() != null) {
                    existingUserDetails.setPreferences(userDetails.getPreferences());
                }
                if (userDetails.getRating() != null) {
                    existingUserDetails.setRating(userDetails.getRating());
                }
                if (userDetails.getAddress() != null) {
                    existingUserDetails.setAddress(userDetails.getAddress());
                }

                return existingUserDetails;
            })
            .map(userDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /user-details} : get all the userDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDetails in body.
     */
    @GetMapping("")
    public List<UserDetails> getAllUserDetails() {
        LOG.debug("REST request to get all UserDetails");
        return userDetailsRepository.findAll();
    }

    /**
     * {@code GET  /user-details/:id} : get the "id" userDetails.
     *
     * @param id the id of the userDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserDetails : {}", id);
        Optional<UserDetails> userDetails = userDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userDetails);
    }

    /**
     * {@code DELETE  /user-details/:id} : delete the "id" userDetails.
     *
     * @param id the id of the userDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserDetails : {}", id);
        userDetailsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
