package org.forbidec.service.mapper;

import org.forbidec.domain.BaremeMention;
import org.forbidec.domain.CentreFormation;
import org.forbidec.service.dto.BaremeMentionDTO;
import org.forbidec.service.dto.CentreFormationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaremeMention} and its DTO {@link BaremeMentionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BaremeMentionMapper extends EntityMapper<BaremeMentionDTO, BaremeMention> {
    @Mapping(target = "centre", source = "centre", qualifiedByName = "centreFormationCode")
    BaremeMentionDTO toDto(BaremeMention s);

    @Named("centreFormationCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    CentreFormationDTO toDtoCentreFormationCode(CentreFormation centreFormation);
}
