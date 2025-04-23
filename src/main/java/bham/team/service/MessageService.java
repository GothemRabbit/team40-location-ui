package bham.team.service;

import bham.team.domain.Message;
import bham.team.domain.ProfileDetails;
import bham.team.domain.User;
import bham.team.repository.MessageRepository;
import bham.team.repository.ProfileDetailsRepository;
import bham.team.repository.UserRepository;
import bham.team.security.SecurityUtils;
import bham.team.service.dto.MessageDTO;
import bham.team.service.mapper.MessageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    private final ProfileDetailsRepository profileDetailsRepository;

    private final UserRepository userRepository;

    public MessageService(
        MessageRepository messageRepository,
        MessageMapper messageMapper,
        ProfileDetailsRepository profileDetailsRepository,
        UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.profileDetailsRepository = profileDetailsRepository;
        this.userRepository = userRepository;
    }

    public MessageDTO save(MessageDTO messageDTO) {
        LOG.debug("Request to save Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);

        if (message.getProfileDetails() == null || message.getProfileDetails().getId() == null) {
            String currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new RuntimeException("User is not logged in"));
            User user = userRepository.findOneByLogin(currentLogin).orElseThrow(() -> new RuntimeException("User not found"));
            ProfileDetails pd = profileDetailsRepository
                .findProfileDetailsByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("ProfileDetails not found for user"));
            message.setProfileDetails(pd);
        }

        if (message.getIsRead() == null) {
            message.setIsRead(false);
        }

        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    public MessageDTO update(MessageDTO messageDTO) {
        LOG.debug("Request to update Message : {}", messageDTO);
        Message message = messageMapper.toEntity(messageDTO);
        message = messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    public Optional<MessageDTO> partialUpdate(MessageDTO messageDTO) {
        LOG.debug("Request to partially update Message : {}", messageDTO);

        return messageRepository
            .findById(messageDTO.getId())
            .map(existingMessage -> {
                messageMapper.partialUpdate(existingMessage, messageDTO);

                return existingMessage;
            })
            .map(messageRepository::save)
            .map(messageMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> findAll() {
        LOG.debug("Request to get all Messages");
        return messageRepository.findAll().stream().map(messageMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<MessageDTO> findOne(Long id) {
        LOG.debug("Request to get Message : {}", id);
        return messageRepository.findById(id).map(messageMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Message : {}", id);
        messageRepository.deleteById(id);
    }
}
