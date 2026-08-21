package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CoursTestSamples.*;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cours.class);
        Cours cours1 = getCoursSample1();
        Cours cours2 = new Cours();
        assertThat(cours1).isNotEqualTo(cours2);

        cours2.setId(cours1.getId());
        assertThat(cours1).isEqualTo(cours2);

        cours2 = getCoursSample2();
        assertThat(cours1).isNotEqualTo(cours2);
    }

    @Test
    void evaluationTest() {
        Cours cours = getCoursRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        cours.addEvaluation(evaluationPrevueBack);
        assertThat(cours.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCours()).isEqualTo(cours);

        cours.removeEvaluation(evaluationPrevueBack);
        assertThat(cours.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCours()).isNull();

        cours.evaluations(new HashSet<>(Set.of(evaluationPrevueBack)));
        assertThat(cours.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCours()).isEqualTo(cours);

        cours.setEvaluations(new HashSet<>());
        assertThat(cours.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getCours()).isNull();
    }
}
