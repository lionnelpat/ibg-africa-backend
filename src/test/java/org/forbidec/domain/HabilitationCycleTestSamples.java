package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HabilitationCycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HabilitationCycle getHabilitationCycleSample1() {
        return new HabilitationCycle().id(1L).keycloakUserId("keycloakUserId1");
    }

    public static HabilitationCycle getHabilitationCycleSample2() {
        return new HabilitationCycle().id(2L).keycloakUserId("keycloakUserId2");
    }

    public static HabilitationCycle getHabilitationCycleRandomSampleGenerator() {
        return new HabilitationCycle().id(longCount.incrementAndGet()).keycloakUserId(UUID.randomUUID().toString());
    }
}
