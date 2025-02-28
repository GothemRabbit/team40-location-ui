package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
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
    void userOneTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        conversation.setUserOne(userDetailsBack);
        assertThat(conversation.getUserOne()).isEqualTo(userDetailsBack);

        conversation.userOne(null);
        assertThat(conversation.getUserOne()).isNull();
    }

    @Test
    void userTwoTest() {
        Conversation conversation = getConversationRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        conversation.setUserTwo(userDetailsBack);
        assertThat(conversation.getUserTwo()).isEqualTo(userDetailsBack);

        conversation.userTwo(null);
        assertThat(conversation.getUserTwo()).isNull();
    }
}
