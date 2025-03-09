package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
import static bham.team.domain.ProductStatusTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConversationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conversation.class);
        Conversation conversation1 = getConversationSample1();
        Conversation conversation2 = new Conversation();
        assertThat(conversation1).isNotEqualTo(conversation2);

        conversation2.setId(conversation1.getId());
        assertThat(conversation1).isEqualTo(conversation2);

        conversation2 = getConversationSample2();
        assertThat(conversation1).isNotEqualTo(conversation2);
    }

    @Test
    void participantsTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        conversation.addParticipants(userDetailsBack);
        assertThat(conversation.getParticipants()).containsOnly(userDetailsBack);

        conversation.removeParticipants(userDetailsBack);
        assertThat(conversation.getParticipants()).doesNotContain(userDetailsBack);

        conversation.participants(new HashSet<>(Set.of(userDetailsBack)));
        assertThat(conversation.getParticipants()).containsOnly(userDetailsBack);

        conversation.setParticipants(new HashSet<>());
        assertThat(conversation.getParticipants()).doesNotContain(userDetailsBack);
    }

    @Test
    void productStatusTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        ProductStatus productStatusBack = getProductStatusRandomSampleGenerator();

        conversation.setProductStatus(productStatusBack);
        assertThat(conversation.getProductStatus()).isEqualTo(productStatusBack);
        assertThat(productStatusBack.getConversation()).isEqualTo(conversation);

        conversation.productStatus(null);
        assertThat(conversation.getProductStatus()).isNull();
        assertThat(productStatusBack.getConversation()).isNull();
    }

    @Test
    void messagesTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        Message messageBack = getMessageRandomSampleGenerator();

        conversation.addMessages(messageBack);
        assertThat(conversation.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getConvo()).isEqualTo(conversation);

        conversation.removeMessages(messageBack);
        assertThat(conversation.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getConvo()).isNull();

        conversation.messages(new HashSet<>(Set.of(messageBack)));
        assertThat(conversation.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getConvo()).isEqualTo(conversation);

        conversation.setMessages(new HashSet<>());
        assertThat(conversation.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getConvo()).isNull();
    }
}
