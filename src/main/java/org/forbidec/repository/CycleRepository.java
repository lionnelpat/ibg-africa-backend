package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.Cycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cycle entity.
 */
@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long>, JpaSpecificationExecutor<Cycle> {
    default Optional<Cycle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cycle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cycle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select cycle from Cycle cycle left join fetch cycle.centre", countQuery = "select count(cycle) from Cycle cycle")
    Page<Cycle> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cycle from Cycle cycle left join fetch cycle.centre")
    List<Cycle> findAllWithToOneRelationships();

    @Query("select cycle from Cycle cycle left join fetch cycle.centre where cycle.id =:id")
    Optional<Cycle> findOneWithToOneRelationships(@Param("id") Long id);
}
