package bham.team.service.mapper;

import static bham.team.domain.ImagesAsserts.*;
import static bham.team.domain.ImagesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImagesMapperTest {

    private ImagesMapper imagesMapper;

    @BeforeEach
    void setUp() {
        imagesMapper = new ImagesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImagesSample1();
        var actual = imagesMapper.toEntity(imagesMapper.toDto(expected));
        assertImagesAllPropertiesEquals(expected, actual);
    }
}
