package bham.team.domain;

import static bham.team.domain.AuthenticationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthenticationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Authentication.class);
        Authentication authentication1 = getAuthenticationSample1();
        Authentication authentication2 = new Authentication();
        assertThat(authentication1).isNotEqualTo(authentication2);

        authentication2.setId(authentication1.getId());
        assertThat(authentication1).isEqualTo(authentication2);

        authentication2 = getAuthenticationSample2();
        assertThat(authentication1).isNotEqualTo(authentication2);
    }
}
