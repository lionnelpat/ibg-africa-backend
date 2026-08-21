package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.EtudiantTestSamples.*;
import static org.forbidec.domain.EvaluationRealiseeTestSamples.*;
import static org.forbidec.domain.EvenementEtudiantTestSamples.*;
import static org.forbidec.domain.InscriptionCycleTestSamples.*;
import static org.forbidec.domain.PaysTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etudiant.class);
        Etudiant etudiant1 = getEtudiantSample1();
        Etudiant etudiant2 = new Etudiant();
        assertThat(etudiant1).isNotEqualTo(etudiant2);

        etudiant2.setId(etudiant1.getId());
        assertThat(etudiant1).isEqualTo(etudiant2);

        etudiant2 = getEtudiantSample2();
        assertThat(etudiant1).isNotEqualTo(etudiant2);
    }

    @Test
    void paysTest() {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Pays paysBack = getPaysRandomSampleGenerator();

        etudiant.setPays(paysBack);
        assertThat(etudiant.getPays()).isEqualTo(paysBack);

        etudiant.pays(null);
        assertThat(etudiant.getPays()).isNull();
    }

    @Test
    void inscriptionTest() {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        InscriptionCycle inscriptionCycleBack = getInscriptionCycleRandomSampleGenerator();

        etudiant.addInscription(inscriptionCycleBack);
        assertThat(etudiant.getInscriptions()).containsOnly(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.removeInscription(inscriptionCycleBack);
        assertThat(etudiant.getInscriptions()).doesNotContain(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getEtudiant()).isNull();

        etudiant.inscriptions(new HashSet<>(Set.of(inscriptionCycleBack)));
        assertThat(etudiant.getInscriptions()).containsOnly(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.setInscriptions(new HashSet<>());
        assertThat(etudiant.getInscriptions()).doesNotContain(inscriptionCycleBack);
        assertThat(inscriptionCycleBack.getEtudiant()).isNull();
    }

    @Test
    void evenementTest() {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        EvenementEtudiant evenementEtudiantBack = getEvenementEtudiantRandomSampleGenerator();

        etudiant.addEvenement(evenementEtudiantBack);
        assertThat(etudiant.getEvenements()).containsOnly(evenementEtudiantBack);
        assertThat(evenementEtudiantBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.removeEvenement(evenementEtudiantBack);
        assertThat(etudiant.getEvenements()).doesNotContain(evenementEtudiantBack);
        assertThat(evenementEtudiantBack.getEtudiant()).isNull();

        etudiant.evenements(new HashSet<>(Set.of(evenementEtudiantBack)));
        assertThat(etudiant.getEvenements()).containsOnly(evenementEtudiantBack);
        assertThat(evenementEtudiantBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.setEvenements(new HashSet<>());
        assertThat(etudiant.getEvenements()).doesNotContain(evenementEtudiantBack);
        assertThat(evenementEtudiantBack.getEtudiant()).isNull();
    }

    @Test
    void noteTest() {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        EvaluationRealisee evaluationRealiseeBack = getEvaluationRealiseeRandomSampleGenerator();

        etudiant.addNote(evaluationRealiseeBack);
        assertThat(etudiant.getNotes()).containsOnly(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.removeNote(evaluationRealiseeBack);
        assertThat(etudiant.getNotes()).doesNotContain(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEtudiant()).isNull();

        etudiant.notes(new HashSet<>(Set.of(evaluationRealiseeBack)));
        assertThat(etudiant.getNotes()).containsOnly(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.setNotes(new HashSet<>());
        assertThat(etudiant.getNotes()).doesNotContain(evaluationRealiseeBack);
        assertThat(evaluationRealiseeBack.getEtudiant()).isNull();
    }
}
