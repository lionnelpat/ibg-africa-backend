package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EvaluationPrevueTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EvaluationPrevue getEvaluationPrevueSample1() {
        return new EvaluationPrevue().id(1L).intitule("intitule1").libelleImpression("libelleImpression1").commentaire("commentaire1");
    }

    public static EvaluationPrevue getEvaluationPrevueSample2() {
        return new EvaluationPrevue().id(2L).intitule("intitule2").libelleImpression("libelleImpression2").commentaire("commentaire2");
    }

    public static EvaluationPrevue getEvaluationPrevueRandomSampleGenerator() {
        return new EvaluationPrevue()
            .id(longCount.incrementAndGet())
            .intitule(UUID.randomUUID().toString())
            .libelleImpression(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
