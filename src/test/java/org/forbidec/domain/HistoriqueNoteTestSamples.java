package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HistoriqueNoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HistoriqueNote getHistoriqueNoteSample1() {
        return new HistoriqueNote().id(1L).motif("motif1").modifiePar("modifiePar1");
    }

    public static HistoriqueNote getHistoriqueNoteSample2() {
        return new HistoriqueNote().id(2L).motif("motif2").modifiePar("modifiePar2");
    }

    public static HistoriqueNote getHistoriqueNoteRandomSampleGenerator() {
        return new HistoriqueNote()
            .id(longCount.incrementAndGet())
            .motif(UUID.randomUUID().toString())
            .modifiePar(UUID.randomUUID().toString());
    }
}
