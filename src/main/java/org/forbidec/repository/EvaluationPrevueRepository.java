package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.EvaluationPrevue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EvaluationPrevue entity.
 */
@Repository
public interface EvaluationPrevueRepository extends JpaRepository<EvaluationPrevue, Long>, JpaSpecificationExecutor<EvaluationPrevue> {
    default Optional<EvaluationPrevue> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EvaluationPrevue> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EvaluationPrevue> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select evaluationPrevue from EvaluationPrevue evaluationPrevue left join fetch evaluationPrevue.cycle left join fetch evaluationPrevue.enseignant left join fetch evaluationPrevue.matiere left join fetch evaluationPrevue.sousMatiere left join fetch evaluationPrevue.cours left join fetch evaluationPrevue.typeTache",
        countQuery = "select count(evaluationPrevue) from EvaluationPrevue evaluationPrevue"
    )
    Page<EvaluationPrevue> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select evaluationPrevue from EvaluationPrevue evaluationPrevue left join fetch evaluationPrevue.cycle left join fetch evaluationPrevue.enseignant left join fetch evaluationPrevue.matiere left join fetch evaluationPrevue.sousMatiere left join fetch evaluationPrevue.cours left join fetch evaluationPrevue.typeTache"
    )
    List<EvaluationPrevue> findAllWithToOneRelationships();

    @Query(
        "select evaluationPrevue from EvaluationPrevue evaluationPrevue left join fetch evaluationPrevue.cycle left join fetch evaluationPrevue.enseignant left join fetch evaluationPrevue.matiere left join fetch evaluationPrevue.sousMatiere left join fetch evaluationPrevue.cours left join fetch evaluationPrevue.typeTache where evaluationPrevue.id =:id"
    )
    Optional<EvaluationPrevue> findOneWithToOneRelationships(@Param("id") Long id);
}
