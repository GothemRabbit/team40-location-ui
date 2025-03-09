package bham.team.service;

import bham.team.domain.Wishlist;
import bham.team.repository.WishlistRepository;
import bham.team.service.dto.WishlistDTO;
import bham.team.service.mapper.WishlistMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.Wishlist}.
 */
@Service
@Transactional
public class WishlistService {

    private static final Logger LOG = LoggerFactory.getLogger(WishlistService.class);

    private final WishlistRepository wishlistRepository;

    private final WishlistMapper wishlistMapper;

    public WishlistService(WishlistRepository wishlistRepository, WishlistMapper wishlistMapper) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistMapper = wishlistMapper;
    }

    /**
     * Save a wishlist.
     *
     * @param wishlistDTO the entity to save.
     * @return the persisted entity.
     */
    public WishlistDTO save(WishlistDTO wishlistDTO) {
        LOG.debug("Request to save Wishlist : {}", wishlistDTO);
        Wishlist wishlist = wishlistMapper.toEntity(wishlistDTO);
        wishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toDto(wishlist);
    }

    /**
     * Update a wishlist.
     *
     * @param wishlistDTO the entity to save.
     * @return the persisted entity.
     */
    public WishlistDTO update(WishlistDTO wishlistDTO) {
        LOG.debug("Request to update Wishlist : {}", wishlistDTO);
        Wishlist wishlist = wishlistMapper.toEntity(wishlistDTO);
        wishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toDto(wishlist);
    }

    /**
     * Partially update a wishlist.
     *
     * @param wishlistDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WishlistDTO> partialUpdate(WishlistDTO wishlistDTO) {
        LOG.debug("Request to partially update Wishlist : {}", wishlistDTO);

        return wishlistRepository
            .findById(wishlistDTO.getId())
            .map(existingWishlist -> {
                wishlistMapper.partialUpdate(existingWishlist, wishlistDTO);

                return existingWishlist;
            })
            .map(wishlistRepository::save)
            .map(wishlistMapper::toDto);
    }

    /**
     * Get all the wishlists.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WishlistDTO> findAll() {
        LOG.debug("Request to get all Wishlists");
        return wishlistRepository.findAll().stream().map(wishlistMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one wishlist by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WishlistDTO> findOne(Long id) {
        LOG.debug("Request to get Wishlist : {}", id);
        return wishlistRepository.findById(id).map(wishlistMapper::toDto);
    }

    /**
     * Delete the wishlist by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Wishlist : {}", id);
        wishlistRepository.deleteById(id);
    }
}
