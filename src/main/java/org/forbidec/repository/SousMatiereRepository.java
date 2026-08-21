package org.forbidec.repository;

import org.forbidec.domain.SousMatiere;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SousMatiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SousMatiereRepository extends JpaRepository<SousMatiere, Long> {}
