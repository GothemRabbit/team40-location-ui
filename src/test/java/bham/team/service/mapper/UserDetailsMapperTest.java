package bham.team.service.mapper;

import static bham.team.domain.UserDetailsAsserts.*;
import static bham.team.domain.UserDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDetailsMapperTest {

    private UserDetailsMapper userDetailsMapper;

    @BeforeEach
    void setUp() {
        userDetailsMapper = new UserDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserDetailsSample1();
        var actual = userDetailsMapper.toEntity(userDetailsMapper.toDto(expected));
        assertUserDetailsAllPropertiesEquals(expected, actual);
    }
}
