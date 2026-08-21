package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HistoriqueNoteCriteriaTest {

    @Test
    void newHistoriqueNoteCriteriaHasAllFiltersNullTest() {
        var historiqueNoteCriteria = new HistoriqueNoteCriteria();
        assertThat(historiqueNoteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void historiqueNoteCriteriaFluentMethodsCreatesFiltersTest() {
        var historiqueNoteCriteria = new HistoriqueNoteCriteria();

        setAllFilters(historiqueNoteCriteria);

        assertThat(historiqueNoteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void historiqueNoteCriteriaCopyCreatesNullFilterTest() {
        var historiqueNoteCriteria = new HistoriqueNoteCriteria();
        var copy = historiqueNoteCriteria.copy();

        assertThat(historiqueNoteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueNoteCriteria)
        );
    }

    @Test
    void historiqueNoteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var historiqueNoteCriteria = new HistoriqueNoteCriteria();
        setAllFilters(historiqueNoteCriteria);

        var copy = historiqueNoteCriteria.copy();

        assertThat(historiqueNoteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueNoteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var historiqueNoteCriteria = new HistoriqueNoteCriteria();

        assertThat(historiqueNoteCriteria).hasToString("HistoriqueNoteCriteria{}");
    }

    private static void setAllFilters(HistoriqueNoteCriteria historiqueNoteCriteria) {
        historiqueNoteCriteria.id();
        historiqueNoteCriteria.noteAvant();
        historiqueNoteCriteria.noteApres();
        historiqueNoteCriteria.statutAvant();
        historiqueNoteCriteria.statutApres();
        historiqueNoteCriteria.motif();
        historiqueNoteCriteria.modifiePar();
        historiqueNoteCriteria.modifieLe();
        historiqueNoteCriteria.evaluationRealiseeId();
        historiqueNoteCriteria.distinct();
    }

    private static Condition<HistoriqueNoteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNoteAvant()) &&
                condition.apply(criteria.getNoteApres()) &&
                condition.apply(criteria.getStatutAvant()) &&
                condition.apply(criteria.getStatutApres()) &&
                condition.apply(criteria.getMotif()) &&
                condition.apply(criteria.getModifiePar()) &&
                condition.apply(criteria.getModifieLe()) &&
                condition.apply(criteria.getEvaluationRealiseeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HistoriqueNoteCriteria> copyFiltersAre(
        HistoriqueNoteCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNoteAvant(), copy.getNoteAvant()) &&
                condition.apply(criteria.getNoteApres(), copy.getNoteApres()) &&
                condition.apply(criteria.getStatutAvant(), copy.getStatutAvant()) &&
                condition.apply(criteria.getStatutApres(), copy.getStatutApres()) &&
                condition.apply(criteria.getMotif(), copy.getMotif()) &&
                condition.apply(criteria.getModifiePar(), copy.getModifiePar()) &&
                condition.apply(criteria.getModifieLe(), copy.getModifieLe()) &&
                condition.apply(criteria.getEvaluationRealiseeId(), copy.getEvaluationRealiseeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
