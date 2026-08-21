package org.forbidec.service.mapper;

import org.forbidec.domain.Enseignant;
import org.forbidec.service.dto.EnseignantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enseignant} and its DTO {@link EnseignantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnseignantMapper extends EntityMapper<EnseignantDTO, Enseignant> {}
