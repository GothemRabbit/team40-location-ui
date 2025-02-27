package bham.team.domain;

import static bham.team.domain.UserSearchHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSearchHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSearchHistory.class);
        UserSearchHistory userSearchHistory1 = getUserSearchHistorySample1();
        UserSearchHistory userSearchHistory2 = new UserSearchHistory();
        assertThat(userSearchHistory1).isNotEqualTo(userSearchHistory2);

        userSearchHistory2.setId(userSearchHistory1.getId());
        assertThat(userSearchHistory1).isEqualTo(userSearchHistory2);

        userSearchHistory2 = getUserSearchHistorySample2();
        assertThat(userSearchHistory1).isNotEqualTo(userSearchHistory2);
    }
}
