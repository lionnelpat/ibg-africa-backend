package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ParametreTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Parametre getParametreSample1() {
        return new Parametre().id(1L).cle("cle1").libelle("libelle1").valeur("valeur1");
    }

    public static Parametre getParametreSample2() {
        return new Parametre().id(2L).cle("cle2").libelle("libelle2").valeur("valeur2");
    }

    public static Parametre getParametreRandomSampleGenerator() {
        return new Parametre()
            .id(longCount.incrementAndGet())
            .cle(UUID.randomUUID().toString())
            .libelle(UUID.randomUUID().toString())
            .valeur(UUID.randomUUID().toString());
    }
}
