package bham.team.domain;

import static bham.team.domain.ConversationTestSamples.*;
import static bham.team.domain.ItemTestSamples.*;
import static bham.team.domain.LocationTestSamples.*;
import static bham.team.domain.ProductStatusTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductStatus.class);
        ProductStatus productStatus1 = getProductStatusSample1();
        ProductStatus productStatus2 = new ProductStatus();
        assertThat(productStatus1).isNotEqualTo(productStatus2);

        productStatus2.setId(productStatus1.getId());
        assertThat(productStatus1).isEqualTo(productStatus2);

        productStatus2 = getProductStatusSample2();
        assertThat(productStatus1).isNotEqualTo(productStatus2);
    }

    @Test
    void itemTest() {
        ProductStatus productStatus = getProductStatusRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        productStatus.setItem(itemBack);
        assertThat(productStatus.getItem()).isEqualTo(itemBack);

        productStatus.item(null);
        assertThat(productStatus.getItem()).isNull();
    }

    @Test
    void conversationTest() {
        ProductStatus productStatus = getProductStatusRandomSampleGenerator();
        Conversation conversationBack = getConversationRandomSampleGenerator();

        productStatus.setConversation(conversationBack);
        assertThat(productStatus.getConversation()).isEqualTo(conversationBack);

        productStatus.conversation(null);
        assertThat(productStatus.getConversation()).isNull();
    }

    @Test
    void buyerTest() {
        ProductStatus productStatus = getProductStatusRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        productStatus.setBuyer(userDetailsBack);
        assertThat(productStatus.getBuyer()).isEqualTo(userDetailsBack);

        productStatus.buyer(null);
        assertThat(productStatus.getBuyer()).isNull();
    }

    @Test
    void sellerTest() {
        ProductStatus productStatus = getProductStatusRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        productStatus.setSeller(userDetailsBack);
        assertThat(productStatus.getSeller()).isEqualTo(userDetailsBack);

        productStatus.seller(null);
        assertThat(productStatus.getSeller()).isNull();
    }

    @Test
    void meetingLocationTest() {
        ProductStatus productStatus = getProductStatusRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        productStatus.setMeetingLocation(locationBack);
        assertThat(productStatus.getMeetingLocation()).isEqualTo(locationBack);

        productStatus.meetingLocation(null);
        assertThat(productStatus.getMeetingLocation()).isNull();
    }
}
