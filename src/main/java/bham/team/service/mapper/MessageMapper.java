package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Message;
import bham.team.domain.ProfileDetails;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.MessageDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    @Mapping(target = "profileDetails", source = "profileDetails", qualifiedByName = "profileDetailsId")
    @Mapping(target = "username", source = "profileDetails.userName")
    MessageDTO toDto(Message s);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);

    @Named("profileDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDetailsDTO toDtoProfileDetailsId(ProfileDetails profileDetails);
}
