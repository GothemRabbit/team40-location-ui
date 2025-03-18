package bham.team.service;

import bham.team.domain.ProfileDetails;
import bham.team.domain.Review;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.repository.ReviewRepository;
import bham.team.security.SecurityUtils;
import bham.team.service.dto.ReviewDTO;
import bham.team.service.mapper.ReviewMapper;
import bham.team.web.rest.errors.BadRequestAlertException;
import java.nio.file.AccessDeniedException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Review}.
 */
@Service
@Transactional
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final ProfileDetailsRepository profileDetailsRepository;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, ProfileDetailsRepository profileDetailsRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.profileDetailsRepository = profileDetailsRepository;
    }

    /**
     * Save a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewDTO save(ReviewDTO reviewDTO) {
        LOG.debug("Request to save Review : {}", reviewDTO);
        Review review = reviewMapper.toEntity(reviewDTO);
        review = reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    /**
     * Update a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewDTO update(ReviewDTO reviewDTO) {
        Long reviewId = reviewDTO.getId();
        Optional<String> user = SecurityUtils.getCurrentUserLogin();
        Optional<ProfileDetails> profileDetails = Optional.empty();
        if (user.isPresent()) {
            profileDetails = profileDetailsRepository.findByUserName(user.get());
        }

        Long id1 = 0L;
        if (profileDetails.isPresent()) {
            id1 = profileDetails.get().getId(); //this is current users profile id.
        }
        // now profile id on the review
        Optional<Review> review = reviewRepository.findById(reviewId);
        Long id2 = 0L;
        Review review1 = new Review();
        if (review.isPresent()) {
            review1 = review.get();
        }
        id2 = review1.getProfileDetails().getId();
        if (review1.getProfileDetails() == null || !Objects.equals(id1, id2)) {
            try {
                throw new AccessDeniedException("you can not edit this review");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        LOG.debug("Request to update Review : {}", reviewDTO);
        Review review2 = reviewMapper.toEntity(reviewDTO);
        review2 = reviewRepository.save(review2);
        return reviewMapper.toDto(review2);
    }

    /**
     * Partially update a review.
     *
     * @param reviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewDTO> partialUpdate(ReviewDTO reviewDTO) {
        Long reviewId = reviewDTO.getId();
        Optional<String> user = SecurityUtils.getCurrentUserLogin();
        Optional<ProfileDetails> profileDetails = Optional.empty();
        if (user.isPresent()) {
            profileDetails = profileDetailsRepository.findByUserName(user.get());
        }

        Long id1 = 0L;
        if (profileDetails.isPresent()) {
            id1 = profileDetails.get().getId(); //this is current users profile id.
        }
        // now profile id on the review
        Optional<Review> review = reviewRepository.findById(reviewId);
        Long id2 = 0L;
        Review review1 = new Review();
        if (review.isPresent()) {
            review1 = review.get();
        }
        id2 = review1.getProfileDetails().getId();
        if (review1.getProfileDetails() == null || !Objects.equals(id1, id2)) {
            try {
                throw new AccessDeniedException("you can not edit this review");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        LOG.debug("Request to partially update Review : {}", reviewDTO);

        return reviewRepository
            .findById(reviewDTO.getId())
            .map(existingReview -> {
                reviewMapper.partialUpdate(existingReview, reviewDTO);

                return existingReview;
            })
            .map(reviewRepository::save)
            .map(reviewMapper::toDto);
    }

    /**
     * Get all the reviews.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReviewDTO> findAll() {
        LOG.debug("Request to get all Reviews");
        return reviewRepository.findAll().stream().map(reviewMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one review by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findOne(Long id) {
        LOG.debug("Request to get Review : {}", id);
        return reviewRepository.findById(id).map(reviewMapper::toDto);
    }

    /**
     * Delete the review by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        Optional<String> user = SecurityUtils.getCurrentUserLogin();
        Optional<ProfileDetails> profileDetails = Optional.empty();
        if (user.isPresent()) {
            profileDetails = profileDetailsRepository.findByUserName(user.get());
        }

        Long id1 = 0L;
        if (profileDetails.isPresent()) {
            id1 = profileDetails.get().getId(); //this is current users profile id.
        }
        // now profile id on the review
        Optional<Review> review = reviewRepository.findById(id);
        Long id2 = 0L;
        Review review1 = new Review();
        if (review.isPresent()) {
            review1 = review.get();
        }
        id2 = review1.getProfileDetails().getId();
        if (review1.getProfileDetails() == null || !Objects.equals(id1, id2)) {
            try {
                throw new AccessDeniedException("you can not delete this review");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        LOG.debug("Request to delete Review : {}", id);
        reviewRepository.deleteById(id);
    }
}
