package bham.team.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthenticationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Authentication getAuthenticationSample1() {
        return new Authentication().id(1L).password("password1");
    }

    public static Authentication getAuthenticationSample2() {
        return new Authentication().id(2L).password("password2");
    }

    public static Authentication getAuthenticationRandomSampleGenerator() {
        return new Authentication().id(longCount.incrementAndGet()).password(UUID.randomUUID().toString());
    }
}
