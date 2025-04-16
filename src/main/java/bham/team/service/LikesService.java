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

    public LikesDTO toggleLike(Long itemId, Long profileId) {
        LOG.debug("Request to toggle like for item {} by profile {}", itemId, profileId);

        Optional<Likes> existingLike = likesRepository.findByItemIdAndProfileId(itemId, profileId);

        if (existingLike.isPresent()) {
            likesRepository.delete(existingLike.get());
            LikesDTO dto = new LikesDTO();
            dto.setItemId(itemId);
            dto.setProfileDetailsId(profileId);
            return dto;
        } else {
            Likes newLike = new Likes();
            newLike.setItem(new Item().id(itemId));
            newLike.setProfileDetails(new ProfileDetails().id(profileId));

            Likes savedLike = likesRepository.save(newLike);
            LikesDTO dto = likesMapper.toDto(savedLike);
            return dto;
        }
    }
}
