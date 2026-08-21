package org.forbidec.service.mapper;

import org.forbidec.domain.Etudiant;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.service.dto.EtudiantDTO;
import org.forbidec.service.dto.EvaluationPrevueDTO;
import org.forbidec.service.dto.EvaluationRealiseeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EvaluationRealisee} and its DTO {@link EvaluationRealiseeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvaluationRealiseeMapper extends EntityMapper<EvaluationRealiseeDTO, EvaluationRealisee> {
    @Mapping(target = "evaluationPrevue", source = "evaluationPrevue", qualifiedByName = "evaluationPrevueIntitule")
    @Mapping(target = "etudiant", source = "etudiant", qualifiedByName = "etudiantNom")
    EvaluationRealiseeDTO toDto(EvaluationRealisee s);

    @Named("evaluationPrevueIntitule")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "intitule", source = "intitule")
    EvaluationPrevueDTO toDtoEvaluationPrevueIntitule(EvaluationPrevue evaluationPrevue);

    @Named("etudiantNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    EtudiantDTO toDtoEtudiantNom(Etudiant etudiant);
}
