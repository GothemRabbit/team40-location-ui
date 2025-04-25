package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Location;
import bham.team.domain.User;
import bham.team.domain.UserDetails;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.LocationDTO;
import bham.team.service.dto.UserDTO;
import bham.team.service.dto.UserDetailsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDetails} and its DTO {@link UserDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserDetailsMapper extends EntityMapper<UserDetailsDTO, UserDetails> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "meetupLocations", source = "meetupLocations", qualifiedByName = "locationIdSet")
    @Mapping(target = "chats", source = "chats", qualifiedByName = "conversationIdSet")
    UserDetailsDTO toDto(UserDetails s);

    @Mapping(target = "removeMeetupLocations", ignore = true)
    @Mapping(target = "chats", ignore = true)
    UserDetails toEntity(UserDetailsDTO userDetailsDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);

    @Named("locationIdSet")
    default Set<LocationDTO> toDtoLocationIdSet(Set<Location> location) {
        return location.stream().map(this::toDtoLocationId).collect(Collectors.toSet());
    }

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);

    @Named("conversationIdSet")
    default Set<ConversationDTO> toDtoConversationIdSet(Set<Conversation> conversation) {
        return conversation.stream().map(this::toDtoConversationId).collect(Collectors.toSet());
    }
}
