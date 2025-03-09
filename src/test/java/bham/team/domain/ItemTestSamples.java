package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Item getItemSample1() {
        return new Item().id(1L).title("title1").brand("brand1").colour("colour1");
    }

    public static Item getItemSample2() {
        return new Item().id(2L).title("title2").brand("brand2").colour("colour2");
    }

    public static Item getItemRandomSampleGenerator() {
        return new Item()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .brand(UUID.randomUUID().toString())
            .colour(UUID.randomUUID().toString());
    }
}
