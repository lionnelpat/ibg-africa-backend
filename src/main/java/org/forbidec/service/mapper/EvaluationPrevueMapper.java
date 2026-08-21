package org.forbidec.service.mapper;

import org.forbidec.domain.Cours;
import org.forbidec.domain.Cycle;
import org.forbidec.domain.Enseignant;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.Matiere;
import org.forbidec.domain.SousMatiere;
import org.forbidec.domain.TypeTache;
import org.forbidec.service.dto.CoursDTO;
import org.forbidec.service.dto.CycleDTO;
import org.forbidec.service.dto.EnseignantDTO;
import org.forbidec.service.dto.EvaluationPrevueDTO;
import org.forbidec.service.dto.MatiereDTO;
import org.forbidec.service.dto.SousMatiereDTO;
import org.forbidec.service.dto.TypeTacheDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EvaluationPrevue} and its DTO {@link EvaluationPrevueDTO}.
 */
@Mapper(componentModel = "spring")
public interface EvaluationPrevueMapper extends EntityMapper<EvaluationPrevueDTO, EvaluationPrevue> {
    @Mapping(target = "cycle", source = "cycle", qualifiedByName = "cycleAnnee")
    @Mapping(target = "enseignant", source = "enseignant", qualifiedByName = "enseignantNom")
    @Mapping(target = "matiere", source = "matiere", qualifiedByName = "matiereIntitule")
    @Mapping(target = "sousMatiere", source = "sousMatiere", qualifiedByName = "sousMatiereIntitule")
    @Mapping(target = "cours", source = "cours", qualifiedByName = "coursIntitule")
    @Mapping(target = "typeTache", source = "typeTache", qualifiedByName = "typeTacheIntitule")
    EvaluationPrevueDTO toDto(EvaluationPrevue s);

    @Named("cycleAnnee")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "annee", source = "annee")
    CycleDTO toDtoCycleAnnee(Cycle cycle);

    @Named("enseignantNom")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    EnseignantDTO toDtoEnseignantNom(Enseignant enseignant);

    @Named("matiereIntitule")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "intitule", source = "intitule")
    MatiereDTO toDtoMatiereIntitule(Matiere matiere);

    @Named("sousMatiereIntitule")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "intitule", source = "intitule")
    SousMatiereDTO toDtoSousMatiereIntitule(SousMatiere sousMatiere);

    @Named("coursIntitule")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "intitule", source = "intitule")
    CoursDTO toDtoCoursIntitule(Cours cours);

    @Named("typeTacheIntitule")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "intitule", source = "intitule")
    TypeTacheDTO toDtoTypeTacheIntitule(TypeTache typeTache);
}
