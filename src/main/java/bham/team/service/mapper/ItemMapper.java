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
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ImagesMapper.class })
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "wishlists", source = "wishlists", qualifiedByName = "wishlistIdSet")
    @Mapping(target = "profileDetails", source = "profileDetails", qualifiedByName = "profileDetailsUsername")
    @Mapping(target = "seller", source = "seller", qualifiedByName = "userDetailsId")
    @Mapping(target = "images", source = "images")
    ItemDTO toDto(Item s);

    @Mapping(target = "removeWishlist", ignore = true)
    Item toEntity(ItemDTO itemDTO);

    @Named("wishlistId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WishlistDTO toDtoWishlistId(Wishlist wishlist);

    @Named("wishlistIdSet")
    default Set<WishlistDTO> toDtoWishlistIdSet(Set<Wishlist> wishlist) {
        return wishlist.stream().map(this::toDtoWishlistId).collect(Collectors.toSet());
    }

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);

    @Named("profileDetailsUsername")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userName", source = "userName")
    ProfileDetailsDTO toDtoProfileDetailsUsername(ProfileDetails profileDetails);
}
