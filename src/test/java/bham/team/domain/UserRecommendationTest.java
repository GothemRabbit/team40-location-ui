package bham.team.domain;

import static bham.team.domain.UserRecommendationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserRecommendationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRecommendation.class);
        UserRecommendation userRecommendation1 = getUserRecommendationSample1();
        UserRecommendation userRecommendation2 = new UserRecommendation();
        assertThat(userRecommendation1).isNotEqualTo(userRecommendation2);

        userRecommendation2.setId(userRecommendation1.getId());
        assertThat(userRecommendation1).isEqualTo(userRecommendation2);

        userRecommendation2 = getUserRecommendationSample2();
        assertThat(userRecommendation1).isNotEqualTo(userRecommendation2);
    }
}
