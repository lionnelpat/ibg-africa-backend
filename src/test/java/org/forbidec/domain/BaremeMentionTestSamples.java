package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BaremeMentionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BaremeMention getBaremeMentionSample1() {
        return new BaremeMention()
            .id(1L)
            .libelleLong("libelleLong1")
            .libelleCourt("libelleCourt1")
            .ordreAffichage(1)
            .commentaire("commentaire1");
    }

    public static BaremeMention getBaremeMentionSample2() {
        return new BaremeMention()
            .id(2L)
            .libelleLong("libelleLong2")
            .libelleCourt("libelleCourt2")
            .ordreAffichage(2)
            .commentaire("commentaire2");
    }

    public static BaremeMention getBaremeMentionRandomSampleGenerator() {
        return new BaremeMention()
            .id(longCount.incrementAndGet())
            .libelleLong(UUID.randomUUID().toString())
            .libelleCourt(UUID.randomUUID().toString())
            .ordreAffichage(intCount.incrementAndGet())
            .commentaire(UUID.randomUUID().toString());
    }
}
