package org.forbidec.repository.enseignant;

import java.util.List;
import org.forbidec.domain.EvaluationPrevue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Requêtes de lecture pour la fiche détail d'un enseignant : les matières
 * qu'il dispense, groupées par cycle.
 */
@Repository
public interface EnseignantDetailQueryRepository extends JpaRepository<EvaluationPrevue, Long> {
    @Query(
        """
        select ep from EvaluationPrevue ep
            join fetch ep.cycle c
            join fetch ep.cours
            left join fetch ep.matiere
            left join fetch ep.sousMatiere
        where ep.enseignant.id = :enseignantId
        order by c.annee desc, ep.cours.intitule
        """
    )
    List<EvaluationPrevue> findByEnseignantId(@Param("enseignantId") Long enseignantId);
}
