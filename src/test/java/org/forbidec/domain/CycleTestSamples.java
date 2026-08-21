package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cycle getCycleSample1() {
        return new Cycle().id(1L).annee(1).libelle("libelle1").commentaire("commentaire1");
    }

    public static Cycle getCycleSample2() {
        return new Cycle().id(2L).annee(2).libelle("libelle2").commentaire("commentaire2");
    }

    public static Cycle getCycleRandomSampleGenerator() {
        return new Cycle()
            .id(longCount.incrementAndGet())
            .annee(intCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
