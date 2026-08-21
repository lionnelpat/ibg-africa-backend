package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;
import static org.forbidec.domain.TypeTacheTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeTacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeTache.class);
        TypeTache typeTache1 = getTypeTacheSample1();
        TypeTache typeTache2 = new TypeTache();
        assertThat(typeTache1).isNotEqualTo(typeTache2);

        typeTache2.setId(typeTache1.getId());
        assertThat(typeTache1).isEqualTo(typeTache2);

        typeTache2 = getTypeTacheSample2();
        assertThat(typeTache1).isNotEqualTo(typeTache2);
    }

    @Test
    void evaluationTest() {
        TypeTache typeTache = getTypeTacheRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        typeTache.addEvaluation(evaluationPrevueBack);
        assertThat(typeTache.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getTypeTache()).isEqualTo(typeTache);

        typeTache.removeEvaluation(evaluationPrevueBack);
        assertThat(typeTache.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getTypeTache()).isNull();

        typeTache.evaluations(new HashSet<>(Set.of(evaluationPrevueBack)));
        assertThat(typeTache.getEvaluations()).containsOnly(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getTypeTache()).isEqualTo(typeTache);

        typeTache.setEvaluations(new HashSet<>());
        assertThat(typeTache.getEvaluations()).doesNotContain(evaluationPrevueBack);
        assertThat(evaluationPrevueBack.getTypeTache()).isNull();
    }
}
