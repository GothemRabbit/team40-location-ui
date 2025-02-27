package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserRecommendationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserRecommendation getUserRecommendationSample1() {
        return new UserRecommendation().id(1L).reason("reason1");
    }

    public static UserRecommendation getUserRecommendationSample2() {
        return new UserRecommendation().id(2L).reason("reason2");
    }

    public static UserRecommendation getUserRecommendationRandomSampleGenerator() {
        return new UserRecommendation().id(longCount.incrementAndGet()).reason(UUID.randomUUID().toString());
    }
}
