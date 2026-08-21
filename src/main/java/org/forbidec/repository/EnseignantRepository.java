package org.forbidec.repository;

import org.forbidec.domain.Enseignant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enseignant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long>, JpaSpecificationExecutor<Enseignant> {}
