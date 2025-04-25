package bham.team.service;

import bham.team.domain.Conversation;
import bham.team.domain.Message;
import bham.team.domain.ProfileDetails;
import bham.team.domain.User;
import bham.team.repository.*;
import bham.team.security.SecurityUtils;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.MessageDTO;
import bham.team.service.mapper.ConversationMapper;
import bham.team.service.mapper.MessageMapper;
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
 * Service Implementation for managing {@link bham.team.domain.Conversation}.
 */
@Service
@Transactional
public class ConversationService {

    private static final Logger LOG = LoggerFactory.getLogger(ConversationService.class);

    private final ConversationRepository conversationRepository;

    private final ConversationMapper conversationMapper;

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    private final ProfileDetailsRepository profileRepo;

    private final UserRepository userRepo;

    public ConversationService(
        ConversationRepository conversationRepository,
        ConversationMapper conversationMapper,
        MessageRepository messageRepository,
        MessageMapper messageMapper,
        ProfileDetailsRepository profileDetailsRepository,
        UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationMapper = conversationMapper;
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.profileRepo = profileDetailsRepository;
        this.userRepo = userRepository;
    }

    /**
     * Save a conversation.
     *
     * @param conversationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversationDTO save(ConversationDTO conversationDTO) {
        LOG.debug("Request to save Conversation : {}", conversationDTO);
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Update a conversation.
     *
     * @param conversationDTO the entity to save.
     * @return the persisted entity.
     */
    public ConversationDTO update(ConversationDTO conversationDTO) {
        LOG.debug("Request to update Conversation : {}", conversationDTO);
        Conversation conversation = conversationMapper.toEntity(conversationDTO);
        conversation = conversationRepository.save(conversation);
        return conversationMapper.toDto(conversation);
    }

    /**
     * Partially update a conversation.
     *
     * @param conversationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConversationDTO> partialUpdate(ConversationDTO conversationDTO) {
        LOG.debug("Request to partially update Conversation : {}", conversationDTO);

        return conversationRepository
            .findById(conversationDTO.getId())
            .map(existingConversation -> {
                conversationMapper.partialUpdate(existingConversation, conversationDTO);

                return existingConversation;
            })
            .map(conversationRepository::save)
            .map(conversationMapper::toDto);
    }

    /**
     * Get all the conversations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConversationDTO> findAll() {
        LOG.debug("Request to get all Conversations");
        return conversationRepository.findAll().stream().map(conversationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the conversations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ConversationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return conversationRepository.findAllWithEagerRelationships(pageable).map(conversationMapper::toDto);
    }

    /**
     *  Get all the conversations where ProductStatus is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConversationDTO> findAllWhereProductStatusIsNull() {
        LOG.debug("Request to get all conversations where ProductStatus is null");
        return StreamSupport.stream(conversationRepository.findAll().spliterator(), false)
            .filter(conversation -> conversation.getProductStatus() == null)
            .map(conversationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one conversation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConversationDTO> findOne(Long id) {
        LOG.debug("Request to get Conversation : {}", id);
        return conversationRepository.findOneWithEagerRelationships(id).map(conversationMapper::toDto);
    }

    /**
     * Delete the conversation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Conversation : {}", id);
        conversationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> findAllMessagesByConversationId(Long conversationId) {
        LOG.debug("Request to get all Messages for Conversation with ID: {}", conversationId);

        conversationRepository
            .findById(conversationId)
            .orElseThrow(() -> new RuntimeException("Conversation not found with id: " + conversationId));

        List<Message> messages = messageRepository.findAllByConversationIdOrderByTimestampAsc(conversationId);
        return messages.stream().map(messageMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConversationDTO> grabMyConvos() {
        String me = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User not logged in"));
        User user = userRepo.findOneByLogin(me).orElseThrow(() -> new RuntimeException("User not found"));
        ProfileDetails vibePd = profileRepo
            .findProfileDetailsByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("ProfileDetails missing"));

        return conversationRepository
            .fetchConvosByProfile(vibePd.getId())
            .stream()
            .map(conversationMapper::toDto)
            .collect(Collectors.toList());
    }
}
