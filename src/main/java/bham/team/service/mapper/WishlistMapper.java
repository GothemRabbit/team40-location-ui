package bham.team.service.mapper;

import bham.team.domain.Item;
import bham.team.domain.ProfileDetails;
import bham.team.domain.UserDetails;
import bham.team.domain.Wishlist;
import bham.team.service.dto.ItemDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import bham.team.service.dto.UserDetailsDTO;
import bham.team.service.dto.WishlistDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Wishlist} and its DTO {@link WishlistDTO}.
 */
@Mapper(componentModel = "spring")
public interface WishlistMapper extends EntityMapper<WishlistDTO, Wishlist> {
    @Mapping(target = "profileDetails", source = "profileDetails", qualifiedByName = "profileDetailsId")
    @Mapping(target = "items", source = "items", qualifiedByName = "itemIdSet")
    @Mapping(target = "userDetails", source = "userDetails", qualifiedByName = "userDetailsId")
    WishlistDTO toDto(Wishlist s);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "removeItem", ignore = true)
    Wishlist toEntity(WishlistDTO wishlistDTO);

    @Named("profileDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDetailsDTO toDtoProfileDetailsId(ProfileDetails profileDetails);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);

    @Named("itemIdSet")
    default Set<ItemDTO> toDtoItemIdSet(Set<Item> item) {
        return item.stream().map(this::toDtoItemId).collect(Collectors.toSet());
    }

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);
}
