package bham.team.service.mapper;

import bham.team.domain.Item;
import bham.team.domain.Likes;
import bham.team.domain.ProfileDetails;
import bham.team.service.dto.ItemDTO;
import bham.team.service.dto.LikesDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Likes} and its DTO {@link LikesDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikesMapper extends EntityMapper<LikesDTO, Likes> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "profileDetails", source = "profileDetails", qualifiedByName = "profileDetailsId")
    LikesDTO toDto(Likes s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);

    @Named("profileDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDetailsDTO toDtoProfileDetailsId(ProfileDetails profileDetails);

    @Named("itemFromId")
    default Item itemFromId(Long id) {
        if (id == null) {
            return null;
        }
        Item item = new Item();
        item.setId(id);
        return item;
    }

    @Named("profileDetailsFromId")
    default ProfileDetails profileDetailsFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileDetails profileDetails = new ProfileDetails();
        profileDetails.setId(id);
        return profileDetails;
    }
}
