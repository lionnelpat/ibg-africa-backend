package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.repository.EvaluationPrevueRepository;
import org.forbidec.service.criteria.EvaluationPrevueCriteria;
import org.forbidec.service.dto.EvaluationPrevueDTO;
import org.forbidec.service.mapper.EvaluationPrevueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EvaluationPrevue} entities in the database.
 * The main input is a {@link EvaluationPrevueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EvaluationPrevueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvaluationPrevueQueryService extends QueryService<EvaluationPrevue> {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationPrevueQueryService.class);

    private final EvaluationPrevueRepository evaluationPrevueRepository;

    private final EvaluationPrevueMapper evaluationPrevueMapper;

    public EvaluationPrevueQueryService(
        EvaluationPrevueRepository evaluationPrevueRepository,
        EvaluationPrevueMapper evaluationPrevueMapper
    ) {
        this.evaluationPrevueRepository = evaluationPrevueRepository;
        this.evaluationPrevueMapper = evaluationPrevueMapper;
    }

    /**
     * Return a {@link Page} of {@link EvaluationPrevueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvaluationPrevueDTO> findByCriteria(EvaluationPrevueCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EvaluationPrevue> specification = createSpecification(criteria);
        return evaluationPrevueRepository.findAll(specification, page).map(evaluationPrevueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvaluationPrevueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EvaluationPrevue> specification = createSpecification(criteria);
        return evaluationPrevueRepository.count(specification);
    }

    /**
     * Function to convert {@link EvaluationPrevueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EvaluationPrevue> createSpecification(EvaluationPrevueCriteria criteria) {
        Specification<EvaluationPrevue> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EvaluationPrevue_.id),
                buildStringSpecification(criteria.getIntitule(), EvaluationPrevue_.intitule),
                buildStringSpecification(criteria.getLibelleImpression(), EvaluationPrevue_.libelleImpression),
                buildRangeSpecification(criteria.getCoefficient(), EvaluationPrevue_.coefficient),
                buildSpecification(criteria.getCompteDansMoyenne(), EvaluationPrevue_.compteDansMoyenne),
                buildRangeSpecification(criteria.getNoteMaximale(), EvaluationPrevue_.noteMaximale),
                buildRangeSpecification(criteria.getDateDebut(), EvaluationPrevue_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), EvaluationPrevue_.dateFin),
                buildStringSpecification(criteria.getCommentaire(), EvaluationPrevue_.commentaire),
                buildSpecification(criteria.getCycleId(), root -> root.join(EvaluationPrevue_.cycle, JoinType.LEFT).get(Cycle_.id)),
                buildSpecification(criteria.getEnseignantId(), root ->
                    root.join(EvaluationPrevue_.enseignant, JoinType.LEFT).get(Enseignant_.id)
                ),
                buildSpecification(criteria.getMatiereId(), root -> root.join(EvaluationPrevue_.matiere, JoinType.LEFT).get(Matiere_.id)),
                buildSpecification(criteria.getSousMatiereId(), root ->
                    root.join(EvaluationPrevue_.sousMatiere, JoinType.LEFT).get(SousMatiere_.id)
                ),
                buildSpecification(criteria.getCoursId(), root -> root.join(EvaluationPrevue_.cours, JoinType.LEFT).get(Cours_.id)),
                buildSpecification(criteria.getTypeTacheId(), root ->
                    root.join(EvaluationPrevue_.typeTache, JoinType.LEFT).get(TypeTache_.id)
                ),
                buildSpecification(criteria.getNoteId(), root ->
                    root.join(EvaluationPrevue_.notes, JoinType.LEFT).get(EvaluationRealisee_.id)
                )
            );
        }
        return specification;
    }
}
