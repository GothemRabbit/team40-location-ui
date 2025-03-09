package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.ItemTestSamples.*;
import static bham.team.domain.LikesTestSamples.*;
import static bham.team.domain.LocationTestSamples.*;
import static bham.team.domain.MessageTestSamples.*;
import static bham.team.domain.ProductStatusTestSamples.*;
import static bham.team.domain.ProfileDetailsTestSamples.*;
import static bham.team.domain.ReviewTestSamples.*;
import static bham.team.domain.WishlistTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfileDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDetails.class);
        ProfileDetails profileDetails1 = getProfileDetailsSample1();
        ProfileDetails profileDetails2 = new ProfileDetails();
        assertThat(profileDetails1).isNotEqualTo(profileDetails2);

        profileDetails2.setId(profileDetails1.getId());
        assertThat(profileDetails1).isEqualTo(profileDetails2);

        profileDetails2 = getProfileDetailsSample2();
        assertThat(profileDetails1).isNotEqualTo(profileDetails2);
    }

    @Test
    void itemTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        profileDetails.addItem(itemBack);
        assertThat(profileDetails.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.removeItem(itemBack);
        assertThat(profileDetails.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getProfileDetails()).isNull();

        profileDetails.items(new HashSet<>(Set.of(itemBack)));
        assertThat(profileDetails.getItems()).containsOnly(itemBack);
        assertThat(itemBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.setItems(new HashSet<>());
        assertThat(profileDetails.getItems()).doesNotContain(itemBack);
        assertThat(itemBack.getProfileDetails()).isNull();
    }

    @Test
    void wishlistTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Wishlist wishlistBack = getWishlistRandomSampleGenerator();

        profileDetails.addWishlist(wishlistBack);
        assertThat(profileDetails.getWishlists()).containsOnly(wishlistBack);
        assertThat(wishlistBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.removeWishlist(wishlistBack);
        assertThat(profileDetails.getWishlists()).doesNotContain(wishlistBack);
        assertThat(wishlistBack.getProfileDetails()).isNull();

        profileDetails.wishlists(new HashSet<>(Set.of(wishlistBack)));
        assertThat(profileDetails.getWishlists()).containsOnly(wishlistBack);
        assertThat(wishlistBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.setWishlists(new HashSet<>());
        assertThat(profileDetails.getWishlists()).doesNotContain(wishlistBack);
        assertThat(wishlistBack.getProfileDetails()).isNull();
    }

    @Test
    void locationTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        profileDetails.addLocation(locationBack);
        assertThat(profileDetails.getLocations()).containsOnly(locationBack);

        profileDetails.removeLocation(locationBack);
        assertThat(profileDetails.getLocations()).doesNotContain(locationBack);

        profileDetails.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(profileDetails.getLocations()).containsOnly(locationBack);

        profileDetails.setLocations(new HashSet<>());
        assertThat(profileDetails.getLocations()).doesNotContain(locationBack);
    }

    @Test
    void likesTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Likes likesBack = getLikesRandomSampleGenerator();

        profileDetails.addLikes(likesBack);
        assertThat(profileDetails.getLikes()).containsOnly(likesBack);
        assertThat(likesBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.removeLikes(likesBack);
        assertThat(profileDetails.getLikes()).doesNotContain(likesBack);
        assertThat(likesBack.getProfileDetails()).isNull();

        profileDetails.likes(new HashSet<>(Set.of(likesBack)));
        assertThat(profileDetails.getLikes()).containsOnly(likesBack);
        assertThat(likesBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.setLikes(new HashSet<>());
        assertThat(profileDetails.getLikes()).doesNotContain(likesBack);
        assertThat(likesBack.getProfileDetails()).isNull();
    }

    @Test
    void reviewTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Review reviewBack = getReviewRandomSampleGenerator();

        profileDetails.addReview(reviewBack);
        assertThat(profileDetails.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.removeReview(reviewBack);
        assertThat(profileDetails.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getProfileDetails()).isNull();

        profileDetails.reviews(new HashSet<>(Set.of(reviewBack)));
        assertThat(profileDetails.getReviews()).containsOnly(reviewBack);
        assertThat(reviewBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.setReviews(new HashSet<>());
        assertThat(profileDetails.getReviews()).doesNotContain(reviewBack);
        assertThat(reviewBack.getProfileDetails()).isNull();
    }

    @Test
    void messageTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Message messageBack = getMessageRandomSampleGenerator();

        profileDetails.addMessage(messageBack);
        assertThat(profileDetails.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.removeMessage(messageBack);
        assertThat(profileDetails.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getProfileDetails()).isNull();

        profileDetails.messages(new HashSet<>(Set.of(messageBack)));
        assertThat(profileDetails.getMessages()).containsOnly(messageBack);
        assertThat(messageBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.setMessages(new HashSet<>());
        assertThat(profileDetails.getMessages()).doesNotContain(messageBack);
        assertThat(messageBack.getProfileDetails()).isNull();
    }

    @Test
    void productStatusTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        ProductStatus productStatusBack = getProductStatusRandomSampleGenerator();

        profileDetails.addProductStatus(productStatusBack);
        assertThat(profileDetails.getProductStatuses()).containsOnly(productStatusBack);
        assertThat(productStatusBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.removeProductStatus(productStatusBack);
        assertThat(profileDetails.getProductStatuses()).doesNotContain(productStatusBack);
        assertThat(productStatusBack.getProfileDetails()).isNull();

        profileDetails.productStatuses(new HashSet<>(Set.of(productStatusBack)));
        assertThat(profileDetails.getProductStatuses()).containsOnly(productStatusBack);
        assertThat(productStatusBack.getProfileDetails()).isEqualTo(profileDetails);

        profileDetails.setProductStatuses(new HashSet<>());
        assertThat(profileDetails.getProductStatuses()).doesNotContain(productStatusBack);
        assertThat(productStatusBack.getProfileDetails()).isNull();
    }

    @Test
    void conversationTest() {
        ProfileDetails profileDetails = getProfileDetailsRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        profileDetails.addConversation(conversationBack);
        assertThat(profileDetails.getConversations()).containsOnly(conversationBack);
        assertThat(conversationBack.getProfileDetails()).containsOnly(profileDetails);

        profileDetails.removeConversation(conversationBack);
        assertThat(profileDetails.getConversations()).doesNotContain(conversationBack);
        assertThat(conversationBack.getProfileDetails()).doesNotContain(profileDetails);

        profileDetails.conversations(new HashSet<>(Set.of(conversationBack)));
        assertThat(profileDetails.getConversations()).containsOnly(conversationBack);
        assertThat(conversationBack.getProfileDetails()).containsOnly(profileDetails);

        profileDetails.setConversations(new HashSet<>());
        assertThat(profileDetails.getConversations()).doesNotContain(conversationBack);
        assertThat(conversationBack.getProfileDetails()).doesNotContain(profileDetails);
    }
}
