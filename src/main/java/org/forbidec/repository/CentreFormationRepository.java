package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.CentreFormation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CentreFormation entity.
 */
@Repository
public interface CentreFormationRepository extends JpaRepository<CentreFormation, Long> {
    default Optional<CentreFormation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CentreFormation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CentreFormation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select centreFormation from CentreFormation centreFormation left join fetch centreFormation.pays",
        countQuery = "select count(centreFormation) from CentreFormation centreFormation"
    )
    Page<CentreFormation> findAllWithToOneRelationships(Pageable pageable);

    @Query("select centreFormation from CentreFormation centreFormation left join fetch centreFormation.pays")
    List<CentreFormation> findAllWithToOneRelationships();

    @Query("select centreFormation from CentreFormation centreFormation left join fetch centreFormation.pays where centreFormation.id =:id")
    Optional<CentreFormation> findOneWithToOneRelationships(@Param("id") Long id);
}
