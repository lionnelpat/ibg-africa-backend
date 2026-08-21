package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaysTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pays getPaysSample1() {
        return new Pays().id(1L).codeIso("codeIso1").nom("nom1").langue("langue1").fuseau("fuseau1");
    }

    public static Pays getPaysSample2() {
        return new Pays().id(2L).codeIso("codeIso2").nom("nom2").langue("langue2").fuseau("fuseau2");
    }

    public static Pays getPaysRandomSampleGenerator() {
        return new Pays()
            .id(longCount.incrementAndGet())
            .codeIso(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .langue(UUID.randomUUID().toString())
            .fuseau(UUID.randomUUID().toString());
    }
}
