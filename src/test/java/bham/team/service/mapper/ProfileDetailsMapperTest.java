package bham.team.service.mapper;

import static bham.team.domain.ProfileDetailsAsserts.*;
import static bham.team.domain.ProfileDetailsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfileDetailsMapperTest {

    private ProfileDetailsMapper profileDetailsMapper;

    @BeforeEach
    void setUp() {
        profileDetailsMapper = new ProfileDetailsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfileDetailsSample1();
        var actual = profileDetailsMapper.toEntity(profileDetailsMapper.toDto(expected));
        assertProfileDetailsAllPropertiesEquals(expected, actual);
    }
}
