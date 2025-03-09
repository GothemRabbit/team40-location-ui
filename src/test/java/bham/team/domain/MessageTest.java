package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
import static bham.team.domain.ProfileDetailsTestSamples.*;
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
    void conversationTest() {
        Message message = getMessageRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        message.setConversation(conversationBack);
        assertThat(message.getConversation()).isEqualTo(conversationBack);

        message.conversation(null);
        assertThat(message.getConversation()).isNull();
    }

    @Test
    void profileDetailsTest() {
        Message message = getMessageRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        message.setProfileDetails(profileDetailsBack);
        assertThat(message.getProfileDetails()).isEqualTo(profileDetailsBack);

        message.profileDetails(null);
        assertThat(message.getProfileDetails()).isNull();
    }
}
