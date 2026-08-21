package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EvaluationRealiseeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EvaluationRealisee getEvaluationRealiseeSample1() {
        return new EvaluationRealisee()
            .id(1L)
            .commentaire1("commentaire11")
            .commentaire2("commentaire21")
            .commentaire3("commentaire31")
            .saisiePar("saisiePar1")
            .valideePar("valideePar1");
    }

    public static EvaluationRealisee getEvaluationRealiseeSample2() {
        return new EvaluationRealisee()
            .id(2L)
            .commentaire1("commentaire12")
            .commentaire2("commentaire22")
            .commentaire3("commentaire32")
            .saisiePar("saisiePar2")
            .valideePar("valideePar2");
    }

    public static EvaluationRealisee getEvaluationRealiseeRandomSampleGenerator() {
        return new EvaluationRealisee()
            .id(longCount.incrementAndGet())
            .commentaire1(UUID.randomUUID().toString())
            .commentaire2(UUID.randomUUID().toString())
            .commentaire3(UUID.randomUUID().toString())
            .saisiePar(UUID.randomUUID().toString())
            .valideePar(UUID.randomUUID().toString());
    }
}
