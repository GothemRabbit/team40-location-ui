package bham.team.domain;

import static bham.team.domain.ImagesTestSamples.*;
import static bham.team.domain.ItemTestSamples.*;
import static bham.team.domain.LikesTestSamples.*;
import static bham.team.domain.ProductStatusTestSamples.*;
import static bham.team.domain.ProfileDetailsTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static bham.team.domain.WishlistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = getItemSample1();
        Item item2 = new Item();
        assertThat(item1).isNotEqualTo(item2);

        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);

        item2 = getItemSample2();
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void imagesTest() {
        Item item = getItemRandomSampleGenerator();
        Images imagesBack = getImagesRandomSampleGenerator();

        item.addImages(imagesBack);
        assertThat(item.getImages()).containsOnly(imagesBack);
        assertThat(imagesBack.getItem()).isEqualTo(item);

        item.removeImages(imagesBack);
        assertThat(item.getImages()).doesNotContain(imagesBack);
        assertThat(imagesBack.getItem()).isNull();

        item.images(new HashSet<>(Set.of(imagesBack)));
        assertThat(item.getImages()).containsOnly(imagesBack);
        assertThat(imagesBack.getItem()).isEqualTo(item);

        item.setImages(new HashSet<>());
        assertThat(item.getImages()).doesNotContain(imagesBack);
        assertThat(imagesBack.getItem()).isNull();
    }

    @Test
    void wishlistTest() {
        Item item = getItemRandomSampleGenerator();
        Wishlist wishlistBack = getWishlistRandomSampleGenerator();

        item.addWishlist(wishlistBack);
        assertThat(item.getWishlists()).containsOnly(wishlistBack);

        item.removeWishlist(wishlistBack);
        assertThat(item.getWishlists()).doesNotContain(wishlistBack);

        item.wishlists(new HashSet<>(Set.of(wishlistBack)));
        assertThat(item.getWishlists()).containsOnly(wishlistBack);

        item.setWishlists(new HashSet<>());
        assertThat(item.getWishlists()).doesNotContain(wishlistBack);
    }

    @Test
    void productStatusTest() {
        Item item = getItemRandomSampleGenerator();
        ProductStatus productStatusBack = getProductStatusRandomSampleGenerator();

        item.setProductStatus(productStatusBack);
        assertThat(item.getProductStatus()).isEqualTo(productStatusBack);
        assertThat(productStatusBack.getItem()).isEqualTo(item);

        item.productStatus(null);
        assertThat(item.getProductStatus()).isNull();
        assertThat(productStatusBack.getItem()).isNull();
    }

    @Test
    void profileDetailsTest() {
        Item item = getItemRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        item.setProfileDetails(profileDetailsBack);
        assertThat(item.getProfileDetails()).isEqualTo(profileDetailsBack);

        item.profileDetails(null);
        assertThat(item.getProfileDetails()).isNull();
    }

    @Test
    void likesTest() {
        Item item = getItemRandomSampleGenerator();
        Likes likesBack = getLikesRandomSampleGenerator();

        item.addLikes(likesBack);
        assertThat(item.getLikes()).containsOnly(likesBack);
        assertThat(likesBack.getItem()).isEqualTo(item);

        item.removeLikes(likesBack);
        assertThat(item.getLikes()).doesNotContain(likesBack);
        assertThat(likesBack.getItem()).isNull();

        item.likes(new HashSet<>(Set.of(likesBack)));
        assertThat(item.getLikes()).containsOnly(likesBack);
        assertThat(likesBack.getItem()).isEqualTo(item);

        item.setLikes(new HashSet<>());
        assertThat(item.getLikes()).doesNotContain(likesBack);
        assertThat(likesBack.getItem()).isNull();
    }

    @Test
    void sellerTest() {
        Item item = getItemRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        item.setSeller(userDetailsBack);
        assertThat(item.getSeller()).isEqualTo(userDetailsBack);

        item.seller(null);
        assertThat(item.getSeller()).isNull();
    }
}
