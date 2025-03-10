package bham.team.service.mapper;

import static bham.team.domain.WishlistAsserts.*;
import static bham.team.domain.WishlistTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WishlistMapperTest {

    private WishlistMapper wishlistMapper;

    @BeforeEach
    void setUp() {
        wishlistMapper = new WishlistMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWishlistSample1();
        var actual = wishlistMapper.toEntity(wishlistMapper.toDto(expected));
        assertWishlistAllPropertiesEquals(expected, actual);
    }
}
