package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CoursCriteriaTest {

    @Test
    void newCoursCriteriaHasAllFiltersNullTest() {
        var coursCriteria = new CoursCriteria();
        assertThat(coursCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void coursCriteriaFluentMethodsCreatesFiltersTest() {
        var coursCriteria = new CoursCriteria();

        setAllFilters(coursCriteria);

        assertThat(coursCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void coursCriteriaCopyCreatesNullFilterTest() {
        var coursCriteria = new CoursCriteria();
        var copy = coursCriteria.copy();

        assertThat(coursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(coursCriteria)
        );
    }

    @Test
    void coursCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var coursCriteria = new CoursCriteria();
        setAllFilters(coursCriteria);

        var copy = coursCriteria.copy();

        assertThat(coursCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(coursCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var coursCriteria = new CoursCriteria();

        assertThat(coursCriteria).hasToString("CoursCriteria{}");
    }

    private static void setAllFilters(CoursCriteria coursCriteria) {
        coursCriteria.id();
        coursCriteria.intitule();
        coursCriteria.libelleLong();
        coursCriteria.libelleCourt();
        coursCriteria.ordreAffichage();
        coursCriteria.nbPeriodes();
        coursCriteria.coefficient();
        coursCriteria.dateDebut();
        coursCriteria.dateFin();
        coursCriteria.commentaire();
        coursCriteria.actif();
        coursCriteria.evaluationId();
        coursCriteria.distinct();
    }

    private static Condition<CoursCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIntitule()) &&
                condition.apply(criteria.getLibelleLong()) &&
                condition.apply(criteria.getLibelleCourt()) &&
                condition.apply(criteria.getOrdreAffichage()) &&
                condition.apply(criteria.getNbPeriodes()) &&
                condition.apply(criteria.getCoefficient()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateFin()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getEvaluationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CoursCriteria> copyFiltersAre(CoursCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIntitule(), copy.getIntitule()) &&
                condition.apply(criteria.getLibelleLong(), copy.getLibelleLong()) &&
                condition.apply(criteria.getLibelleCourt(), copy.getLibelleCourt()) &&
                condition.apply(criteria.getOrdreAffichage(), copy.getOrdreAffichage()) &&
                condition.apply(criteria.getNbPeriodes(), copy.getNbPeriodes()) &&
                condition.apply(criteria.getCoefficient(), copy.getCoefficient()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateFin(), copy.getDateFin()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getEvaluationId(), copy.getEvaluationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
