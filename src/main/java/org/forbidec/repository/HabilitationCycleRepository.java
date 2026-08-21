package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.HabilitationCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HabilitationCycle entity.
 */
@Repository
public interface HabilitationCycleRepository extends JpaRepository<HabilitationCycle, Long> {
    default Optional<HabilitationCycle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HabilitationCycle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HabilitationCycle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select habilitationCycle from HabilitationCycle habilitationCycle left join fetch habilitationCycle.centre left join fetch habilitationCycle.cycle",
        countQuery = "select count(habilitationCycle) from HabilitationCycle habilitationCycle"
    )
    Page<HabilitationCycle> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select habilitationCycle from HabilitationCycle habilitationCycle left join fetch habilitationCycle.centre left join fetch habilitationCycle.cycle"
    )
    List<HabilitationCycle> findAllWithToOneRelationships();

    @Query(
        "select habilitationCycle from HabilitationCycle habilitationCycle left join fetch habilitationCycle.centre left join fetch habilitationCycle.cycle where habilitationCycle.id =:id"
    )
    Optional<HabilitationCycle> findOneWithToOneRelationships(@Param("id") Long id);
}
