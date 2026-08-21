package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CycleTestSamples.*;
import static org.forbidec.domain.EtudiantTestSamples.*;
import static org.forbidec.domain.InscriptionCycleTestSamples.*;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InscriptionCycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionCycle.class);
        InscriptionCycle inscriptionCycle1 = getInscriptionCycleSample1();
        InscriptionCycle inscriptionCycle2 = new InscriptionCycle();
        assertThat(inscriptionCycle1).isNotEqualTo(inscriptionCycle2);

        inscriptionCycle2.setId(inscriptionCycle1.getId());
        assertThat(inscriptionCycle1).isEqualTo(inscriptionCycle2);

        inscriptionCycle2 = getInscriptionCycleSample2();
        assertThat(inscriptionCycle1).isNotEqualTo(inscriptionCycle2);
    }

    @Test
    void cycleTest() {
        InscriptionCycle inscriptionCycle = getInscriptionCycleRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        inscriptionCycle.setCycle(cycleBack);
        assertThat(inscriptionCycle.getCycle()).isEqualTo(cycleBack);

        inscriptionCycle.cycle(null);
        assertThat(inscriptionCycle.getCycle()).isNull();
    }

    @Test
    void etudiantTest() {
        InscriptionCycle inscriptionCycle = getInscriptionCycleRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        inscriptionCycle.setEtudiant(etudiantBack);
        assertThat(inscriptionCycle.getEtudiant()).isEqualTo(etudiantBack);

        inscriptionCycle.etudiant(null);
        assertThat(inscriptionCycle.getEtudiant()).isNull();
    }
}
