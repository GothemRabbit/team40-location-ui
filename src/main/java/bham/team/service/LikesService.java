package bham.team.service;

import bham.team.domain.Item;
import bham.team.domain.Likes;
import bham.team.domain.ProfileDetails;
import bham.team.repository.LikesRepository;
import bham.team.service.dto.LikesDTO;
import bham.team.service.mapper.LikesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.Likes}.
 */
@Service
@Transactional
public class LikesService {

    private static final Logger LOG = LoggerFactory.getLogger(LikesService.class);

    private final LikesRepository likesRepository;

    private final LikesMapper likesMapper;

    public LikesService(LikesRepository likesRepository, LikesMapper likesMapper) {
        this.likesRepository = likesRepository;
        this.likesMapper = likesMapper;
    }

    /**
     * Save a likes.
     *
     * @param likesDTO the entity to save.
     * @return the persisted entity.
     */
    public LikesDTO save(LikesDTO likesDTO) {
        LOG.debug("Request to save Likes : {}", likesDTO);
        Likes likes = likesMapper.toEntity(likesDTO);
        likes = likesRepository.save(likes);
        return likesMapper.toDto(likes);
    }

    /**
     * Update a likes.
     *
     * @param likesDTO the entity to save.
     * @return the persisted entity.
     */
    public LikesDTO update(LikesDTO likesDTO) {
        LOG.debug("Request to update Likes : {}", likesDTO);
        Likes likes = likesMapper.toEntity(likesDTO);
        likes = likesRepository.save(likes);
        return likesMapper.toDto(likes);
    }

    /**
     * Partially update a likes.
     *
     * @param likesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LikesDTO> partialUpdate(LikesDTO likesDTO) {
        LOG.debug("Request to partially update Likes : {}", likesDTO);

        return likesRepository
            .findById(likesDTO.getId())
            .map(existingLikes -> {
                likesMapper.partialUpdate(existingLikes, likesDTO);

                return existingLikes;
            })
            .map(likesRepository::save)
            .map(likesMapper::toDto);
    }

    /**
     * Get all the likes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LikesDTO> findAll() {
        LOG.debug("Request to get all Likes");
        return likesRepository.findAll().stream().map(likesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one likes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LikesDTO> findOne(Long id) {
        LOG.debug("Request to get Likes : {}", id);
        return likesRepository.findById(id).map(likesMapper::toDto);
    }

    /**
     * Delete the likes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Likes : {}", id);
        likesRepository.deleteById(id);
    }

    /**
     * Like an item.
     *
     * @param itemId the id of the item.
     * @param profileId the id of the profile.
     * @return the persisted LikesDTO.
     */
    public LikesDTO likeItem(Long itemId, Long profileId) {
        LOG.debug("Request to like item: {} by profile: {}", itemId, profileId);
        Likes likes = new Likes();
        Item item = new Item();
        item.setId(itemId);
        ProfileDetails profile = new ProfileDetails();
        profile.setId(profileId);

        likes.setItem(item);
        likes.setProfileDetails(profile);

        Likes savedLike = likesRepository.save(likes);
        return likesMapper.toDto(savedLike);
    }

    /**
     * Unlike an item.
     *
     * @param itemId the id of the item.
     * @param profileId the id of the profile.
     */
    public void unlikeItem(Long itemId, Long profileId) {
        LOG.debug("Request to unlike item: {} by profile: {}", itemId, profileId);
        likesRepository.deleteByItemIdAndProfileDetailsId(itemId, profileId);
    }

    /**
     * Get the number of likes for an item.
     *
     * @param itemId the id of the item.
     * @return the number of likes.
     */
    @Transactional(readOnly = true)
    public int getLikesCount(Long itemId) {
        LOG.debug("Request to get likes count for item: {}", itemId);
        return likesRepository.countLikesByItemId(itemId);
    }

    /**
     * Check if a profile has liked an item.
     *
     * @param itemId the id of the item.
     * @param profileId the id of the profile.
     * @return true if liked, false otherwise.
     */
    @Transactional(readOnly = true)
    public boolean checkIfLiked(Long itemId, Long profileId) {
        LOG.debug("Request to check if profile: {} liked item: {}", profileId, itemId);
        return likesRepository.existsByItemIdAndProfileDetailsId(itemId, profileId);
    }
}
