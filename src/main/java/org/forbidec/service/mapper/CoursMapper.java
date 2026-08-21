package org.forbidec.service.mapper;

import org.forbidec.domain.Cours;
import org.forbidec.service.dto.CoursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cours} and its DTO {@link CoursDTO}.
 */
@Mapper(componentModel = "spring")
public interface CoursMapper extends EntityMapper<CoursDTO, Cours> {}
