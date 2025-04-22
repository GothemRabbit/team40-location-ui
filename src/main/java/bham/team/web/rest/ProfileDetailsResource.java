package bham.team.web.rest;

import bham.team.domain.Review;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.service.ProfileDetailsService;
import bham.team.service.ReviewService;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.dto.ReviewDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.ProfileDetails}.
 */
@RestController
@RequestMapping("/api/profile-details")
public class ProfileDetailsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileDetailsResource.class);

    private static final String ENTITY_NAME = "profileDetails";
    private final ReviewService reviewService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfileDetailsService profileDetailsService;

    private final ProfileDetailsRepository profileDetailsRepository;

    public ProfileDetailsResource(
        ProfileDetailsService profileDetailsService,
        ProfileDetailsRepository profileDetailsRepository,
        ReviewService reviewService
    ) {
        this.profileDetailsService = profileDetailsService;
        this.profileDetailsRepository = profileDetailsRepository;
        this.reviewService = reviewService;
    }

    /**
     * {@code POST  /profile-details} : Create a new profileDetails.
     *
     * @param profileDetailsDTO the profileDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profileDetailsDTO, or with status {@code 400 (Bad Request)} if the profileDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfileDetailsDTO> createProfileDetails(@Valid @RequestBody ProfileDetailsDTO profileDetailsDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfileDetails : {}", profileDetailsDTO);
        if (profileDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new profileDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        profileDetailsDTO = profileDetailsService.save(profileDetailsDTO);
        return ResponseEntity.created(new URI("/api/profile-details/" + profileDetailsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, profileDetailsDTO.getId().toString()))
            .body(profileDetailsDTO);
    }

    /**
     * {@code PUT  /profile-details/:id} : Updates an existing profileDetails.
     *
     * @param id the id of the profileDetailsDTO to save.
     * @param profileDetailsDTO the profileDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profileDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the profileDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profileDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfileDetailsDTO> updateProfileDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfileDetailsDTO profileDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfileDetails : {}, {}", id, profileDetailsDTO);
        if (profileDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profileDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        profileDetailsDTO = profileDetailsService.update(profileDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, profileDetailsDTO.getId().toString()))
            .body(profileDetailsDTO);
    }

    /**
     * {@code PATCH  /profile-details/:id} : Partial updates given fields of an existing profileDetails, field will ignore if it is null
     *
     * @param id the id of the profileDetailsDTO to save.
     * @param profileDetailsDTO the profileDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profileDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the profileDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profileDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profileDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfileDetailsDTO> partialUpdateProfileDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfileDetailsDTO profileDetailsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfileDetails partially : {}, {}", id, profileDetailsDTO);
        if (profileDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profileDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profileDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfileDetailsDTO> result = profileDetailsService.partialUpdate(profileDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, profileDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /profile-details} : get all the profileDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profileDetails in body.
     */
    @GetMapping("")
    public List<ProfileDetailsDTO> getAllProfileDetails(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ProfileDetails");
        return profileDetailsService.findAll();
    }

    /**
     * {@code GET  /profile-details/:id} : get the "id" profileDetails.
     *
     * @param id the id of the profileDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profileDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDetailsDTO> getProfileDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfileDetails : {}", id);
        Optional<ProfileDetailsDTO> profileDetailsDTO = profileDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profileDetailsDTO);
    }

    @GetMapping("/public-profile/{userName}")
    public ResponseEntity<ProfileDetailsDTO> getProfileByUserName(@PathVariable String userName) {
        Optional<ProfileDetailsDTO> profileOpt = profileDetailsService.findByUserName(userName); // you’ll define this in service

        if (profileOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ProfileDetailsDTO profile = profileOpt.get();

        // Get the currently logged-in user's login name
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLogin = (authentication != null) ? authentication.getName() : null;

        boolean isOwner = currentLogin != null && profile.getUser() != null && currentLogin.equals(profile.getUser().getLogin());

        //        if (!isOwner) {
        //            profile.setLocation(null);
        //            profile.setEmail(null);
        //            // hide other private fields here
        //        }

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/current")
    public ResponseEntity<ProfileDetailsDTO> getCurrentUserProfile() {
        Optional<ProfileDetailsDTO> profileDetailsDTO = profileDetailsService.getCurrentUserProfile();
        return profileDetailsDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /profile-details/:id} : delete the "id" profileDetails.
     *
     * @param id the id of the profileDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileDetails(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfileDetails : {}", id);
        profileDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<ProfileDetailsDTO> getProfileWithItems(@PathVariable Long id) {
        Optional<ProfileDetailsDTO> profileDetails = profileDetailsService.findProfileWithItems(id);
        return profileDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/review")
    public List<ReviewDTO> getReview(@PathVariable Long id) {
        LOG.debug("REST request to get Review : {}", id);
        return reviewService.findReviewByRetailerID(id);
    }
}
