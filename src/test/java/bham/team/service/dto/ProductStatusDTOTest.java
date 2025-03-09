package bham.team.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductStatusDTO.class);
        ProductStatusDTO productStatusDTO1 = new ProductStatusDTO();
        productStatusDTO1.setId(1L);
        ProductStatusDTO productStatusDTO2 = new ProductStatusDTO();
        assertThat(productStatusDTO1).isNotEqualTo(productStatusDTO2);
        productStatusDTO2.setId(productStatusDTO1.getId());
        assertThat(productStatusDTO1).isEqualTo(productStatusDTO2);
        productStatusDTO2.setId(2L);
        assertThat(productStatusDTO1).isNotEqualTo(productStatusDTO2);
        productStatusDTO1.setId(null);
        assertThat(productStatusDTO1).isNotEqualTo(productStatusDTO2);
    }
}
