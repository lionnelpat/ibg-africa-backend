package org.forbidec.repository;

import org.forbidec.domain.TypeTache;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeTache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeTacheRepository extends JpaRepository<TypeTache, Long> {}
