package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.BaremeMentionTestSamples.*;
import static org.forbidec.domain.CentreFormationTestSamples.*;
import static org.forbidec.domain.CycleTestSamples.*;
import static org.forbidec.domain.HabilitationCycleTestSamples.*;
import static org.forbidec.domain.ParametreTestSamples.*;
import static org.forbidec.domain.PaysTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreFormationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentreFormation.class);
        CentreFormation centreFormation1 = getCentreFormationSample1();
        CentreFormation centreFormation2 = new CentreFormation();
        assertThat(centreFormation1).isNotEqualTo(centreFormation2);

        centreFormation2.setId(centreFormation1.getId());
        assertThat(centreFormation1).isEqualTo(centreFormation2);

        centreFormation2 = getCentreFormationSample2();
        assertThat(centreFormation1).isNotEqualTo(centreFormation2);
    }

    @Test
    void paysTest() {
        CentreFormation centreFormation = getCentreFormationRandomSampleGenerator();
        Pays paysBack = getPaysRandomSampleGenerator();

        centreFormation.setPays(paysBack);
        assertThat(centreFormation.getPays()).isEqualTo(paysBack);

        centreFormation.pays(null);
        assertThat(centreFormation.getPays()).isNull();
    }

    @Test
    void baremeTest() {
        CentreFormation centreFormation = getCentreFormationRandomSampleGenerator();
        BaremeMention baremeMentionBack = getBaremeMentionRandomSampleGenerator();

        centreFormation.addBareme(baremeMentionBack);
        assertThat(centreFormation.getBaremes()).containsOnly(baremeMentionBack);
        assertThat(baremeMentionBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.removeBareme(baremeMentionBack);
        assertThat(centreFormation.getBaremes()).doesNotContain(baremeMentionBack);
        assertThat(baremeMentionBack.getCentre()).isNull();

        centreFormation.baremes(new HashSet<>(Set.of(baremeMentionBack)));
        assertThat(centreFormation.getBaremes()).containsOnly(baremeMentionBack);
        assertThat(baremeMentionBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.setBaremes(new HashSet<>());
        assertThat(centreFormation.getBaremes()).doesNotContain(baremeMentionBack);
        assertThat(baremeMentionBack.getCentre()).isNull();
    }

    @Test
    void parametreTest() {
        CentreFormation centreFormation = getCentreFormationRandomSampleGenerator();
        Parametre parametreBack = getParametreRandomSampleGenerator();

        centreFormation.addParametre(parametreBack);
        assertThat(centreFormation.getParametres()).containsOnly(parametreBack);
        assertThat(parametreBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.removeParametre(parametreBack);
        assertThat(centreFormation.getParametres()).doesNotContain(parametreBack);
        assertThat(parametreBack.getCentre()).isNull();

        centreFormation.parametres(new HashSet<>(Set.of(parametreBack)));
        assertThat(centreFormation.getParametres()).containsOnly(parametreBack);
        assertThat(parametreBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.setParametres(new HashSet<>());
        assertThat(centreFormation.getParametres()).doesNotContain(parametreBack);
        assertThat(parametreBack.getCentre()).isNull();
    }

    @Test
    void cycleTest() {
        CentreFormation centreFormation = getCentreFormationRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        centreFormation.addCycle(cycleBack);
        assertThat(centreFormation.getCycles()).containsOnly(cycleBack);
        assertThat(cycleBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.removeCycle(cycleBack);
        assertThat(centreFormation.getCycles()).doesNotContain(cycleBack);
        assertThat(cycleBack.getCentre()).isNull();

        centreFormation.cycles(new HashSet<>(Set.of(cycleBack)));
        assertThat(centreFormation.getCycles()).containsOnly(cycleBack);
        assertThat(cycleBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.setCycles(new HashSet<>());
        assertThat(centreFormation.getCycles()).doesNotContain(cycleBack);
        assertThat(cycleBack.getCentre()).isNull();
    }

    @Test
    void habilitationTest() {
        CentreFormation centreFormation = getCentreFormationRandomSampleGenerator();
        HabilitationCycle habilitationCycleBack = getHabilitationCycleRandomSampleGenerator();

        centreFormation.addHabilitation(habilitationCycleBack);
        assertThat(centreFormation.getHabilitations()).containsOnly(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.removeHabilitation(habilitationCycleBack);
        assertThat(centreFormation.getHabilitations()).doesNotContain(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCentre()).isNull();

        centreFormation.habilitations(new HashSet<>(Set.of(habilitationCycleBack)));
        assertThat(centreFormation.getHabilitations()).containsOnly(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCentre()).isEqualTo(centreFormation);

        centreFormation.setHabilitations(new HashSet<>());
        assertThat(centreFormation.getHabilitations()).doesNotContain(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCentre()).isNull();
    }
}
