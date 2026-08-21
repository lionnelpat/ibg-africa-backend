package org.forbidec.repository;

import org.forbidec.domain.Cours;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cours entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoursRepository extends JpaRepository<Cours, Long>, JpaSpecificationExecutor<Cours> {}
