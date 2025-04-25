package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
import static bham.team.domain.ProductStatusTestSamples.*;
import static bham.team.domain.ProfileDetailsTestSamples.*;
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
    void profileDetailsTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        conversation.addProfileDetails(profileDetailsBack);
        assertThat(conversation.getProfileDetails()).containsOnly(profileDetailsBack);

        conversation.removeProfileDetails(profileDetailsBack);
        assertThat(conversation.getProfileDetails()).doesNotContain(profileDetailsBack);

        conversation.profileDetails(new HashSet<>(Set.of(profileDetailsBack)));
        assertThat(conversation.getProfileDetails()).containsOnly(profileDetailsBack);

        conversation.setProfileDetails(new HashSet<>());
        assertThat(conversation.getProfileDetails()).doesNotContain(profileDetailsBack);
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

    @Test
    void participantsTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        conversation.addParticipants(profileDetailsBack);
        assertThat(conversation.getParticipants()).containsOnly(profileDetailsBack);

        conversation.removeParticipants(profileDetailsBack);
        assertThat(conversation.getParticipants()).doesNotContain(profileDetailsBack);

        conversation.participants(new HashSet<>(Set.of(profileDetailsBack)));
        assertThat(conversation.getParticipants()).containsOnly(profileDetailsBack);

        conversation.setParticipants(new HashSet<>());
        assertThat(conversation.getParticipants()).doesNotContain(profileDetailsBack);
    }
}
