package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Item;
import bham.team.domain.Location;
import bham.team.domain.ProductStatus;
import bham.team.domain.UserDetails;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.ItemDTO;
import bham.team.service.dto.LocationDTO;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.dto.UserDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductStatus} and its DTO {@link ProductStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductStatusMapper extends EntityMapper<ProductStatusDTO, ProductStatus> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "userDetailsId")
    @Mapping(target = "seller", source = "seller", qualifiedByName = "userDetailsId")
    @Mapping(target = "meetingLocation", source = "meetingLocation", qualifiedByName = "locationId")
    ProductStatusDTO toDto(ProductStatus s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);
}
