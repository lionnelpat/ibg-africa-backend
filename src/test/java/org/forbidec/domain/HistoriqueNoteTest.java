package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EvaluationRealiseeTestSamples.*;
import static org.forbidec.domain.HistoriqueNoteTestSamples.*;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueNote.class);
        HistoriqueNote historiqueNote1 = getHistoriqueNoteSample1();
        HistoriqueNote historiqueNote2 = new HistoriqueNote();
        assertThat(historiqueNote1).isNotEqualTo(historiqueNote2);

        historiqueNote2.setId(historiqueNote1.getId());
        assertThat(historiqueNote1).isEqualTo(historiqueNote2);

        historiqueNote2 = getHistoriqueNoteSample2();
        assertThat(historiqueNote1).isNotEqualTo(historiqueNote2);
    }

    @Test
    void evaluationRealiseeTest() {
        HistoriqueNote historiqueNote = getHistoriqueNoteRandomSampleGenerator();
        EvaluationRealisee evaluationRealiseeBack = getEvaluationRealiseeRandomSampleGenerator();

        historiqueNote.setEvaluationRealisee(evaluationRealiseeBack);
        assertThat(historiqueNote.getEvaluationRealisee()).isEqualTo(evaluationRealiseeBack);

        historiqueNote.evaluationRealisee(null);
        assertThat(historiqueNote.getEvaluationRealisee()).isNull();
    }
}
