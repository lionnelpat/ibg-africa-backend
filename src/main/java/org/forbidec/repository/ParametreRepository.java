package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.Parametre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Parametre entity.
 */
@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Long> {
    default Optional<Parametre> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Parametre> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Parametre> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select parametre from Parametre parametre left join fetch parametre.centre",
        countQuery = "select count(parametre) from Parametre parametre"
    )
    Page<Parametre> findAllWithToOneRelationships(Pageable pageable);

    @Query("select parametre from Parametre parametre left join fetch parametre.centre")
    List<Parametre> findAllWithToOneRelationships();

    @Query("select parametre from Parametre parametre left join fetch parametre.centre where parametre.id =:id")
    Optional<Parametre> findOneWithToOneRelationships(@Param("id") Long id);
}
