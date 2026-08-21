package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EtudiantTestSamples.*;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;
import static org.forbidec.domain.EvaluationRealiseeTestSamples.*;
import static org.forbidec.domain.HistoriqueNoteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaluationRealiseeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationRealisee.class);
        EvaluationRealisee evaluationRealisee1 = getEvaluationRealiseeSample1();
        EvaluationRealisee evaluationRealisee2 = new EvaluationRealisee();
        assertThat(evaluationRealisee1).isNotEqualTo(evaluationRealisee2);

        evaluationRealisee2.setId(evaluationRealisee1.getId());
        assertThat(evaluationRealisee1).isEqualTo(evaluationRealisee2);

        evaluationRealisee2 = getEvaluationRealiseeSample2();
        assertThat(evaluationRealisee1).isNotEqualTo(evaluationRealisee2);
    }

    @Test
    void evaluationPrevueTest() {
        EvaluationRealisee evaluationRealisee = getEvaluationRealiseeRandomSampleGenerator();
        EvaluationPrevue evaluationPrevueBack = getEvaluationPrevueRandomSampleGenerator();

        evaluationRealisee.setEvaluationPrevue(evaluationPrevueBack);
        assertThat(evaluationRealisee.getEvaluationPrevue()).isEqualTo(evaluationPrevueBack);

        evaluationRealisee.evaluationPrevue(null);
        assertThat(evaluationRealisee.getEvaluationPrevue()).isNull();
    }

    @Test
    void etudiantTest() {
        EvaluationRealisee evaluationRealisee = getEvaluationRealiseeRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        evaluationRealisee.setEtudiant(etudiantBack);
        assertThat(evaluationRealisee.getEtudiant()).isEqualTo(etudiantBack);

        evaluationRealisee.etudiant(null);
        assertThat(evaluationRealisee.getEtudiant()).isNull();
    }

    @Test
    void historiqueTest() {
        EvaluationRealisee evaluationRealisee = getEvaluationRealiseeRandomSampleGenerator();
        HistoriqueNote historiqueNoteBack = getHistoriqueNoteRandomSampleGenerator();

        evaluationRealisee.addHistorique(historiqueNoteBack);
        assertThat(evaluationRealisee.getHistoriques()).containsOnly(historiqueNoteBack);
        assertThat(historiqueNoteBack.getEvaluationRealisee()).isEqualTo(evaluationRealisee);

        evaluationRealisee.removeHistorique(historiqueNoteBack);
        assertThat(evaluationRealisee.getHistoriques()).doesNotContain(historiqueNoteBack);
        assertThat(historiqueNoteBack.getEvaluationRealisee()).isNull();

        evaluationRealisee.historiques(new HashSet<>(Set.of(historiqueNoteBack)));
        assertThat(evaluationRealisee.getHistoriques()).containsOnly(historiqueNoteBack);
        assertThat(historiqueNoteBack.getEvaluationRealisee()).isEqualTo(evaluationRealisee);

        evaluationRealisee.setHistoriques(new HashSet<>());
        assertThat(evaluationRealisee.getHistoriques()).doesNotContain(historiqueNoteBack);
        assertThat(historiqueNoteBack.getEvaluationRealisee()).isNull();
    }
}
