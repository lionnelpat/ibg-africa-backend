package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnseignantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Enseignant getEnseignantSample1() {
        return new Enseignant()
            .id(1L)
            .nom("nom1")
            .prenom("prenom1")
            .libelleLong("libelleLong1")
            .libelleCourt("libelleCourt1")
            .email("email1")
            .telephone("telephone1")
            .keycloakUserId("keycloakUserId1")
            .commentaire("commentaire1");
    }

    public static Enseignant getEnseignantSample2() {
        return new Enseignant()
            .id(2L)
            .nom("nom2")
            .prenom("prenom2")
            .libelleLong("libelleLong2")
            .libelleCourt("libelleCourt2")
            .email("email2")
            .telephone("telephone2")
            .keycloakUserId("keycloakUserId2")
            .commentaire("commentaire2");
    }

    public static Enseignant getEnseignantRandomSampleGenerator() {
        return new Enseignant()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .libelleLong(UUID.randomUUID().toString())
            .libelleCourt(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .keycloakUserId(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
