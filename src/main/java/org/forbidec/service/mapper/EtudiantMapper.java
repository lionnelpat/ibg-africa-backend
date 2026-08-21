package org.forbidec.service.mapper;

import org.forbidec.domain.Etudiant;
import org.forbidec.domain.Pays;
import org.forbidec.service.dto.EtudiantDTO;
import org.forbidec.service.dto.PaysDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Etudiant} and its DTO {@link EtudiantDTO}.
 */
@Mapper(componentModel = "spring")
public interface EtudiantMapper extends EntityMapper<EtudiantDTO, Etudiant> {
    @Mapping(target = "pays", source = "pays", qualifiedByName = "paysNom")
    EtudiantDTO toDto(Etudiant s);

    @Named("paysNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    PaysDTO toDtoPaysNom(Pays pays);
}
