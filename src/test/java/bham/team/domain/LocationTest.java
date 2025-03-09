package bham.team.domain;

import static bham.team.domain.LocationTestSamples.*;
import static bham.team.domain.ProductStatusTestSamples.*;
import static bham.team.domain.ProfileDetailsTestSamples.*;
import static bham.team.domain.UserDetailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bham.team.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void productStatusTest() {
        Location location = getLocationRandomSampleGenerator();
        ProductStatus productStatusBack = getProductStatusRandomSampleGenerator();

        location.addProductStatus(productStatusBack);
        assertThat(location.getProductStatuses()).containsOnly(productStatusBack);
        assertThat(productStatusBack.getLocation()).isEqualTo(location);

        location.removeProductStatus(productStatusBack);
        assertThat(location.getProductStatuses()).doesNotContain(productStatusBack);
        assertThat(productStatusBack.getLocation()).isNull();

        location.productStatuses(new HashSet<>(Set.of(productStatusBack)));
        assertThat(location.getProductStatuses()).containsOnly(productStatusBack);
        assertThat(productStatusBack.getLocation()).isEqualTo(location);

        location.setProductStatuses(new HashSet<>());
        assertThat(location.getProductStatuses()).doesNotContain(productStatusBack);
        assertThat(productStatusBack.getLocation()).isNull();
    }

    @Test
    void profileDetailsTest() {
        Location location = getLocationRandomSampleGenerator();
        ProfileDetails profileDetailsBack = getProfileDetailsRandomSampleGenerator();

        location.addProfileDetails(profileDetailsBack);
        assertThat(location.getProfileDetails()).containsOnly(profileDetailsBack);
        assertThat(profileDetailsBack.getLocations()).containsOnly(location);

        location.removeProfileDetails(profileDetailsBack);
        assertThat(location.getProfileDetails()).doesNotContain(profileDetailsBack);
        assertThat(profileDetailsBack.getLocations()).doesNotContain(location);

        location.profileDetails(new HashSet<>(Set.of(profileDetailsBack)));
        assertThat(location.getProfileDetails()).containsOnly(profileDetailsBack);
        assertThat(profileDetailsBack.getLocations()).containsOnly(location);

        location.setProfileDetails(new HashSet<>());
        assertThat(location.getProfileDetails()).doesNotContain(profileDetailsBack);
        assertThat(profileDetailsBack.getLocations()).doesNotContain(location);
    }

    @Test
    void usersTest() {
        Location location = getLocationRandomSampleGenerator();
        UserDetails userDetailsBack = getUserDetailsRandomSampleGenerator();

        location.addUsers(userDetailsBack);
        assertThat(location.getUsers()).containsOnly(userDetailsBack);
        assertThat(userDetailsBack.getMeetupLocations()).containsOnly(location);

        location.removeUsers(userDetailsBack);
        assertThat(location.getUsers()).doesNotContain(userDetailsBack);
        assertThat(userDetailsBack.getMeetupLocations()).doesNotContain(location);

        location.users(new HashSet<>(Set.of(userDetailsBack)));
        assertThat(location.getUsers()).containsOnly(userDetailsBack);
        assertThat(userDetailsBack.getMeetupLocations()).containsOnly(location);

        location.setUsers(new HashSet<>());
        assertThat(location.getUsers()).doesNotContain(userDetailsBack);
        assertThat(userDetailsBack.getMeetupLocations()).doesNotContain(location);
    }
}
