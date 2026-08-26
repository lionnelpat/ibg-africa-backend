package org.forbidec.repository.etudiant;

import java.util.List;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.service.dto.etudiant.EtudiantCycleCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Comptage des cycles distincts par étudiant, pour la colonne "Année" de
 * la liste des étudiants (une requête groupée par page plutôt qu'un N+1).
 */
@Repository
public interface EtudiantCycleCountQueryRepository extends JpaRepository<InscriptionCycle, Long> {
    @Query(
        "select new org.forbidec.service.dto.etudiant.EtudiantCycleCountDTO(ic.etudiant.id, count(distinct ic.cycle.id)) " +
        "from InscriptionCycle ic where ic.etudiant.id in :etudiantIds group by ic.etudiant.id"
    )
    List<EtudiantCycleCountDTO> countByEtudiantIds(@Param("etudiantIds") List<Long> etudiantIds);
}
