package org.forbidec.service.mapper;

import org.forbidec.domain.SousMatiere;
import org.forbidec.service.dto.SousMatiereDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SousMatiere} and its DTO {@link SousMatiereDTO}.
 */
@Mapper(componentModel = "spring")
public interface SousMatiereMapper extends EntityMapper<SousMatiereDTO, SousMatiere> {}
