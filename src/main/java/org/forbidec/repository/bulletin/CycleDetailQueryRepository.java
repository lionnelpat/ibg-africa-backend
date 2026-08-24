package org.forbidec.repository.bulletin;

import java.util.List;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.InscriptionCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Requêtes de lecture pour la vue détaillée d'un cycle (matières dispensées,
 * étudiants inscrits). Dédié plutôt qu'ajouté aux repositories générés par
 * JHipster, pour ne rien risquer à une prochaine régénération d'entités.
 */
@Repository
public interface CycleDetailQueryRepository extends JpaRepository<EvaluationPrevue, Long> {
    @Query(
        """
        select distinct ep from EvaluationPrevue ep
            left join fetch ep.cours
            left join fetch ep.matiere
            left join fetch ep.sousMatiere
            left join fetch ep.enseignant
        where ep.cycle.id = :cycleId
        order by ep.cours.ordreAffichage
        """
    )
    List<EvaluationPrevue> findMatieresDispensees(@Param("cycleId") Long cycleId);

    @Query(
        """
        select ic from InscriptionCycle ic
            left join fetch ic.etudiant
        where ic.cycle.id = :cycleId
        order by ic.etudiant.nom, ic.etudiant.prenom
        """
    )
    List<InscriptionCycle> findInscriptionsForCycle(@Param("cycleId") Long cycleId);
}
