package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProfileDetails getProfileDetailsSample1() {
        return new ProfileDetails().id(1L).userName("userName1");
    }

    public static ProfileDetails getProfileDetailsSample2() {
        return new ProfileDetails().id(2L).userName("userName2");
    }

    public static ProfileDetails getProfileDetailsRandomSampleGenerator() {
        return new ProfileDetails().id(longCount.incrementAndGet()).userName(UUID.randomUUID().toString());
    }
}
