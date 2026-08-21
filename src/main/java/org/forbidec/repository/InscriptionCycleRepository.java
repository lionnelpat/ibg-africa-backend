package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.InscriptionCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InscriptionCycle entity.
 */
@Repository
public interface InscriptionCycleRepository extends JpaRepository<InscriptionCycle, Long>, JpaSpecificationExecutor<InscriptionCycle> {
    default Optional<InscriptionCycle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InscriptionCycle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InscriptionCycle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inscriptionCycle from InscriptionCycle inscriptionCycle left join fetch inscriptionCycle.cycle left join fetch inscriptionCycle.etudiant",
        countQuery = "select count(inscriptionCycle) from InscriptionCycle inscriptionCycle"
    )
    Page<InscriptionCycle> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select inscriptionCycle from InscriptionCycle inscriptionCycle left join fetch inscriptionCycle.cycle left join fetch inscriptionCycle.etudiant"
    )
    List<InscriptionCycle> findAllWithToOneRelationships();

    @Query(
        "select inscriptionCycle from InscriptionCycle inscriptionCycle left join fetch inscriptionCycle.cycle left join fetch inscriptionCycle.etudiant where inscriptionCycle.id =:id"
    )
    Optional<InscriptionCycle> findOneWithToOneRelationships(@Param("id") Long id);
}
