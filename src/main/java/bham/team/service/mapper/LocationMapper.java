package bham.team.service.mapper;

import bham.team.domain.Location;
import bham.team.domain.UserDetails;
import bham.team.service.dto.LocationDTO;
import bham.team.service.dto.UserDetailsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "users", source = "users", qualifiedByName = "userDetailsIdSet")
    LocationDTO toDto(Location s);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);

    @Named("userDetailsIdSet")
    default Set<UserDetailsDTO> toDtoUserDetailsIdSet(Set<UserDetails> userDetails) {
        return userDetails.stream().map(this::toDtoUserDetailsId).collect(Collectors.toSet());
    }
}
