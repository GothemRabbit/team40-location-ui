package bham.team.service;

import bham.team.domain.Item;
import bham.team.repository.ImagesRepository;
import bham.team.repository.ItemRepository;
import bham.team.service.dto.ItemDTO;
import bham.team.service.mapper.ItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.Item}.
 */
@Service
@Transactional
public class ItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private ImagesRepository imagesRepository;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Save a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemDTO save(ItemDTO itemDTO) {
        LOG.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * Update a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemDTO update(ItemDTO itemDTO) {
        LOG.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    /**
     * Partially update a item.
     *
     * @param itemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        LOG.debug("Request to partially update Item : {}", itemDTO);

        return itemRepository
            .findById(itemDTO.getId())
            .map(existingItem -> {
                itemMapper.partialUpdate(existingItem, itemDTO);

                return existingItem;
            })
            .map(itemRepository::save)
            .map(itemMapper::toDto);
    }

    /**
     * Get all the items with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return itemRepository.findAllWithEagerRelationships(pageable).map(itemMapper::toDto);
    }

    /**
     *  Get all the items where ProductStatus is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemDTO> findAllWhereProductStatusIsNull() {
        LOG.debug("Request to get all items where ProductStatus is null");
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
            .filter(item -> item.getProductStatus() == null)
            .map(itemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Optional<ItemDTO> findOneWithLikes(Long id) {
        return itemRepository.findOneWithLikes(id).map(itemMapper::toDto); // Assuming itemMapper is used to map the entity to DTO
    }

    /**
     * Get one item by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    //    @Transactional(readOnly = true)
    //    public Optional<ItemDTO> findOne(Long id) {
    //        LOG.debug("Request to get Item : {}", id);
    //        return itemRepository.findOneWithEagerRelationships(id).map(itemMapper::toDto);
    //    }
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        LOG.debug("Request to get Item with images: {}", id);
        return itemRepository.findByIdWithImages(id).map(itemMapper::toDto);
    }

    /**
     * Delete the item by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }

    public Optional<Item> getItemWithImages(Long itemId) {
        return itemRepository.findByIdWithImages(itemId);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findAllWithImages() {
        LOG.debug("Request to get all Items with images");
        return itemRepository.findAllWithImages().stream().map(itemMapper::toDto).collect(Collectors.toList());
    }
}
