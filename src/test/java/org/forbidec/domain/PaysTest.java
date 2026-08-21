package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CentreFormationTestSamples.*;
import static org.forbidec.domain.EtudiantTestSamples.*;
import static org.forbidec.domain.PaysTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaysTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pays.class);
        Pays pays1 = getPaysSample1();
        Pays pays2 = new Pays();
        assertThat(pays1).isNotEqualTo(pays2);

        pays2.setId(pays1.getId());
        assertThat(pays1).isEqualTo(pays2);

        pays2 = getPaysSample2();
        assertThat(pays1).isNotEqualTo(pays2);
    }

    @Test
    void centreTest() {
        Pays pays = getPaysRandomSampleGenerator();
        CentreFormation centreFormationBack = getCentreFormationRandomSampleGenerator();

        pays.addCentre(centreFormationBack);
        assertThat(pays.getCentres()).containsOnly(centreFormationBack);
        assertThat(centreFormationBack.getPays()).isEqualTo(pays);

        pays.removeCentre(centreFormationBack);
        assertThat(pays.getCentres()).doesNotContain(centreFormationBack);
        assertThat(centreFormationBack.getPays()).isNull();

        pays.centres(new HashSet<>(Set.of(centreFormationBack)));
        assertThat(pays.getCentres()).containsOnly(centreFormationBack);
        assertThat(centreFormationBack.getPays()).isEqualTo(pays);

        pays.setCentres(new HashSet<>());
        assertThat(pays.getCentres()).doesNotContain(centreFormationBack);
        assertThat(centreFormationBack.getPays()).isNull();
    }

    @Test
    void etudiantTest() {
        Pays pays = getPaysRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        pays.addEtudiant(etudiantBack);
        assertThat(pays.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getPays()).isEqualTo(pays);

        pays.removeEtudiant(etudiantBack);
        assertThat(pays.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getPays()).isNull();

        pays.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(pays.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getPays()).isEqualTo(pays);

        pays.setEtudiants(new HashSet<>());
        assertThat(pays.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getPays()).isNull();
    }
}
