package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.EvaluationRealisee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EvaluationRealisee entity.
 */
@Repository
public interface EvaluationRealiseeRepository
    extends JpaRepository<EvaluationRealisee, Long>, JpaSpecificationExecutor<EvaluationRealisee> {
    default Optional<EvaluationRealisee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EvaluationRealisee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EvaluationRealisee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select evaluationRealisee from EvaluationRealisee evaluationRealisee left join fetch evaluationRealisee.evaluationPrevue left join fetch evaluationRealisee.etudiant",
        countQuery = "select count(evaluationRealisee) from EvaluationRealisee evaluationRealisee"
    )
    Page<EvaluationRealisee> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select evaluationRealisee from EvaluationRealisee evaluationRealisee left join fetch evaluationRealisee.evaluationPrevue left join fetch evaluationRealisee.etudiant"
    )
    List<EvaluationRealisee> findAllWithToOneRelationships();

    @Query(
        "select evaluationRealisee from EvaluationRealisee evaluationRealisee left join fetch evaluationRealisee.evaluationPrevue left join fetch evaluationRealisee.etudiant where evaluationRealisee.id =:id"
    )
    Optional<EvaluationRealisee> findOneWithToOneRelationships(@Param("id") Long id);
}
