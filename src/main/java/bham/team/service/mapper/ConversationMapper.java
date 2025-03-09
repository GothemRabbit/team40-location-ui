package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.ProfileDetails;
import bham.team.domain.UserDetails;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.dto.UserDetailsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversation} and its DTO {@link ConversationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConversationMapper extends EntityMapper<ConversationDTO, Conversation> {
    @Mapping(target = "profileDetails", source = "profileDetails", qualifiedByName = "profileDetailsIdSet")
    @Mapping(target = "participants", source = "participants", qualifiedByName = "userDetailsIdSet")
    ConversationDTO toDto(Conversation s);

    @Mapping(target = "removeProfileDetails", ignore = true)
    @Mapping(target = "removeParticipants", ignore = true)
    Conversation toEntity(ConversationDTO conversationDTO);

    @Named("profileDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDetailsDTO toDtoProfileDetailsId(ProfileDetails profileDetails);

    @Named("profileDetailsIdSet")
    default Set<ProfileDetailsDTO> toDtoProfileDetailsIdSet(Set<ProfileDetails> profileDetails) {
        return profileDetails.stream().map(this::toDtoProfileDetailsId).collect(Collectors.toSet());
    }

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);

    @Named("userDetailsIdSet")
    default Set<UserDetailsDTO> toDtoUserDetailsIdSet(Set<UserDetails> userDetails) {
        return userDetails.stream().map(this::toDtoUserDetailsId).collect(Collectors.toSet());
    }
}
