package bham.team.service;

import bham.team.domain.ProfileDetails;
import bham.team.domain.User;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.mapper.ProfileDetailsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.ProfileDetails}.
 */
@Service
@Transactional
public class ProfileDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileDetailsService.class);

    private final ProfileDetailsRepository profileDetailsRepository;

    private final ProfileDetailsMapper profileDetailsMapper;

    public ProfileDetailsService(ProfileDetailsRepository profileDetailsRepository, ProfileDetailsMapper profileDetailsMapper) {
        this.profileDetailsRepository = profileDetailsRepository;
        this.profileDetailsMapper = profileDetailsMapper;
    }

    /**
     * Save a profileDetails.
     *
     * @param profileDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfileDetailsDTO save(ProfileDetailsDTO profileDetailsDTO) {
        LOG.debug("Request to save ProfileDetails : {}", profileDetailsDTO);
        ProfileDetails profileDetails = profileDetailsMapper.toEntity(profileDetailsDTO);
        profileDetails = profileDetailsRepository.save(profileDetails);
        return profileDetailsMapper.toDto(profileDetails);
    }

    /**
     * Update a profileDetails.
     *
     * @param profileDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfileDetailsDTO update(ProfileDetailsDTO profileDetailsDTO) {
        LOG.debug("Request to update ProfileDetails : {}", profileDetailsDTO);
        ProfileDetails profileDetails = profileDetailsMapper.toEntity(profileDetailsDTO);
        profileDetails = profileDetailsRepository.save(profileDetails);
        return profileDetailsMapper.toDto(profileDetails);
    }

    /**
     * Partially update a profileDetails.
     *
     * @param profileDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfileDetailsDTO> partialUpdate(ProfileDetailsDTO profileDetailsDTO) {
        LOG.debug("Request to partially update ProfileDetails : {}", profileDetailsDTO);

        return profileDetailsRepository
            .findById(profileDetailsDTO.getId())
            .map(existingProfileDetails -> {
                profileDetailsMapper.partialUpdate(existingProfileDetails, profileDetailsDTO);

                return existingProfileDetails;
            })
            .map(profileDetailsRepository::save)
            .map(profileDetailsMapper::toDto);
    }

    /**
     * Get all the profileDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDetailsDTO> findAll() {
        LOG.debug("Request to get all ProfileDetails");
        return profileDetailsRepository
            .findAll()
            .stream()
            .map(profileDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Optional<ProfileDetailsDTO> findByUserName(String userName) {
        return profileDetailsRepository.findByUserName(userName).map(profileDetailsMapper::toDto);
    }

    /**
     * Get all the profileDetails with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProfileDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return profileDetailsRepository.findAllWithEagerRelationships(pageable).map(profileDetailsMapper::toDto);
    }

    /**
     * Get one profileDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfileDetailsDTO> findOne(Long id) {
        LOG.debug("Request to get ProfileDetails : {}", id);
        return profileDetailsRepository.findOneWithEagerRelationships(id).map(profileDetailsMapper::toDto);
    }

    //    @Transactional(readOnly = true)
    //    public Optional<ProfileDetailsDTO> findOne(Long id) {
    //        return profileDetailsRepository.findByIdWithUser(id)
    //            .map(profileDetails -> {
    //                ProfileDetailsDTO dto = profileDetailsMapper.toDto(profileDetails); // Map all fields
    //                if (profileDetails.getUser() != null) {
    //                    dto.setUserName(profileDetails.getUser().getLogin()); // Manually set username
    //                }
    //                return dto;
    //            });
    //    }

    /**
     * Delete the profileDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileDetails : {}", id);
        profileDetailsRepository.deleteById(id);
    }

    //    @Transactional(readOnly = true)
    //    public List<ItemDTO> findAllItemsByProfile(Long profileId) {
    //        LOG.debug("Request to get all Items for ProfileDetails: {}", profileId);
    //        return profileDetailsRepository.findProfileWithItems(profileId)
    //            .map(profile -> itemMapper.toDto(profile.getItems()))  // Convert items to DTO
    //            .orElse(Collections.emptyList());  // Return empty list if profile not found
    //    }

    @Transactional(readOnly = true)
    public Optional<ProfileDetailsDTO> findProfileWithItems(Long profileId) {
        LOG.debug("Request to get ProfileDetails with Items: {}", profileId);
        return profileDetailsRepository.findProfileWithItems(profileId).map(profileDetailsMapper::toDto); // Convert profile to DTO including items
    }

    @Transactional
    public ProfileDetails createProfileForUser(User user) {
        ProfileDetails profile = new ProfileDetails();
        profile.setUser(user); // Ensure the relationship is established
        return profileDetailsRepository.save(profile);
    }
}
