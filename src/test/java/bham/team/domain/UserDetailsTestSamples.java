package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserDetails getUserDetailsSample1() {
        return new UserDetails().id(1L).firstName("firstName1").lastName("lastName1").email("email1");
    }

    public static UserDetails getUserDetailsSample2() {
        return new UserDetails().id(2L).firstName("firstName2").lastName("lastName2").email("email2");
    }

    public static UserDetails getUserDetailsRandomSampleGenerator() {
        return new UserDetails()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
