package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EvenementEtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EvenementEtudiant getEvenementEtudiantSample1() {
        return new EvenementEtudiant().id(1L).intitule("intitule1").commentaire("commentaire1");
    }

    public static EvenementEtudiant getEvenementEtudiantSample2() {
        return new EvenementEtudiant().id(2L).intitule("intitule2").commentaire("commentaire2");
    }

    public static EvenementEtudiant getEvenementEtudiantRandomSampleGenerator() {
        return new EvenementEtudiant()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
