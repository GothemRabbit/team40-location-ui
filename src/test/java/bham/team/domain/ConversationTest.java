package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
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
    void userDetailsTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        conversation.addUserDetails(userDetailsBack);
        assertThat(conversation.getUserDetails()).containsOnly(userDetailsBack);

        conversation.removeUserDetails(userDetailsBack);
        assertThat(conversation.getUserDetails()).doesNotContain(userDetailsBack);

        conversation.userDetails(new HashSet<>(Set.of(userDetailsBack)));
        assertThat(conversation.getUserDetails()).containsOnly(userDetailsBack);

        conversation.setUserDetails(new HashSet<>());
        assertThat(conversation.getUserDetails()).doesNotContain(userDetailsBack);
    }

    @Test
    void messageTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        Message messageBack = getMessageRandomSampleGenerator();

        conversation.addMessage(messageBack);
        assertThat(conversation.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getConversation()).isEqualTo(conversation);

        conversation.removeMessage(messageBack);
        assertThat(conversation.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getConversation()).isNull();

        conversation.messages(new HashSet<>(Set.of(messageBack)));
        assertThat(conversation.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getConversation()).isEqualTo(conversation);

        conversation.setMessages(new HashSet<>());
        assertThat(conversation.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getConversation()).isNull();
    }
}
