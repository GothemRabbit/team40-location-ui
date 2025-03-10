package bham.team.service;

import bham.team.domain.Images;
import bham.team.repository.ImagesRepository;
import bham.team.service.dto.ImagesDTO;
import bham.team.service.mapper.ImagesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bham.team.domain.Images}.
 */
@Service
@Transactional
public class ImagesService {

    private static final Logger LOG = LoggerFactory.getLogger(ImagesService.class);

    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    public ImagesService(ImagesRepository imagesRepository, ImagesMapper imagesMapper) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
    }

    /**
     * Save a images.
     *
     * @param imagesDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagesDTO save(ImagesDTO imagesDTO) {
        LOG.debug("Request to save Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        return imagesMapper.toDto(images);
    }

    /**
     * Update a images.
     *
     * @param imagesDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagesDTO update(ImagesDTO imagesDTO) {
        LOG.debug("Request to update Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        return imagesMapper.toDto(images);
    }

    /**
     * Partially update a images.
     *
     * @param imagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImagesDTO> partialUpdate(ImagesDTO imagesDTO) {
        LOG.debug("Request to partially update Images : {}", imagesDTO);

        return imagesRepository
            .findById(imagesDTO.getId())
            .map(existingImages -> {
                imagesMapper.partialUpdate(existingImages, imagesDTO);

                return existingImages;
            })
            .map(imagesRepository::save)
            .map(imagesMapper::toDto);
    }

    /**
     * Get all the images.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ImagesDTO> findAll() {
        LOG.debug("Request to get all Images");
        return imagesRepository.findAll().stream().map(imagesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one images by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImagesDTO> findOne(Long id) {
        LOG.debug("Request to get Images : {}", id);
        return imagesRepository.findById(id).map(imagesMapper::toDto);
    }

    /**
     * Delete the images by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Images : {}", id);
        imagesRepository.deleteById(id);
    }
}
