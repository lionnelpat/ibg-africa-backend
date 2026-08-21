package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.EvenementEtudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EvenementEtudiant entity.
 */
@Repository
public interface EvenementEtudiantRepository extends JpaRepository<EvenementEtudiant, Long> {
    default Optional<EvenementEtudiant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EvenementEtudiant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EvenementEtudiant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select evenementEtudiant from EvenementEtudiant evenementEtudiant left join fetch evenementEtudiant.etudiant",
        countQuery = "select count(evenementEtudiant) from EvenementEtudiant evenementEtudiant"
    )
    Page<EvenementEtudiant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select evenementEtudiant from EvenementEtudiant evenementEtudiant left join fetch evenementEtudiant.etudiant")
    List<EvenementEtudiant> findAllWithToOneRelationships();

    @Query(
        "select evenementEtudiant from EvenementEtudiant evenementEtudiant left join fetch evenementEtudiant.etudiant where evenementEtudiant.id =:id"
    )
    Optional<EvenementEtudiant> findOneWithToOneRelationships(@Param("id") Long id);
}
