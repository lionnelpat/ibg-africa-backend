package org.forbidec.service.mapper;

import org.forbidec.domain.CentreFormation;
import org.forbidec.domain.Pays;
import org.forbidec.service.dto.CentreFormationDTO;
import org.forbidec.service.dto.PaysDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CentreFormation} and its DTO {@link CentreFormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface CentreFormationMapper extends EntityMapper<CentreFormationDTO, CentreFormation> {
    @Mapping(target = "pays", source = "pays", qualifiedByName = "paysNom")
    CentreFormationDTO toDto(CentreFormation s);

    @Named("paysNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    PaysDTO toDtoPaysNom(Pays pays);
}
