package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductStatus getProductStatusSample1() {
        return new ProductStatus().id(1L).meetingLocation("meetingLocation1").chatLink("chatLink1");
    }

    public static ProductStatus getProductStatusSample2() {
        return new ProductStatus().id(2L).meetingLocation("meetingLocation2").chatLink("chatLink2");
    }

    public static ProductStatus getProductStatusRandomSampleGenerator() {
        return new ProductStatus()
            .id(longCount.incrementAndGet())
            .meetingLocation(UUID.randomUUID().toString())
            .chatLink(UUID.randomUUID().toString());
    }
}
