package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Message;
import bham.team.domain.UserDetails;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.MessageDTO;
import bham.team.service.dto.UserDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "convo", source = "convo", qualifiedByName = "conversationId")
    @Mapping(target = "sender", source = "sender", qualifiedByName = "userDetailsId")
    MessageDTO toDto(Message s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);
}
