package org.forbidec.repository.cycle;

import java.util.List;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.service.dto.cycle.CycleInscriptionCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Comptage des inscriptions par cycle, pour l'affichage dans la liste des
 * cycles (une requête groupée par page plutôt qu'un N+1 par ligne).
 */
@Repository
public interface CycleInscriptionCountQueryRepository extends JpaRepository<InscriptionCycle, Long> {
    @Query(
        "select new org.forbidec.service.dto.cycle.CycleInscriptionCountDTO(ic.cycle.id, count(ic)) " +
        "from InscriptionCycle ic where ic.cycle.id in :cycleIds group by ic.cycle.id"
    )
    List<CycleInscriptionCountDTO> countByCycleIds(@Param("cycleIds") List<Long> cycleIds);
}
