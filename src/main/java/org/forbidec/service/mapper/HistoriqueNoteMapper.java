package org.forbidec.service.mapper;

import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.domain.HistoriqueNote;
import org.forbidec.service.dto.EvaluationRealiseeDTO;
import org.forbidec.service.dto.HistoriqueNoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoriqueNote} and its DTO {@link HistoriqueNoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueNoteMapper extends EntityMapper<HistoriqueNoteDTO, HistoriqueNote> {
    @Mapping(target = "evaluationRealisee", source = "evaluationRealisee", qualifiedByName = "evaluationRealiseeStatut")
    HistoriqueNoteDTO toDto(HistoriqueNote s);

    @Named("evaluationRealiseeStatut")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statut", source = "statut")
    EvaluationRealiseeDTO toDtoEvaluationRealiseeStatut(EvaluationRealisee evaluationRealisee);
}
