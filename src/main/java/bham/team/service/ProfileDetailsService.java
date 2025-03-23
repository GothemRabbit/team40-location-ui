package bham.team.service;

import bham.team.domain.ProfileDetails;
import bham.team.domain.User;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.repository.UserRepository;
import bham.team.security.SecurityUtils;
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
    private UserService userService;
    private final UserRepository userRepository;

    public ProfileDetailsService(
        ProfileDetailsRepository profileDetailsRepository,
        ProfileDetailsMapper profileDetailsMapper,
        UserService userService,
        UserRepository userRepository
    ) {
        this.profileDetailsRepository = profileDetailsRepository;
        this.profileDetailsMapper = profileDetailsMapper;
        this.userService = userService;
        this.userRepository = userRepository;
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

    public Optional<ProfileDetailsDTO> getCurrentUserProfile() {
        String currentUserlogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User is not logged in"));

        User user = userRepository.findOneByLogin(currentUserlogin).orElseThrow(() -> new RuntimeException("User not found"));

        return profileDetailsRepository.findProfileDetailsByUserId(user.getId()).map(profileDetailsMapper::toDto);
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

    /**
     * Delete the profileDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfileDetails : {}", id);
        ProfileDetails profileDetails = profileDetailsRepository.getReferenceById(id);
        User user = profileDetails.getUser();
        userService.deleteUser(user.getLogin());
        profileDetailsRepository.deleteById(id);
    }
}
