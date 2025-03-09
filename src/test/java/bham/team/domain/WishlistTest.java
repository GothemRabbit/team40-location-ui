package bham.team.domain;

import static bham.team.domain.ItemTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static bham.team.domain.WishlistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WishlistTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wishlist.class);
        Wishlist wishlist1 = getWishlistSample1();
        Wishlist wishlist2 = new Wishlist();
        assertThat(wishlist1).isNotEqualTo(wishlist2);

        wishlist2.setId(wishlist1.getId());
        assertThat(wishlist1).isEqualTo(wishlist2);

        wishlist2 = getWishlistSample2();
        assertThat(wishlist1).isNotEqualTo(wishlist2);
    }

    @Test
    void userDetailsTest() {
        Wishlist wishlist = getWishlistRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        wishlist.setUserDetails(userDetailsBack);
        assertThat(wishlist.getUserDetails()).isEqualTo(userDetailsBack);

        wishlist.userDetails(null);
        assertThat(wishlist.getUserDetails()).isNull();
    }

    @Test
    void itemsTest() {
        Wishlist wishlist = getWishlistRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        wishlist.addItems(itemBack);
        assertThat(wishlist.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getWishlists()).containsOnly(wishlist);

        wishlist.removeItems(itemBack);
        assertThat(wishlist.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getWishlists()).doesNotContain(wishlist);

        wishlist.items(new HashSet<>(Set.of(itemBack)));
        assertThat(wishlist.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getWishlists()).containsOnly(wishlist);

        wishlist.setItems(new HashSet<>());
        assertThat(wishlist.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getWishlists()).doesNotContain(wishlist);
    }
}
