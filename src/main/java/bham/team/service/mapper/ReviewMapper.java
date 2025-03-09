package bham.team.service.mapper;

import bham.team.domain.Review;
import bham.team.domain.UserDetails;
import bham.team.service.dto.ReviewDTO;
import bham.team.service.dto.UserDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "buyer", source = "buyer", qualifiedByName = "userDetailsId")
    @Mapping(target = "seller", source = "seller", qualifiedByName = "userDetailsId")
    ReviewDTO toDto(Review s);

    @Named("userDetailsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDetailsDTO toDtoUserDetailsId(UserDetails userDetails);
}
