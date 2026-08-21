package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CoursTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Cours getCoursSample1() {
        return new Cours()
            .id(1L)
            .intitule("intitule1")
            .libelleLong("libelleLong1")
            .libelleCourt("libelleCourt1")
            .ordreAffichage(1)
            .nbPeriodes(1)
            .commentaire("commentaire1");
    }

    public static Cours getCoursSample2() {
        return new Cours()
            .id(2L)
            .intitule("intitule2")
            .libelleLong("libelleLong2")
            .libelleCourt("libelleCourt2")
            .ordreAffichage(2)
            .nbPeriodes(2)
            .commentaire("commentaire2");
    }

    public static Cours getCoursRandomSampleGenerator() {
        return new Cours()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .libelleLong(UUID.randomUUID().toString())
            .libelleCourt(UUID.randomUUID().toString())
            .ordreAffichage(intCount.incrementAndGet())
            .nbPeriodes(intCount.incrementAndGet())
            .commentaire(UUID.randomUUID().toString());
    }
}
