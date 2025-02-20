package bham.team.domain;

import static bham.team.domain.UserInteractionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserInteractionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInteraction.class);
        UserInteraction userInteraction1 = getUserInteractionSample1();
        UserInteraction userInteraction2 = new UserInteraction();
        assertThat(userInteraction1).isNotEqualTo(userInteraction2);

        userInteraction2.setId(userInteraction1.getId());
        assertThat(userInteraction1).isEqualTo(userInteraction2);

        userInteraction2 = getUserInteractionSample2();
        assertThat(userInteraction1).isNotEqualTo(userInteraction2);
    }
}
