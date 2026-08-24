package org.forbidec.repository.bulletin;

import java.util.List;
import org.forbidec.domain.EvaluationRealisee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Requêtes de lecture pour le bulletin d'un étudiant. Repository dédié
 * (plutôt que d'ajouter des méthodes à EvaluationRealiseeRepository, généré
 * par JHipster) pour ne rien risquer à une prochaine régénération d'entités.
 */
@Repository
public interface BulletinQueryRepository extends JpaRepository<EvaluationRealisee, Long> {
    @Query(
        """
        select
            c.id as cycleId,
            c.annee as cycleAnnee,
            co.id as coursId,
            co.intitule as coursIntitule,
            co.ordreAffichage as coursOrdreAffichage,
            ep.coefficient as coefficient,
            er.note as note
        from EvaluationRealisee er
            join er.evaluationPrevue ep
            join ep.cycle c
            join ep.cours co
        where er.etudiant.id = :etudiantId
            and er.compteDansMoyenne = true
            and ep.compteDansMoyenne = true
            and er.note is not null
            and er.statut in ('SAISIE', 'VALIDEE')
        """
    )
    List<EvaluationLigneProjection> findLignesForEtudiant(@Param("etudiantId") Long etudiantId);
}
