package org.forbidec.repository;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.BaremeMention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BaremeMention entity.
 */
@Repository
public interface BaremeMentionRepository extends JpaRepository<BaremeMention, Long> {
    default Optional<BaremeMention> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BaremeMention> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BaremeMention> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select baremeMention from BaremeMention baremeMention left join fetch baremeMention.centre",
        countQuery = "select count(baremeMention) from BaremeMention baremeMention"
    )
    Page<BaremeMention> findAllWithToOneRelationships(Pageable pageable);

    @Query("select baremeMention from BaremeMention baremeMention left join fetch baremeMention.centre")
    List<BaremeMention> findAllWithToOneRelationships();

    @Query("select baremeMention from BaremeMention baremeMention left join fetch baremeMention.centre where baremeMention.id =:id")
    Optional<BaremeMention> findOneWithToOneRelationships(@Param("id") Long id);
}
