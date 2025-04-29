package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Location;
import bham.team.domain.ProfileDetails;
import bham.team.domain.User;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.LocationDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.dto.UserDTO;
import java.util.HashSet;
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
    UserDTO toDtoUserId(User user);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);

    @Named("locationIdSet")
    default Set<LocationDTO> toDtoLocationIdSet(Set<Location> location) {
        if (location == null) return new HashSet<>();
        return location.stream().map(this::toDtoLocationId).collect(Collectors.toSet());
    }

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);

    @Named("conversationIdSet")
    default Set<ConversationDTO> toDtoConversationIdSet(Set<Conversation> conversation) {
        if (conversation == null) return new HashSet<>();
        return conversation.stream().map(this::toDtoConversationId).collect(Collectors.toSet());
    }
}
