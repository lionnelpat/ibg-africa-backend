package org.forbidec.service.mapper;

import org.forbidec.domain.TypeTache;
import org.forbidec.service.dto.TypeTacheDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeTache} and its DTO {@link TypeTacheDTO}.
 */
@Mapper(componentModel = "spring")
public interface TypeTacheMapper extends EntityMapper<TypeTacheDTO, TypeTache> {}
