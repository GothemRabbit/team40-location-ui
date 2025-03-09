package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = getMessageSample1();
        Message message2 = new Message();
        assertThat(message1).isNotEqualTo(message2);

        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);

        message2 = getMessageSample2();
        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    void convoTest() {
        Message message = getMessageRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        message.setConvo(conversationBack);
        assertThat(message.getConvo()).isEqualTo(conversationBack);

        message.convo(null);
        assertThat(message.getConvo()).isNull();
    }

    @Test
    void senderTest() {
        Message message = getMessageRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        message.setSender(userDetailsBack);
        assertThat(message.getSender()).isEqualTo(userDetailsBack);

        message.sender(null);
        assertThat(message.getSender()).isNull();
    }
}
