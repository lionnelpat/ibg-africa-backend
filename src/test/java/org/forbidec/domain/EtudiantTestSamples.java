package org.forbidec.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EtudiantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Etudiant getEtudiantSample1() {
        return new Etudiant()
            .id(1L)
            .matricule("matricule1")
            .nom("nom1")
            .prenom("prenom1")
            .particularite("particularite1")
            .email("email1")
            .telephone("telephone1")
            .anneeEntree(1)
            .anneeFinale(1)
            .keycloakUserId("keycloakUserId1")
            .commentaire("commentaire1");
    }

    public static Etudiant getEtudiantSample2() {
        return new Etudiant()
            .id(2L)
            .matricule("matricule2")
            .nom("nom2")
            .prenom("prenom2")
            .particularite("particularite2")
            .email("email2")
            .telephone("telephone2")
            .anneeEntree(2)
            .anneeFinale(2)
            .keycloakUserId("keycloakUserId2")
            .commentaire("commentaire2");
    }

    public static Etudiant getEtudiantRandomSampleGenerator() {
        return new Etudiant()
            .id(longCount.incrementAndGet())
            .matricule(UUID.randomUUID().toString())
            .nom(UUID.randomUUID().toString())
            .prenom(UUID.randomUUID().toString())
            .particularite(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telephone(UUID.randomUUID().toString())
            .anneeEntree(intCount.incrementAndGet())
            .anneeFinale(intCount.incrementAndGet())
            .keycloakUserId(UUID.randomUUID().toString())
            .commentaire(UUID.randomUUID().toString());
    }
}
