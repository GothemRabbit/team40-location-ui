package bham.team.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProductStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProductStatus getProductStatusSample1() {
        return new ProductStatus().id(1L);
    }

    public static ProductStatus getProductStatusSample2() {
        return new ProductStatus().id(2L);
    }

    public static ProductStatus getProductStatusRandomSampleGenerator() {
        return new ProductStatus().id(longCount.incrementAndGet());
    }
}
