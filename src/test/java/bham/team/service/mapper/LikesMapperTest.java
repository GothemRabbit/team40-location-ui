package bham.team.service.mapper;

import static bham.team.domain.LikesAsserts.*;
import static bham.team.domain.LikesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LikesMapperTest {

    private LikesMapper likesMapper;

    @BeforeEach
    void setUp() {
        likesMapper = new LikesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLikesSample1();
        var actual = likesMapper.toEntity(likesMapper.toDto(expected));
        assertLikesAllPropertiesEquals(expected, actual);
    }
}
