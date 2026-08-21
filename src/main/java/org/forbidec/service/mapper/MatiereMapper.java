package org.forbidec.service.mapper;

import org.forbidec.domain.Matiere;
import org.forbidec.service.dto.MatiereDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Matiere} and its DTO {@link MatiereDTO}.
 */
@Mapper(componentModel = "spring")
public interface MatiereMapper extends EntityMapper<MatiereDTO, Matiere> {}
