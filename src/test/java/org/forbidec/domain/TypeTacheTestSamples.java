package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TypeTacheTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TypeTache getTypeTacheSample1() {
        return new TypeTache()
            .id(1L)
            .code("code1")
            .intitule("intitule1")
            .libelleLong("libelleLong1")
            .libelleCourt("libelleCourt1")
            .commentaire("commentaire1");
    }

    public static TypeTache getTypeTacheSample2() {
        return new TypeTache()
            .id(2L)
            .code("code2")
            .intitule("intitule2")
            .libelleLong("libelleLong2")
            .libelleCourt("libelleCourt2")
            .commentaire("commentaire2");
    }

    public static TypeTache getTypeTacheRandomSampleGenerator() {
        return new TypeTache()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .intitule(UUID.randomUUID().toString())
            .libelleLong(UUID.randomUUID().toString())
            .libelleCourt(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
