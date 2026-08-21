package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MatiereTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Matiere getMatiereSample1() {
        return new Matiere()
            .id(1L)
            .intitule("intitule1")
            .libelleLong("libelleLong1")
            .libelleCourt("libelleCourt1")
            .commentaire("commentaire1");
    }

    public static Matiere getMatiereSample2() {
        return new Matiere()
            .id(2L)
            .intitule("intitule2")
            .libelleLong("libelleLong2")
            .libelleCourt("libelleCourt2")
            .commentaire("commentaire2");
    }

    public static Matiere getMatiereRandomSampleGenerator() {
        return new Matiere()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .libelleLong(UUID.randomUUID().toString())
            .libelleCourt(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
