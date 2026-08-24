package org.forbidec.repository.dashboard;

import java.math.BigDecimal;
import java.util.List;
import org.forbidec.domain.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Agrégats pour le dashboard global. Repository dédié (comme pour le
 * bulletin) plutôt qu'ajouté aux repositories générés par JHipster.
 */
@Repository
public interface DashboardQueryRepository extends JpaRepository<Etudiant, Long> {
    @Query("select count(e) from Etudiant e where e.actif = true")
    long countEtudiantsActifs();

    @Query("select count(e) from Etudiant e where e.cursusAcheve = true")
    long countFinissants();

    @Query(
        "select c.annee as annee, count(ic) as nombre from InscriptionCycle ic join ic.cycle c group by c.annee order by c.annee"
    )
    List<AnneeCountProjection> countInscriptionsByCycleAnnee();

    @Query(
        """
        select er.note
        from EvaluationRealisee er
        where er.compteDansMoyenne = true
            and er.evaluationPrevue.compteDansMoyenne = true
            and er.note is not null
            and er.statut in ('SAISIE', 'VALIDEE')
        """
    )
    List<BigDecimal> findNotesComptantDansMoyenne();

    @Query("select ic.cycle.id as cycleId, count(ic) as nombre from InscriptionCycle ic group by ic.cycle.id")
    List<CycleInscriptionCountProjection> countInscriptionsByCycle();
}
