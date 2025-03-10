package bham.team.service.mapper;

import static bham.team.domain.ProductStatusAsserts.*;
import static bham.team.domain.ProductStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductStatusMapperTest {

    private ProductStatusMapper productStatusMapper;

    @BeforeEach
    void setUp() {
        productStatusMapper = new ProductStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductStatusSample1();
        var actual = productStatusMapper.toEntity(productStatusMapper.toDto(expected));
        assertProductStatusAllPropertiesEquals(expected, actual);
    }
}
