package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EtudiantTestSamples.*;
import static org.forbidec.domain.EvenementEtudiantTestSamples.*;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvenementEtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvenementEtudiant.class);
        EvenementEtudiant evenementEtudiant1 = getEvenementEtudiantSample1();
        EvenementEtudiant evenementEtudiant2 = new EvenementEtudiant();
        assertThat(evenementEtudiant1).isNotEqualTo(evenementEtudiant2);

        evenementEtudiant2.setId(evenementEtudiant1.getId());
        assertThat(evenementEtudiant1).isEqualTo(evenementEtudiant2);

        evenementEtudiant2 = getEvenementEtudiantSample2();
        assertThat(evenementEtudiant1).isNotEqualTo(evenementEtudiant2);
    }

    @Test
    void etudiantTest() {
        EvenementEtudiant evenementEtudiant = getEvenementEtudiantRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        evenementEtudiant.setEtudiant(etudiantBack);
        assertThat(evenementEtudiant.getEtudiant()).isEqualTo(etudiantBack);

        evenementEtudiant.etudiant(null);
        assertThat(evenementEtudiant.getEtudiant()).isNull();
    }
}
