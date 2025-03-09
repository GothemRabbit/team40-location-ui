package bham.team.service;

import bham.team.domain.UserDetails;
import bham.team.repository.UserDetailsRepository;
import bham.team.service.dto.UserDetailsDTO;
import bham.team.service.mapper.UserDetailsMapper;
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
 * Service Implementation for managing {@link bham.team.domain.UserDetails}.
 */
@Service
@Transactional
public class UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsService.class);

    private final UserDetailsRepository userDetailsRepository;

    private final UserDetailsMapper userDetailsMapper;

    public UserDetailsService(UserDetailsRepository userDetailsRepository, UserDetailsMapper userDetailsMapper) {
        this.userDetailsRepository = userDetailsRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    /**
     * Save a userDetails.
     *
     * @param userDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDetailsDTO save(UserDetailsDTO userDetailsDTO) {
        LOG.debug("Request to save UserDetails : {}", userDetailsDTO);
        UserDetails userDetails = userDetailsMapper.toEntity(userDetailsDTO);
        userDetails = userDetailsRepository.save(userDetails);
        return userDetailsMapper.toDto(userDetails);
    }

    /**
     * Update a userDetails.
     *
     * @param userDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDetailsDTO update(UserDetailsDTO userDetailsDTO) {
        LOG.debug("Request to update UserDetails : {}", userDetailsDTO);
        UserDetails userDetails = userDetailsMapper.toEntity(userDetailsDTO);
        userDetails = userDetailsRepository.save(userDetails);
        return userDetailsMapper.toDto(userDetails);
    }

    /**
     * Partially update a userDetails.
     *
     * @param userDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserDetailsDTO> partialUpdate(UserDetailsDTO userDetailsDTO) {
        LOG.debug("Request to partially update UserDetails : {}", userDetailsDTO);

        return userDetailsRepository
            .findById(userDetailsDTO.getId())
            .map(existingUserDetails -> {
                userDetailsMapper.partialUpdate(existingUserDetails, userDetailsDTO);

                return existingUserDetails;
            })
            .map(userDetailsRepository::save)
            .map(userDetailsMapper::toDto);
    }

    /**
     * Get all the userDetails.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserDetailsDTO> findAll() {
        LOG.debug("Request to get all UserDetails");
        return userDetailsRepository.findAll().stream().map(userDetailsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the userDetails with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserDetailsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userDetailsRepository.findAllWithEagerRelationships(pageable).map(userDetailsMapper::toDto);
    }

    /**
     * Get one userDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDetailsDTO> findOne(Long id) {
        LOG.debug("Request to get UserDetails : {}", id);
        return userDetailsRepository.findOneWithEagerRelationships(id).map(userDetailsMapper::toDto);
    }

    /**
     * Delete the userDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserDetails : {}", id);
        userDetailsRepository.deleteById(id);
    }
}
