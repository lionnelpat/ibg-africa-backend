package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EnseignantTestSamples.*;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnseignantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enseignant.class);
        Enseignant enseignant1 = getEnseignantSample1();
        Enseignant enseignant2 = new Enseignant();
        assertThat(enseignant1).isNotEqualTo(enseignant2);

        enseignant2.setId(enseignant1.getId());
        assertThat(enseignant1).isEqualTo(enseignant2);

        enseignant2 = getEnseignantSample2();
        assertThat(enseignant1).isNotEqualTo(enseignant2);
    }

    @Test
    void evaluationTest() {
        Enseignant enseignant = getEnseignantRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        enseignant.addEvaluation(evaluationPrevueBack);
        assertThat(enseignant.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getEnseignant()).isEqualTo(enseignant);

        enseignant.removeEvaluation(evaluationPrevueBack);
        assertThat(enseignant.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getEnseignant()).isNull();

        enseignant.evaluations(new HashSet<>(Set.of(evaluationPrevueBack)));
        assertThat(enseignant.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getEnseignant()).isEqualTo(enseignant);

        enseignant.setEvaluations(new HashSet<>());
        assertThat(enseignant.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getEnseignant()).isNull();
    }
}
