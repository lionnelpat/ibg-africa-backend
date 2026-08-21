package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;
import static org.forbidec.domain.MatiereTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matiere.class);
        Matiere matiere1 = getMatiereSample1();
        Matiere matiere2 = new Matiere();
        assertThat(matiere1).isNotEqualTo(matiere2);

        matiere2.setId(matiere1.getId());
        assertThat(matiere1).isEqualTo(matiere2);

        matiere2 = getMatiereSample2();
        assertThat(matiere1).isNotEqualTo(matiere2);
    }

    @Test
    void evaluationTest() {
        Matiere matiere = getMatiereRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        matiere.addEvaluation(evaluationPrevueBack);
        assertThat(matiere.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getMatiere()).isEqualTo(matiere);

        matiere.removeEvaluation(evaluationPrevueBack);
        assertThat(matiere.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getMatiere()).isNull();

        matiere.evaluations(new HashSet<>(Set.of(evaluationPrevueBack)));
        assertThat(matiere.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getMatiere()).isEqualTo(matiere);

        matiere.setEvaluations(new HashSet<>());
        assertThat(matiere.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getMatiere()).isNull();
    }
}
