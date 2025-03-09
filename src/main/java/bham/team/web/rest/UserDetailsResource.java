package bham.team.web.rest;

import bham.team.repository.UserDetailsRepository;
import bham.team.service.UserDetailsService;
import bham.team.service.dto.UserDetailsDTO;
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
 * REST controller for managing {@link bham.team.domain.UserDetails}.
 */
@RestController
@RequestMapping("/api/user-details")
public class UserDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsResource.class);

    private static final String ENTITY_NAME = "userDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDetailsService userDetailsService;

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsResource(UserDetailsService userDetailsService, UserDetailsRepository userDetailsRepository) {
        this.userDetailsService = userDetailsService;
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * {@code POST  /user-details} : Create a new userDetails.
     *
     * @param userDetailsDTO the userDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDetailsDTO, or with status {@code 400 (Bad Request)} if the userDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserDetailsDTO> createUserDetails(@Valid @RequestBody UserDetailsDTO userDetailsDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserDetails : {}", userDetailsDTO);
        if (userDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userDetailsDTO = userDetailsService.save(userDetailsDTO);
        return ResponseEntity.created(new URI("/api/user-details/" + userDetailsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userDetailsDTO.getId().toString()))
            .body(userDetailsDTO);
    }

    /**
     * {@code PUT  /user-details/:id} : Updates an existing userDetails.
     *
     * @param id the id of the userDetailsDTO to save.
     * @param userDetailsDTO the userDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the userDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> updateUserDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserDetailsDTO userDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserDetails : {}, {}", id, userDetailsDTO);
        if (userDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userDetailsDTO = userDetailsService.update(userDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDetailsDTO.getId().toString()))
            .body(userDetailsDTO);
    }

    /**
     * {@code PATCH  /user-details/:id} : Partial updates given fields of an existing userDetails, field will ignore if it is null
     *
     * @param id the id of the userDetailsDTO to save.
     * @param userDetailsDTO the userDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the userDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserDetailsDTO> partialUpdateUserDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserDetailsDTO userDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserDetails partially : {}, {}", id, userDetailsDTO);
        if (userDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDetailsDTO> result = userDetailsService.partialUpdate(userDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-details} : get all the userDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDetails in body.
     */
    @GetMapping("")
    public List<UserDetailsDTO> getAllUserDetails(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all UserDetails");
        return userDetailsService.findAll();
    }

    /**
     * {@code GET  /user-details/:id} : get the "id" userDetails.
     *
     * @param id the id of the userDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> getUserDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserDetails : {}", id);
        Optional<UserDetailsDTO> userDetailsDTO = userDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDetailsDTO);
    }

    /**
     * {@code DELETE  /user-details/:id} : delete the "id" userDetails.
     *
     * @param id the id of the userDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserDetails : {}", id);
        userDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
