package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.ItemTestSamples.*;
import static bham.team.domain.LocationTestSamples.*;
import static bham.team.domain.ReviewTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static bham.team.domain.WishlistTestSamples.*;
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
    void itemsOnSaleTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        userDetails.addItemsOnSale(itemBack);
        assertThat(userDetails.getItemsOnSales()).containsOnly(itemBack);
        assertThat(itemBack.getSeller()).isEqualTo(userDetails);

        userDetails.removeItemsOnSale(itemBack);
        assertThat(userDetails.getItemsOnSales()).doesNotContain(itemBack);
        assertThat(itemBack.getSeller()).isNull();

        userDetails.itemsOnSales(new HashSet<>(Set.of(itemBack)));
        assertThat(userDetails.getItemsOnSales()).containsOnly(itemBack);
        assertThat(itemBack.getSeller()).isEqualTo(userDetails);

        userDetails.setItemsOnSales(new HashSet<>());
        assertThat(userDetails.getItemsOnSales()).doesNotContain(itemBack);
        assertThat(itemBack.getSeller()).isNull();
    }

    @Test
    void wishlistTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Wishlist wishlistBack = getWishlistRandomSampleGenerator();

        userDetails.addWishlist(wishlistBack);
        assertThat(userDetails.getWishlists()).containsOnly(wishlistBack);
        assertThat(wishlistBack.getUserDetails()).isEqualTo(userDetails);

        userDetails.removeWishlist(wishlistBack);
        assertThat(userDetails.getWishlists()).doesNotContain(wishlistBack);
        assertThat(wishlistBack.getUserDetails()).isNull();

        userDetails.wishlists(new HashSet<>(Set.of(wishlistBack)));
        assertThat(userDetails.getWishlists()).containsOnly(wishlistBack);
        assertThat(wishlistBack.getUserDetails()).isEqualTo(userDetails);

        userDetails.setWishlists(new HashSet<>());
        assertThat(userDetails.getWishlists()).doesNotContain(wishlistBack);
        assertThat(wishlistBack.getUserDetails()).isNull();
    }

    @Test
    void meetupLocationsTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        userDetails.addMeetupLocations(locationBack);
        assertThat(userDetails.getMeetupLocations()).containsOnly(locationBack);

        userDetails.removeMeetupLocations(locationBack);
        assertThat(userDetails.getMeetupLocations()).doesNotContain(locationBack);

        userDetails.meetupLocations(new HashSet<>(Set.of(locationBack)));
        assertThat(userDetails.getMeetupLocations()).containsOnly(locationBack);

        userDetails.setMeetupLocations(new HashSet<>());
        assertThat(userDetails.getMeetupLocations()).doesNotContain(locationBack);
    }

    @Test
    void buyersReviewTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        userDetails.addBuyersReview(reviewBack);
        assertThat(userDetails.getBuyersReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getBuyer()).isEqualTo(userDetails);

        userDetails.removeBuyersReview(reviewBack);
        assertThat(userDetails.getBuyersReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getBuyer()).isNull();

        userDetails.buyersReviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(userDetails.getBuyersReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getBuyer()).isEqualTo(userDetails);

        userDetails.setBuyersReviews(new HashSet<>());
        assertThat(userDetails.getBuyersReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getBuyer()).isNull();
    }

    @Test
    void reviewsOfSellerTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        userDetails.addReviewsOfSeller(reviewBack);
        assertThat(userDetails.getReviewsOfSellers()).containsOnly(reviewBack);
        assertThat(reviewBack.getSeller()).isEqualTo(userDetails);

        userDetails.removeReviewsOfSeller(reviewBack);
        assertThat(userDetails.getReviewsOfSellers()).doesNotContain(reviewBack);
        assertThat(reviewBack.getSeller()).isNull();

        userDetails.reviewsOfSellers(new HashSet<>(Set.of(reviewBack)));
        assertThat(userDetails.getReviewsOfSellers()).containsOnly(reviewBack);
        assertThat(reviewBack.getSeller()).isEqualTo(userDetails);

        userDetails.setReviewsOfSellers(new HashSet<>());
        assertThat(userDetails.getReviewsOfSellers()).doesNotContain(reviewBack);
        assertThat(reviewBack.getSeller()).isNull();
    }

    @Test
    void chatsTest() {
        UserDetails userDetails = getUserDetailsRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        userDetails.addChats(conversationBack);
        assertThat(userDetails.getChats()).containsOnly(conversationBack);
        assertThat(conversationBack.getParticipants()).containsOnly(userDetails);

        userDetails.removeChats(conversationBack);
        assertThat(userDetails.getChats()).doesNotContain(conversationBack);
        assertThat(conversationBack.getParticipants()).doesNotContain(userDetails);

        userDetails.chats(new HashSet<>(Set.of(conversationBack)));
        assertThat(userDetails.getChats()).containsOnly(conversationBack);
        assertThat(conversationBack.getParticipants()).containsOnly(userDetails);

        userDetails.setChats(new HashSet<>());
        assertThat(userDetails.getChats()).doesNotContain(conversationBack);
        assertThat(conversationBack.getParticipants()).doesNotContain(userDetails);
    }
}
