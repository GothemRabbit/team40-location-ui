package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserInteractionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserInteraction getUserInteractionSample1() {
        return new UserInteraction().id(1L).details("details1");
    }

    public static UserInteraction getUserInteractionSample2() {
        return new UserInteraction().id(2L).details("details2");
    }

    public static UserInteraction getUserInteractionRandomSampleGenerator() {
        return new UserInteraction().id(longCount.incrementAndGet()).details(UUID.randomUUID().toString());
    }
}
