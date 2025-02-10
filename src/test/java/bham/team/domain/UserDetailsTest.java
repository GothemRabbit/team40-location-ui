package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDetails.class);
        UserDetails userDetails1 = getUserDetailsSample1();
        UserDetails userDetails2 = new UserDetails();
        assertThat(userDetails1).isNotEqualTo(userDetails2);

        userDetails2.setId(userDetails1.getId());
        assertThat(userDetails1).isEqualTo(userDetails2);

        userDetails2 = getUserDetailsSample2();
        assertThat(userDetails1).isNotEqualTo(userDetails2);
    }

    @Test
    void messageTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Message messageBack = getMessageRandomSampleGenerator();

        userDetails.addMessage(messageBack);
        assertThat(userDetails.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getUserDetails()).isEqualTo(userDetails);

        userDetails.removeMessage(messageBack);
        assertThat(userDetails.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getUserDetails()).isNull();

        userDetails.messages(new HashSet<>(Set.of(messageBack)));
        assertThat(userDetails.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getUserDetails()).isEqualTo(userDetails);

        userDetails.setMessages(new HashSet<>());
        assertThat(userDetails.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getUserDetails()).isNull();
    }

    @Test
    void conversationTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        userDetails.addConversation(conversationBack);
        assertThat(userDetails.getConversations()).containsOnly(conversationBack);
        assertThat(conversationBack.getUserDetails()).containsOnly(userDetails);

        userDetails.removeConversation(conversationBack);
        assertThat(userDetails.getConversations()).doesNotContain(conversationBack);
        assertThat(conversationBack.getUserDetails()).doesNotContain(userDetails);

        userDetails.conversations(new HashSet<>(Set.of(conversationBack)));
        assertThat(userDetails.getConversations()).containsOnly(conversationBack);
        assertThat(conversationBack.getUserDetails()).containsOnly(userDetails);

        userDetails.setConversations(new HashSet<>());
        assertThat(userDetails.getConversations()).doesNotContain(conversationBack);
        assertThat(conversationBack.getUserDetails()).doesNotContain(userDetails);
    }
}
