package bham.team.domain;

import static bham.team.domain.ProfileDetailsTestSamples.*;
import static bham.team.domain.ReviewTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void consumerTest() {
        Review review = getReviewRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        review.setConsumer(profileDetailsBack);
        assertThat(review.getConsumer()).isEqualTo(profileDetailsBack);

        review.consumer(null);
        assertThat(review.getConsumer()).isNull();
    }

    @Test
    void retailerTest() {
        Review review = getReviewRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        review.setRetailer(profileDetailsBack);
        assertThat(review.getRetailer()).isEqualTo(profileDetailsBack);

        review.retailer(null);
        assertThat(review.getRetailer()).isNull();
    }

    @Test
    void buyerTest() {
        Review review = getReviewRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        review.setBuyer(userDetailsBack);
        assertThat(review.getBuyer()).isEqualTo(userDetailsBack);

        review.buyer(null);
        assertThat(review.getBuyer()).isNull();
    }

    @Test
    void sellerTest() {
        Review review = getReviewRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        review.setSeller(userDetailsBack);
        assertThat(review.getSeller()).isEqualTo(userDetailsBack);

        review.seller(null);
        assertThat(review.getSeller()).isNull();
    }
}
