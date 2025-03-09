package bham.team.domain;

import static bham.team.domain.ItemTestSamples.*;
import static bham.team.domain.LikesTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LikesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Likes.class);
        Likes likes1 = getLikesSample1();
        Likes likes2 = new Likes();
        assertThat(likes1).isNotEqualTo(likes2);

        likes2.setId(likes1.getId());
        assertThat(likes1).isEqualTo(likes2);

        likes2 = getLikesSample2();
        assertThat(likes1).isNotEqualTo(likes2);
    }

    @Test
    void itemTest() {
        Likes likes = getLikesRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        likes.setItem(itemBack);
        assertThat(likes.getItem()).isEqualTo(itemBack);

        likes.item(null);
        assertThat(likes.getItem()).isNull();
    }

    @Test
    void userTest() {
        Likes likes = getLikesRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        likes.setUser(userDetailsBack);
        assertThat(likes.getUser()).isEqualTo(userDetailsBack);

        likes.user(null);
        assertThat(likes.getUser()).isNull();
    }
}
