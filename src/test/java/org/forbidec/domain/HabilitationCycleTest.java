package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CentreFormationTestSamples.*;
import static org.forbidec.domain.CycleTestSamples.*;
import static org.forbidec.domain.HabilitationCycleTestSamples.*;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HabilitationCycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HabilitationCycle.class);
        HabilitationCycle habilitationCycle1 = getHabilitationCycleSample1();
        HabilitationCycle habilitationCycle2 = new HabilitationCycle();
        assertThat(habilitationCycle1).isNotEqualTo(habilitationCycle2);

        habilitationCycle2.setId(habilitationCycle1.getId());
        assertThat(habilitationCycle1).isEqualTo(habilitationCycle2);

        habilitationCycle2 = getHabilitationCycleSample2();
        assertThat(habilitationCycle1).isNotEqualTo(habilitationCycle2);
    }

    @Test
    void centreTest() {
        HabilitationCycle habilitationCycle = getHabilitationCycleRandomSampleGenerator();
        CentreFormation centreFormationBack = getCentreFormationRandomSampleGenerator();

        habilitationCycle.setCentre(centreFormationBack);
        assertThat(habilitationCycle.getCentre()).isEqualTo(centreFormationBack);

        habilitationCycle.centre(null);
        assertThat(habilitationCycle.getCentre()).isNull();
    }

    @Test
    void cycleTest() {
        HabilitationCycle habilitationCycle = getHabilitationCycleRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        habilitationCycle.setCycle(cycleBack);
        assertThat(habilitationCycle.getCycle()).isEqualTo(cycleBack);

        habilitationCycle.cycle(null);
        assertThat(habilitationCycle.getCycle()).isNull();
    }
}
