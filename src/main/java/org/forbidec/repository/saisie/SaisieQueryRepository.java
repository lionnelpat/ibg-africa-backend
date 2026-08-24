package org.forbidec.repository.saisie;

import java.util.List;
import org.forbidec.domain.EvaluationRealisee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Requêtes de lecture pour la saisie des notes (grille cycle × matière ×
 * étudiants inscrits). Dédié, comme bulletin/ et dashboard/, plutôt que
 * d'ajouter des méthodes aux repositories générés par JHipster.
 */
@Repository
public interface SaisieQueryRepository extends JpaRepository<EvaluationRealisee, Long> {
    @Query("select er from EvaluationRealisee er where er.evaluationPrevue.id = :evaluationPrevueId")
    List<EvaluationRealisee> findByEvaluationPrevueId(@Param("evaluationPrevueId") Long evaluationPrevueId);

    @Query("select er from EvaluationRealisee er where er.evaluationPrevue.id = :evaluationPrevueId and er.etudiant.id = :etudiantId")
    java.util.Optional<EvaluationRealisee> findOne(@Param("evaluationPrevueId") Long evaluationPrevueId, @Param("etudiantId") Long etudiantId);
}
