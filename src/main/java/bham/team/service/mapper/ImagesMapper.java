package bham.team.service.mapper;

import bham.team.domain.Images;
import bham.team.domain.Item;
import bham.team.service.dto.ImagesDTO;
import bham.team.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Images} and its DTO {@link ImagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImagesMapper extends EntityMapper<ImagesDTO, Images> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    ImagesDTO toDto(Images s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);
}
