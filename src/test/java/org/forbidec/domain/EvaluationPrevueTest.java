package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CoursTestSamples.*;
import static org.forbidec.domain.CycleTestSamples.*;
import static org.forbidec.domain.EnseignantTestSamples.*;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;
import static org.forbidec.domain.EvaluationRealiseeTestSamples.*;
import static org.forbidec.domain.MatiereTestSamples.*;
import static org.forbidec.domain.SousMatiereTestSamples.*;
import static org.forbidec.domain.TypeTacheTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaluationPrevueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationPrevue.class);
        EvaluationPrevue evaluationPrevue1 = getEvaluationPrevueSample1();
        EvaluationPrevue evaluationPrevue2 = new EvaluationPrevue();
        assertThat(evaluationPrevue1).isNotEqualTo(evaluationPrevue2);

        evaluationPrevue2.setId(evaluationPrevue1.getId());
        assertThat(evaluationPrevue1).isEqualTo(evaluationPrevue2);

        evaluationPrevue2 = getEvaluationPrevueSample2();
        assertThat(evaluationPrevue1).isNotEqualTo(evaluationPrevue2);
    }

    @Test
    void cycleTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        Cycle cycleBack = getCycleRandomSampleGenerator();

        evaluationPrevue.setCycle(cycleBack);
        assertThat(evaluationPrevue.getCycle()).isEqualTo(cycleBack);

        evaluationPrevue.cycle(null);
        assertThat(evaluationPrevue.getCycle()).isNull();
    }

    @Test
    void enseignantTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        Enseignant enseignantBack = getEnseignantRandomSampleGenerator();

        evaluationPrevue.setEnseignant(enseignantBack);
        assertThat(evaluationPrevue.getEnseignant()).isEqualTo(enseignantBack);

        evaluationPrevue.enseignant(null);
        assertThat(evaluationPrevue.getEnseignant()).isNull();
    }

    @Test
    void matiereTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        evaluationPrevue.setMatiere(matiereBack);
        assertThat(evaluationPrevue.getMatiere()).isEqualTo(matiereBack);

        evaluationPrevue.matiere(null);
        assertThat(evaluationPrevue.getMatiere()).isNull();
    }

    @Test
    void sousMatiereTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        SousMatiere sousMatiereBack = getSousMatiereRandomSampleGenerator();

        evaluationPrevue.setSousMatiere(sousMatiereBack);
        assertThat(evaluationPrevue.getSousMatiere()).isEqualTo(sousMatiereBack);

        evaluationPrevue.sousMatiere(null);
        assertThat(evaluationPrevue.getSousMatiere()).isNull();
    }

    @Test
    void coursTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        Cours coursBack = getCoursRandomSampleGenerator();

        evaluationPrevue.setCours(coursBack);
        assertThat(evaluationPrevue.getCours()).isEqualTo(coursBack);

        evaluationPrevue.cours(null);
        assertThat(evaluationPrevue.getCours()).isNull();
    }

    @Test
    void typeTacheTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        TypeTache typeTacheBack = getTypeTacheRandomSampleGenerator();

        evaluationPrevue.setTypeTache(typeTacheBack);
        assertThat(evaluationPrevue.getTypeTache()).isEqualTo(typeTacheBack);

        evaluationPrevue.typeTache(null);
        assertThat(evaluationPrevue.getTypeTache()).isNull();
    }

    @Test
    void noteTest() {
        EvaluationPrevue evaluationPrevue = getEvaluationPrevueRandomSampleGenerator();
        EvaluationRealisee evaluationRealiseeBack = getEvaluationRealiseeRandomSampleGenerator();

        evaluationPrevue.addNote(evaluationRealiseeBack);
        assertThat(evaluationPrevue.getNotes()).containsOnly(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEvaluationPrevue()).isEqualTo(evaluationPrevue);

        evaluationPrevue.removeNote(evaluationRealiseeBack);
        assertThat(evaluationPrevue.getNotes()).doesNotContain(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEvaluationPrevue()).isNull();

        evaluationPrevue.notes(new HashSet<>(Set.of(evaluationRealiseeBack)));
        assertThat(evaluationPrevue.getNotes()).containsOnly(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEvaluationPrevue()).isEqualTo(evaluationPrevue);

        evaluationPrevue.setNotes(new HashSet<>());
        assertThat(evaluationPrevue.getNotes()).doesNotContain(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEvaluationPrevue()).isNull();
    }
}
