package org.forbidec.service.mapper;

import org.forbidec.domain.CentreFormation;
import org.forbidec.domain.Cycle;
import org.forbidec.service.dto.CentreFormationDTO;
import org.forbidec.service.dto.CycleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cycle} and its DTO {@link CycleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CycleMapper extends EntityMapper<CycleDTO, Cycle> {
    @Mapping(target = "centre", source = "centre", qualifiedByName = "centreFormationCode")
    CycleDTO toDto(Cycle s);

    @Named("centreFormationCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CentreFormationDTO toDtoCentreFormationCode(CentreFormation centreFormation);
}
