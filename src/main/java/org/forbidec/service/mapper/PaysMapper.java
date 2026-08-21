package org.forbidec.service.mapper;

import org.forbidec.domain.Pays;
import org.forbidec.service.dto.PaysDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pays} and its DTO {@link PaysDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaysMapper extends EntityMapper<PaysDTO, Pays> {}
