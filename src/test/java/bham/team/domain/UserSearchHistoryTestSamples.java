package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserSearchHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserSearchHistory getUserSearchHistorySample1() {
        return new UserSearchHistory().id(1L).searchQuery("searchQuery1").filters("filters1");
    }

    public static UserSearchHistory getUserSearchHistorySample2() {
        return new UserSearchHistory().id(2L).searchQuery("searchQuery2").filters("filters2");
    }

    public static UserSearchHistory getUserSearchHistoryRandomSampleGenerator() {
        return new UserSearchHistory()
            .id(longCount.incrementAndGet())
            .searchQuery(UUID.randomUUID().toString())
            .filters(UUID.randomUUID().toString());
    }
}
