package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EnseignantCriteriaTest {

    @Test
    void newEnseignantCriteriaHasAllFiltersNullTest() {
        var enseignantCriteria = new EnseignantCriteria();
        assertThat(enseignantCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void enseignantCriteriaFluentMethodsCreatesFiltersTest() {
        var enseignantCriteria = new EnseignantCriteria();

        setAllFilters(enseignantCriteria);

        assertThat(enseignantCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void enseignantCriteriaCopyCreatesNullFilterTest() {
        var enseignantCriteria = new EnseignantCriteria();
        var copy = enseignantCriteria.copy();

        assertThat(enseignantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(enseignantCriteria)
        );
    }

    @Test
    void enseignantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var enseignantCriteria = new EnseignantCriteria();
        setAllFilters(enseignantCriteria);

        var copy = enseignantCriteria.copy();

        assertThat(enseignantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(enseignantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var enseignantCriteria = new EnseignantCriteria();

        assertThat(enseignantCriteria).hasToString("EnseignantCriteria{}");
    }

    private static void setAllFilters(EnseignantCriteria enseignantCriteria) {
        enseignantCriteria.id();
        enseignantCriteria.nom();
        enseignantCriteria.prenom();
        enseignantCriteria.libelleLong();
        enseignantCriteria.libelleCourt();
        enseignantCriteria.email();
        enseignantCriteria.telephone();
        enseignantCriteria.keycloakUserId();
        enseignantCriteria.commentaire();
        enseignantCriteria.actif();
        enseignantCriteria.evaluationId();
        enseignantCriteria.distinct();
    }

    private static Condition<EnseignantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getLibelleLong()) &&
                condition.apply(criteria.getLibelleCourt()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getKeycloakUserId()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getEvaluationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EnseignantCriteria> copyFiltersAre(EnseignantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getLibelleLong(), copy.getLibelleLong()) &&
                condition.apply(criteria.getLibelleCourt(), copy.getLibelleCourt()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getKeycloakUserId(), copy.getKeycloakUserId()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getEvaluationId(), copy.getEvaluationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
