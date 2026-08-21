package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InscriptionCycleCriteriaTest {

    @Test
    void newInscriptionCycleCriteriaHasAllFiltersNullTest() {
        var inscriptionCycleCriteria = new InscriptionCycleCriteria();
        assertThat(inscriptionCycleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inscriptionCycleCriteriaFluentMethodsCreatesFiltersTest() {
        var inscriptionCycleCriteria = new InscriptionCycleCriteria();

        setAllFilters(inscriptionCycleCriteria);

        assertThat(inscriptionCycleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inscriptionCycleCriteriaCopyCreatesNullFilterTest() {
        var inscriptionCycleCriteria = new InscriptionCycleCriteria();
        var copy = inscriptionCycleCriteria.copy();

        assertThat(inscriptionCycleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inscriptionCycleCriteria)
        );
    }

    @Test
    void inscriptionCycleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inscriptionCycleCriteria = new InscriptionCycleCriteria();
        setAllFilters(inscriptionCycleCriteria);

        var copy = inscriptionCycleCriteria.copy();

        assertThat(inscriptionCycleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inscriptionCycleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inscriptionCycleCriteria = new InscriptionCycleCriteria();

        assertThat(inscriptionCycleCriteria).hasToString("InscriptionCycleCriteria{}");
    }

    private static void setAllFilters(InscriptionCycleCriteria inscriptionCycleCriteria) {
        inscriptionCycleCriteria.id();
        inscriptionCycleCriteria.dateInscription();
        inscriptionCycleCriteria.cycleTermine();
        inscriptionCycleCriteria.groupe();
        inscriptionCycleCriteria.commentaire1();
        inscriptionCycleCriteria.commentaire2();
        inscriptionCycleCriteria.commentaire3();
        inscriptionCycleCriteria.commentaire5();
        inscriptionCycleCriteria.cycleId();
        inscriptionCycleCriteria.etudiantId();
        inscriptionCycleCriteria.distinct();
    }

    private static Condition<InscriptionCycleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateInscription()) &&
                condition.apply(criteria.getCycleTermine()) &&
                condition.apply(criteria.getGroupe()) &&
                condition.apply(criteria.getCommentaire1()) &&
                condition.apply(criteria.getCommentaire2()) &&
                condition.apply(criteria.getCommentaire3()) &&
                condition.apply(criteria.getCommentaire5()) &&
                condition.apply(criteria.getCycleId()) &&
                condition.apply(criteria.getEtudiantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InscriptionCycleCriteria> copyFiltersAre(
        InscriptionCycleCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateInscription(), copy.getDateInscription()) &&
                condition.apply(criteria.getCycleTermine(), copy.getCycleTermine()) &&
                condition.apply(criteria.getGroupe(), copy.getGroupe()) &&
                condition.apply(criteria.getCommentaire1(), copy.getCommentaire1()) &&
                condition.apply(criteria.getCommentaire2(), copy.getCommentaire2()) &&
                condition.apply(criteria.getCommentaire3(), copy.getCommentaire3()) &&
                condition.apply(criteria.getCommentaire5(), copy.getCommentaire5()) &&
                condition.apply(criteria.getCycleId(), copy.getCycleId()) &&
                condition.apply(criteria.getEtudiantId(), copy.getEtudiantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
