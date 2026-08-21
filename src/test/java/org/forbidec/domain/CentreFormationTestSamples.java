package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CentreFormationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CentreFormation getCentreFormationSample1() {
        return new CentreFormation()
            .id(1L)
            .code("code1")
            .nom("nom1")
            .ville("ville1")
            .adresse("adresse1")
            .signataire("signataire1")
            .logoUrl("logoUrl1")
            .nbCyclesCursus(1);
    }

    public static CentreFormation getCentreFormationSample2() {
        return new CentreFormation()
            .id(2L)
            .code("code2")
            .nom("nom2")
            .ville("ville2")
            .adresse("adresse2")
            .signataire("signataire2")
            .logoUrl("logoUrl2")
            .nbCyclesCursus(2);
    }

    public static CentreFormation getCentreFormationRandomSampleGenerator() {
        return new CentreFormation()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .ville(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .signataire(UUID.randomUUID().toString())
            .logoUrl(UUID.randomUUID().toString())
            .nbCyclesCursus(intCount.incrementAndGet());
    }
}
