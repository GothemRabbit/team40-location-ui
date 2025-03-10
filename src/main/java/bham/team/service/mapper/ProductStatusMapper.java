package bham.team.service.mapper;

import bham.team.domain.Conversation;
import bham.team.domain.Item;
import bham.team.domain.Location;
import bham.team.domain.ProductStatus;
import bham.team.domain.ProfileDetails;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.ItemDTO;
import bham.team.service.dto.LocationDTO;
import bham.team.service.dto.ProductStatusDTO;
import bham.team.service.dto.ProfileDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductStatus} and its DTO {@link ProductStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductStatusMapper extends EntityMapper<ProductStatusDTO, ProductStatus> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    @Mapping(target = "conversation", source = "conversation", qualifiedByName = "conversationId")
    @Mapping(target = "profileDetails", source = "profileDetails", qualifiedByName = "profileDetailsId")
    @Mapping(target = "location", source = "location", qualifiedByName = "locationId")
    ProductStatusDTO toDto(ProductStatus s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);

    @Named("conversationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversationDTO toDtoConversationId(Conversation conversation);

    @Named("profileDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDetailsDTO toDtoProfileDetailsId(ProfileDetails profileDetails);

    @Named("locationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocationDTO toDtoLocationId(Location location);
}
