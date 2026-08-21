package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CentreFormationTestSamples.*;
import static org.forbidec.domain.CycleTestSamples.*;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;
import static org.forbidec.domain.HabilitationCycleTestSamples.*;
import static org.forbidec.domain.InscriptionCycleTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cycle.class);
        Cycle cycle1 = getCycleSample1();
        Cycle cycle2 = new Cycle();
        assertThat(cycle1).isNotEqualTo(cycle2);

        cycle2.setId(cycle1.getId());
        assertThat(cycle1).isEqualTo(cycle2);

        cycle2 = getCycleSample2();
        assertThat(cycle1).isNotEqualTo(cycle2);
    }

    @Test
    void centreTest() {
        Cycle cycle = getCycleRandomSampleGenerator();
        CentreFormation centreFormationBack = getCentreFormationRandomSampleGenerator();

        cycle.setCentre(centreFormationBack);
        assertThat(cycle.getCentre()).isEqualTo(centreFormationBack);

        cycle.centre(null);
        assertThat(cycle.getCentre()).isNull();
    }

    @Test
    void inscriptionTest() {
        Cycle cycle = getCycleRandomSampleGenerator();
        InscriptionCycle inscriptionCycleBack = getInscriptionCycleRandomSampleGenerator();

        cycle.addInscription(inscriptionCycleBack);
        assertThat(cycle.getInscriptions()).containsOnly(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getCycle()).isEqualTo(cycle);

        cycle.removeInscription(inscriptionCycleBack);
        assertThat(cycle.getInscriptions()).doesNotContain(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getCycle()).isNull();

        cycle.inscriptions(new HashSet<>(Set.of(inscriptionCycleBack)));
        assertThat(cycle.getInscriptions()).containsOnly(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getCycle()).isEqualTo(cycle);

        cycle.setInscriptions(new HashSet<>());
        assertThat(cycle.getInscriptions()).doesNotContain(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getCycle()).isNull();
    }

    @Test
    void evaluationTest() {
        Cycle cycle = getCycleRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        cycle.addEvaluation(evaluationPrevueBack);
        assertThat(cycle.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCycle()).isEqualTo(cycle);

        cycle.removeEvaluation(evaluationPrevueBack);
        assertThat(cycle.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCycle()).isNull();

        cycle.evaluations(new HashSet<>(Set.of(evaluationPrevueBack)));
        assertThat(cycle.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCycle()).isEqualTo(cycle);

        cycle.setEvaluations(new HashSet<>());
        assertThat(cycle.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCycle()).isNull();
    }

    @Test
    void habilitationTest() {
        Cycle cycle = getCycleRandomSampleGenerator();
        HabilitationCycle habilitationCycleBack = getHabilitationCycleRandomSampleGenerator();

        cycle.addHabilitation(habilitationCycleBack);
        assertThat(cycle.getHabilitations()).containsOnly(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCycle()).isEqualTo(cycle);

        cycle.removeHabilitation(habilitationCycleBack);
        assertThat(cycle.getHabilitations()).doesNotContain(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCycle()).isNull();

        cycle.habilitations(new HashSet<>(Set.of(habilitationCycleBack)));
        assertThat(cycle.getHabilitations()).containsOnly(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCycle()).isEqualTo(cycle);

        cycle.setHabilitations(new HashSet<>());
        assertThat(cycle.getHabilitations()).doesNotContain(habilitationCycleBack);
        assertThat(habilitationCycleBack.getCycle()).isNull();
    }
}
