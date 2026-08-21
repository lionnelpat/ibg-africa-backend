package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EvaluationPrevueCriteriaTest {

    @Test
    void newEvaluationPrevueCriteriaHasAllFiltersNullTest() {
        var evaluationPrevueCriteria = new EvaluationPrevueCriteria();
        assertThat(evaluationPrevueCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void evaluationPrevueCriteriaFluentMethodsCreatesFiltersTest() {
        var evaluationPrevueCriteria = new EvaluationPrevueCriteria();

        setAllFilters(evaluationPrevueCriteria);

        assertThat(evaluationPrevueCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void evaluationPrevueCriteriaCopyCreatesNullFilterTest() {
        var evaluationPrevueCriteria = new EvaluationPrevueCriteria();
        var copy = evaluationPrevueCriteria.copy();

        assertThat(evaluationPrevueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(evaluationPrevueCriteria)
        );
    }

    @Test
    void evaluationPrevueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var evaluationPrevueCriteria = new EvaluationPrevueCriteria();
        setAllFilters(evaluationPrevueCriteria);

        var copy = evaluationPrevueCriteria.copy();

        assertThat(evaluationPrevueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(evaluationPrevueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var evaluationPrevueCriteria = new EvaluationPrevueCriteria();

        assertThat(evaluationPrevueCriteria).hasToString("EvaluationPrevueCriteria{}");
    }

    private static void setAllFilters(EvaluationPrevueCriteria evaluationPrevueCriteria) {
        evaluationPrevueCriteria.id();
        evaluationPrevueCriteria.intitule();
        evaluationPrevueCriteria.libelleImpression();
        evaluationPrevueCriteria.coefficient();
        evaluationPrevueCriteria.compteDansMoyenne();
        evaluationPrevueCriteria.noteMaximale();
        evaluationPrevueCriteria.dateDebut();
        evaluationPrevueCriteria.dateFin();
        evaluationPrevueCriteria.commentaire();
        evaluationPrevueCriteria.cycleId();
        evaluationPrevueCriteria.enseignantId();
        evaluationPrevueCriteria.matiereId();
        evaluationPrevueCriteria.sousMatiereId();
        evaluationPrevueCriteria.coursId();
        evaluationPrevueCriteria.typeTacheId();
        evaluationPrevueCriteria.noteId();
        evaluationPrevueCriteria.distinct();
    }

    private static Condition<EvaluationPrevueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIntitule()) &&
                condition.apply(criteria.getLibelleImpression()) &&
                condition.apply(criteria.getCoefficient()) &&
                condition.apply(criteria.getCompteDansMoyenne()) &&
                condition.apply(criteria.getNoteMaximale()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getCycleId()) &&
                condition.apply(criteria.getEnseignantId()) &&
                condition.apply(criteria.getMatiereId()) &&
                condition.apply(criteria.getSousMatiereId()) &&
                condition.apply(criteria.getCoursId()) &&
                condition.apply(criteria.getTypeTacheId()) &&
                condition.apply(criteria.getNoteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EvaluationPrevueCriteria> copyFiltersAre(
        EvaluationPrevueCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIntitule(), copy.getIntitule()) &&
                condition.apply(criteria.getLibelleImpression(), copy.getLibelleImpression()) &&
                condition.apply(criteria.getCoefficient(), copy.getCoefficient()) &&
                condition.apply(criteria.getCompteDansMoyenne(), copy.getCompteDansMoyenne()) &&
                condition.apply(criteria.getNoteMaximale(), copy.getNoteMaximale()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getCycleId(), copy.getCycleId()) &&
                condition.apply(criteria.getEnseignantId(), copy.getEnseignantId()) &&
                condition.apply(criteria.getMatiereId(), copy.getMatiereId()) &&
                condition.apply(criteria.getSousMatiereId(), copy.getSousMatiereId()) &&
                condition.apply(criteria.getCoursId(), copy.getCoursId()) &&
                condition.apply(criteria.getTypeTacheId(), copy.getTypeTacheId()) &&
                condition.apply(criteria.getNoteId(), copy.getNoteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
