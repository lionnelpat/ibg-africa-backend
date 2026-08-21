package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.HistoriqueNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoriqueNote entity.
 */
@Repository
public interface HistoriqueNoteRepository extends JpaRepository<HistoriqueNote, Long>, JpaSpecificationExecutor<HistoriqueNote> {
    default Optional<HistoriqueNote> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HistoriqueNote> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HistoriqueNote> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select historiqueNote from HistoriqueNote historiqueNote left join fetch historiqueNote.evaluationRealisee",
        countQuery = "select count(historiqueNote) from HistoriqueNote historiqueNote"
    )
    Page<HistoriqueNote> findAllWithToOneRelationships(Pageable pageable);

    @Query("select historiqueNote from HistoriqueNote historiqueNote left join fetch historiqueNote.evaluationRealisee")
    List<HistoriqueNote> findAllWithToOneRelationships();

    @Query(
        "select historiqueNote from HistoriqueNote historiqueNote left join fetch historiqueNote.evaluationRealisee where historiqueNote.id =:id"
    )
    Optional<HistoriqueNote> findOneWithToOneRelationships(@Param("id") Long id);
}
