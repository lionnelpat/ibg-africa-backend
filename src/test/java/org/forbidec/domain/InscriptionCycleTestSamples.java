package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InscriptionCycleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InscriptionCycle getInscriptionCycleSample1() {
        return new InscriptionCycle()
            .id(1L)
            .groupe("groupe1")
            .commentaire1("commentaire11")
            .commentaire2("commentaire21")
            .commentaire3("commentaire31")
            .commentaire5("commentaire51");
    }

    public static InscriptionCycle getInscriptionCycleSample2() {
        return new InscriptionCycle()
            .id(2L)
            .groupe("groupe2")
            .commentaire1("commentaire12")
            .commentaire2("commentaire22")
            .commentaire3("commentaire32")
            .commentaire5("commentaire52");
    }

    public static InscriptionCycle getInscriptionCycleRandomSampleGenerator() {
        return new InscriptionCycle()
            .id(longCount.incrementAndGet())
            .groupe(UUID.randomUUID().toString())
            .commentaire1(UUID.randomUUID().toString())
            .commentaire2(UUID.randomUUID().toString())
            .commentaire3(UUID.randomUUID().toString())
            .commentaire5(UUID.randomUUID().toString());
    }
}
