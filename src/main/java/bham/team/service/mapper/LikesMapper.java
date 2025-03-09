package bham.team.service.mapper;

import bham.team.domain.Item;
import bham.team.domain.Likes;
import bham.team.domain.UserDetails;
import bham.team.service.dto.ItemDTO;
import bham.team.service.dto.LikesDTO;
import bham.team.service.dto.UserDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Likes} and its DTO {@link LikesDTO}.
 */
@Mapper(componentModel = "spring")
public interface LikesMapper extends EntityMapper<LikesDTO, Likes> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userDetailsId")
    LikesDTO toDto(Likes s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);
}
