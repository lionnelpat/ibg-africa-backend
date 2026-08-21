package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SousMatiereTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SousMatiere getSousMatiereSample1() {
        return new SousMatiere()
            .id(1L)
            .intitule("intitule1")
            .libelleLong("libelleLong1")
            .libelleCourt("libelleCourt1")
            .commentaire("commentaire1");
    }

    public static SousMatiere getSousMatiereSample2() {
        return new SousMatiere()
            .id(2L)
            .intitule("intitule2")
            .libelleLong("libelleLong2")
            .libelleCourt("libelleCourt2")
            .commentaire("commentaire2");
    }

    public static SousMatiere getSousMatiereRandomSampleGenerator() {
        return new SousMatiere()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .libelleLong(UUID.randomUUID().toString())
            .libelleCourt(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
