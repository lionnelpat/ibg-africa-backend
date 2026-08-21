package org.forbidec.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EtudiantCriteriaTest {

    @Test
    void newEtudiantCriteriaHasAllFiltersNullTest() {
        var etudiantCriteria = new EtudiantCriteria();
        assertThat(etudiantCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void etudiantCriteriaFluentMethodsCreatesFiltersTest() {
        var etudiantCriteria = new EtudiantCriteria();

        setAllFilters(etudiantCriteria);

        assertThat(etudiantCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void etudiantCriteriaCopyCreatesNullFilterTest() {
        var etudiantCriteria = new EtudiantCriteria();
        var copy = etudiantCriteria.copy();

        assertThat(etudiantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(etudiantCriteria)
        );
    }

    @Test
    void etudiantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var etudiantCriteria = new EtudiantCriteria();
        setAllFilters(etudiantCriteria);

        var copy = etudiantCriteria.copy();

        assertThat(etudiantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(etudiantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var etudiantCriteria = new EtudiantCriteria();

        assertThat(etudiantCriteria).hasToString("EtudiantCriteria{}");
    }

    private static void setAllFilters(EtudiantCriteria etudiantCriteria) {
        etudiantCriteria.id();
        etudiantCriteria.matricule();
        etudiantCriteria.nom();
        etudiantCriteria.prenom();
        etudiantCriteria.particularite();
        etudiantCriteria.dateNaissance();
        etudiantCriteria.email();
        etudiantCriteria.telephone();
        etudiantCriteria.anneeEntree();
        etudiantCriteria.cursusAcheve();
        etudiantCriteria.anneeFinale();
        etudiantCriteria.keycloakUserId();
        etudiantCriteria.commentaire();
        etudiantCriteria.actif();
        etudiantCriteria.paysId();
        etudiantCriteria.inscriptionId();
        etudiantCriteria.evenementId();
        etudiantCriteria.noteId();
        etudiantCriteria.distinct();
    }

    private static Condition<EtudiantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMatricule()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getPrenom()) &&
                condition.apply(criteria.getParticularite()) &&
                condition.apply(criteria.getDateNaissance()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getTelephone()) &&
                condition.apply(criteria.getAnneeEntree()) &&
                condition.apply(criteria.getCursusAcheve()) &&
                condition.apply(criteria.getAnneeFinale()) &&
                condition.apply(criteria.getKeycloakUserId()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getActif()) &&
                condition.apply(criteria.getPaysId()) &&
                condition.apply(criteria.getInscriptionId()) &&
                condition.apply(criteria.getEvenementId()) &&
                condition.apply(criteria.getNoteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EtudiantCriteria> copyFiltersAre(EtudiantCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMatricule(), copy.getMatricule()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getPrenom(), copy.getPrenom()) &&
                condition.apply(criteria.getParticularite(), copy.getParticularite()) &&
                condition.apply(criteria.getDateNaissance(), copy.getDateNaissance()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getTelephone(), copy.getTelephone()) &&
                condition.apply(criteria.getAnneeEntree(), copy.getAnneeEntree()) &&
                condition.apply(criteria.getCursusAcheve(), copy.getCursusAcheve()) &&
                condition.apply(criteria.getAnneeFinale(), copy.getAnneeFinale()) &&
                condition.apply(criteria.getKeycloakUserId(), copy.getKeycloakUserId()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getActif(), copy.getActif()) &&
                condition.apply(criteria.getPaysId(), copy.getPaysId()) &&
                condition.apply(criteria.getInscriptionId(), copy.getInscriptionId()) &&
                condition.apply(criteria.getEvenementId(), copy.getEvenementId()) &&
                condition.apply(criteria.getNoteId(), copy.getNoteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
