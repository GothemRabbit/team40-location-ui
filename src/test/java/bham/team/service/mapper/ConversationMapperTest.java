package bham.team.service.mapper;

import static bham.team.domain.ConversationAsserts.*;
import static bham.team.domain.ConversationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConversationMapperTest {

    private ConversationMapper conversationMapper;

    @BeforeEach
    void setUp() {
        conversationMapper = new ConversationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConversationSample1();
        var actual = conversationMapper.toEntity(conversationMapper.toDto(expected));
        assertConversationAllPropertiesEquals(expected, actual);
    }
}
