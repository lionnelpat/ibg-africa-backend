package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EvaluationRealiseeCriteriaTest {

    @Test
    void newEvaluationRealiseeCriteriaHasAllFiltersNullTest() {
        var evaluationRealiseeCriteria = new EvaluationRealiseeCriteria();
        assertThat(evaluationRealiseeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void evaluationRealiseeCriteriaFluentMethodsCreatesFiltersTest() {
        var evaluationRealiseeCriteria = new EvaluationRealiseeCriteria();

        setAllFilters(evaluationRealiseeCriteria);

        assertThat(evaluationRealiseeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void evaluationRealiseeCriteriaCopyCreatesNullFilterTest() {
        var evaluationRealiseeCriteria = new EvaluationRealiseeCriteria();
        var copy = evaluationRealiseeCriteria.copy();

        assertThat(evaluationRealiseeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(evaluationRealiseeCriteria)
        );
    }

    @Test
    void evaluationRealiseeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var evaluationRealiseeCriteria = new EvaluationRealiseeCriteria();
        setAllFilters(evaluationRealiseeCriteria);

        var copy = evaluationRealiseeCriteria.copy();

        assertThat(evaluationRealiseeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(evaluationRealiseeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var evaluationRealiseeCriteria = new EvaluationRealiseeCriteria();

        assertThat(evaluationRealiseeCriteria).hasToString("EvaluationRealiseeCriteria{}");
    }

    private static void setAllFilters(EvaluationRealiseeCriteria evaluationRealiseeCriteria) {
        evaluationRealiseeCriteria.id();
        evaluationRealiseeCriteria.note();
        evaluationRealiseeCriteria.statut();
        evaluationRealiseeCriteria.compteDansMoyenne();
        evaluationRealiseeCriteria.dateDebut();
        evaluationRealiseeCriteria.dateFin();
        evaluationRealiseeCriteria.commentaire1();
        evaluationRealiseeCriteria.commentaire2();
        evaluationRealiseeCriteria.commentaire3();
        evaluationRealiseeCriteria.saisiePar();
        evaluationRealiseeCriteria.saisieLe();
        evaluationRealiseeCriteria.valideePar();
        evaluationRealiseeCriteria.valideeLe();
        evaluationRealiseeCriteria.evaluationPrevueId();
        evaluationRealiseeCriteria.etudiantId();
        evaluationRealiseeCriteria.historiqueId();
        evaluationRealiseeCriteria.distinct();
    }

    private static Condition<EvaluationRealiseeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getCompteDansMoyenne()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getCommentaire1()) &&
                condition.apply(criteria.getCommentaire2()) &&
                condition.apply(criteria.getCommentaire3()) &&
                condition.apply(criteria.getSaisiePar()) &&
                condition.apply(criteria.getSaisieLe()) &&
                condition.apply(criteria.getValideePar()) &&
                condition.apply(criteria.getValideeLe()) &&
                condition.apply(criteria.getEvaluationPrevueId()) &&
                condition.apply(criteria.getEtudiantId()) &&
                condition.apply(criteria.getHistoriqueId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EvaluationRealiseeCriteria> copyFiltersAre(
        EvaluationRealiseeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getCompteDansMoyenne(), copy.getCompteDansMoyenne()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getCommentaire1(), copy.getCommentaire1()) &&
                condition.apply(criteria.getCommentaire2(), copy.getCommentaire2()) &&
                condition.apply(criteria.getCommentaire3(), copy.getCommentaire3()) &&
                condition.apply(criteria.getSaisiePar(), copy.getSaisiePar()) &&
                condition.apply(criteria.getSaisieLe(), copy.getSaisieLe()) &&
                condition.apply(criteria.getValideePar(), copy.getValideePar()) &&
                condition.apply(criteria.getValideeLe(), copy.getValideeLe()) &&
                condition.apply(criteria.getEvaluationPrevueId(), copy.getEvaluationPrevueId()) &&
                condition.apply(criteria.getEtudiantId(), copy.getEtudiantId()) &&
                condition.apply(criteria.getHistoriqueId(), copy.getHistoriqueId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
