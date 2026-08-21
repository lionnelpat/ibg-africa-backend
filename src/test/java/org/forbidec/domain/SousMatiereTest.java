package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;
import static org.forbidec.domain.SousMatiereTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousMatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousMatiere.class);
        SousMatiere sousMatiere1 = getSousMatiereSample1();
        SousMatiere sousMatiere2 = new SousMatiere();
        assertThat(sousMatiere1).isNotEqualTo(sousMatiere2);

        sousMatiere2.setId(sousMatiere1.getId());
        assertThat(sousMatiere1).isEqualTo(sousMatiere2);

        sousMatiere2 = getSousMatiereSample2();
        assertThat(sousMatiere1).isNotEqualTo(sousMatiere2);
    }

    @Test
    void evaluationTest() {
        SousMatiere sousMatiere = getSousMatiereRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        sousMatiere.addEvaluation(evaluationPrevueBack);
        assertThat(sousMatiere.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getSousMatiere()).isEqualTo(sousMatiere);

        sousMatiere.removeEvaluation(evaluationPrevueBack);
        assertThat(sousMatiere.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getSousMatiere()).isNull();

        sousMatiere.evaluations(new HashSet<>(Set.of(evaluationPrevueBack)));
        assertThat(sousMatiere.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getSousMatiere()).isEqualTo(sousMatiere);

        sousMatiere.setEvaluations(new HashSet<>());
        assertThat(sousMatiere.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getSousMatiere()).isNull();
    }
}
