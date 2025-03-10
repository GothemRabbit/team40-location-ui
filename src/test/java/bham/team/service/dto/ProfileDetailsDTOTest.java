package bham.team.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDetailsDTO.class);
        ProfileDetailsDTO profileDetailsDTO1 = new ProfileDetailsDTO();
        profileDetailsDTO1.setId(1L);
        ProfileDetailsDTO profileDetailsDTO2 = new ProfileDetailsDTO();
        assertThat(profileDetailsDTO1).isNotEqualTo(profileDetailsDTO2);
        profileDetailsDTO2.setId(profileDetailsDTO1.getId());
        assertThat(profileDetailsDTO1).isEqualTo(profileDetailsDTO2);
        profileDetailsDTO2.setId(2L);
        assertThat(profileDetailsDTO1).isNotEqualTo(profileDetailsDTO2);
        profileDetailsDTO1.setId(null);
        assertThat(profileDetailsDTO1).isNotEqualTo(profileDetailsDTO2);
    }
}
