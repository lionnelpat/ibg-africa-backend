package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CycleCriteriaTest {

    @Test
    void newCycleCriteriaHasAllFiltersNullTest() {
        var cycleCriteria = new CycleCriteria();
        assertThat(cycleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cycleCriteriaFluentMethodsCreatesFiltersTest() {
        var cycleCriteria = new CycleCriteria();

        setAllFilters(cycleCriteria);

        assertThat(cycleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cycleCriteriaCopyCreatesNullFilterTest() {
        var cycleCriteria = new CycleCriteria();
        var copy = cycleCriteria.copy();

        assertThat(cycleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cycleCriteria)
        );
    }

    @Test
    void cycleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cycleCriteria = new CycleCriteria();
        setAllFilters(cycleCriteria);

        var copy = cycleCriteria.copy();

        assertThat(cycleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cycleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cycleCriteria = new CycleCriteria();

        assertThat(cycleCriteria).hasToString("CycleCriteria{}");
    }

    private static void setAllFilters(CycleCriteria cycleCriteria) {
        cycleCriteria.id();
        cycleCriteria.annee();
        cycleCriteria.libelle();
        cycleCriteria.dateDebut();
        cycleCriteria.dateFin();
        cycleCriteria.cloture();
        cycleCriteria.commentaire();
        cycleCriteria.centreId();
        cycleCriteria.inscriptionId();
        cycleCriteria.evaluationId();
        cycleCriteria.habilitationId();
        cycleCriteria.distinct();
    }

    private static Condition<CycleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAnnee()) &&
                condition.apply(criteria.getLibelle()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getCloture()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getCentreId()) &&
                condition.apply(criteria.getInscriptionId()) &&
                condition.apply(criteria.getEvaluationId()) &&
                condition.apply(criteria.getHabilitationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CycleCriteria> copyFiltersAre(CycleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAnnee(), copy.getAnnee()) &&
                condition.apply(criteria.getLibelle(), copy.getLibelle()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getCloture(), copy.getCloture()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getCentreId(), copy.getCentreId()) &&
                condition.apply(criteria.getInscriptionId(), copy.getInscriptionId()) &&
                condition.apply(criteria.getEvaluationId(), copy.getEvaluationId()) &&
                condition.apply(criteria.getHabilitationId(), copy.getHabilitationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
