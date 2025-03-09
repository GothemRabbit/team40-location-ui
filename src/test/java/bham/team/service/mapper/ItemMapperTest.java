package bham.team.service.mapper;

import static bham.team.domain.ItemAsserts.*;
import static bham.team.domain.ItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemMapperTest {

    private ItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        itemMapper = new ItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getItemSample1();
        var actual = itemMapper.toEntity(itemMapper.toDto(expected));
        assertItemAllPropertiesEquals(expected, actual);
    }
}
