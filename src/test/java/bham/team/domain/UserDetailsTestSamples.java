package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserDetails getUserDetailsSample1() {
        return new UserDetails()
            .id(1L)
            .userName("userName1")
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .preferences("preferences1")
            .address("address1");
    }

    public static UserDetails getUserDetailsSample2() {
        return new UserDetails()
            .id(2L)
            .userName("userName2")
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .preferences("preferences2")
            .address("address2");
    }

    public static UserDetails getUserDetailsRandomSampleGenerator() {
        return new UserDetails()
            .id(longCount.incrementAndGet())
            .userName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .preferences(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
