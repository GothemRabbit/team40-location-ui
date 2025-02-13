package bham.team.domain;

import static bham.team.domain.ProductStatusTestSamples.*;
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
}
