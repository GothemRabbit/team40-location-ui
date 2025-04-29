package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Location;
import bham.team.domain.ProfileDetails;
import bham.team.domain.User;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.LocationDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileDetails} and its DTO {@link ProfileDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfileDetailsMapper extends EntityMapper<ProfileDetailsDTO, ProfileDetails> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "locations", source = "locations", qualifiedByName = "locationIdSet")
    @Mapping(target = "conversations", source = "conversations", qualifiedByName = "conversationIdSet")
    ProfileDetailsDTO toDto(ProfileDetails s);

    @Mapping(target = "removeLocation", ignore = true)
    @Mapping(target = "conversations", ignore = true)
    @Mapping(target = "removeConversation", ignore = true)
    ProfileDetails toEntity(ProfileDetailsDTO profileDetailsDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    default UserDTO toDtoUserId(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        return dto;
    }

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    default LocationDTO toDtoLocationId(Location location) {
        if (location == null) return null;
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        return dto;
    }

    @Named("locationIdSet")
    default Set<LocationDTO> toDtoLocationIdSet(Set<Location> location) {
        return location == null ? null : location.stream().map(this::toDtoLocationId).collect(Collectors.toSet());
    }

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    default ConversationDTO toDtoConversationId(Conversation conversation) {
        if (conversation == null) return null;
        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        return dto;
    }

    @Named("conversationIdSet")
    default Set<ConversationDTO> toDtoConversationIdSet(Set<Conversation> conversation) {
        return conversation == null ? null : conversation.stream().map(this::toDtoConversationId).collect(Collectors.toSet());
    }
}
