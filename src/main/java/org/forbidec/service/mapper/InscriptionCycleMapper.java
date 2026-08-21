package org.forbidec.service.mapper;

import org.forbidec.domain.Cycle;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.service.dto.CycleDTO;
import org.forbidec.service.dto.EtudiantDTO;
import org.forbidec.service.dto.InscriptionCycleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InscriptionCycle} and its DTO {@link InscriptionCycleDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionCycleMapper extends EntityMapper<InscriptionCycleDTO, InscriptionCycle> {
    @Mapping(target = "cycle", source = "cycle", qualifiedByName = "cycleAnnee")
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantNom")
    InscriptionCycleDTO toDto(InscriptionCycle s);

    @Named("cycleAnnee")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "annee", source = "annee")
    CycleDTO toDtoCycleAnnee(Cycle cycle);

    @Named("etudiantNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    EtudiantDTO toDtoEtudiantNom(Etudiant etudiant);
}
