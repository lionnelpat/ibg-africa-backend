package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.repository.EvaluationRealiseeRepository;
import org.forbidec.service.criteria.EvaluationRealiseeCriteria;
import org.forbidec.service.dto.EvaluationRealiseeDTO;
import org.forbidec.service.mapper.EvaluationRealiseeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EvaluationRealisee} entities in the database.
 * The main input is a {@link EvaluationRealiseeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EvaluationRealiseeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvaluationRealiseeQueryService extends QueryService<EvaluationRealisee> {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationRealiseeQueryService.class);

    private final EvaluationRealiseeRepository evaluationRealiseeRepository;

    private final EvaluationRealiseeMapper evaluationRealiseeMapper;

    public EvaluationRealiseeQueryService(
        EvaluationRealiseeRepository evaluationRealiseeRepository,
        EvaluationRealiseeMapper evaluationRealiseeMapper
    ) {
        this.evaluationRealiseeRepository = evaluationRealiseeRepository;
        this.evaluationRealiseeMapper = evaluationRealiseeMapper;
    }

    /**
     * Return a {@link Page} of {@link EvaluationRealiseeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvaluationRealiseeDTO> findByCriteria(EvaluationRealiseeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EvaluationRealisee> specification = createSpecification(criteria);
        return evaluationRealiseeRepository.findAll(specification, page).map(evaluationRealiseeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvaluationRealiseeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EvaluationRealisee> specification = createSpecification(criteria);
        return evaluationRealiseeRepository.count(specification);
    }

    /**
     * Function to convert {@link EvaluationRealiseeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EvaluationRealisee> createSpecification(EvaluationRealiseeCriteria criteria) {
        Specification<EvaluationRealisee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EvaluationRealisee_.id),
                buildRangeSpecification(criteria.getNote(), EvaluationRealisee_.note),
                buildSpecification(criteria.getStatut(), EvaluationRealisee_.statut),
                buildSpecification(criteria.getCompteDansMoyenne(), EvaluationRealisee_.compteDansMoyenne),
                buildRangeSpecification(criteria.getDateDebut(), EvaluationRealisee_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), EvaluationRealisee_.dateFin),
                buildStringSpecification(criteria.getCommentaire1(), EvaluationRealisee_.commentaire1),
                buildStringSpecification(criteria.getCommentaire2(), EvaluationRealisee_.commentaire2),
                buildStringSpecification(criteria.getCommentaire3(), EvaluationRealisee_.commentaire3),
                buildStringSpecification(criteria.getSaisiePar(), EvaluationRealisee_.saisiePar),
                buildRangeSpecification(criteria.getSaisieLe(), EvaluationRealisee_.saisieLe),
                buildStringSpecification(criteria.getValideePar(), EvaluationRealisee_.valideePar),
                buildRangeSpecification(criteria.getValideeLe(), EvaluationRealisee_.valideeLe),
                buildSpecification(criteria.getEvaluationPrevueId(), root ->
                    root.join(EvaluationRealisee_.evaluationPrevue, JoinType.LEFT).get(EvaluationPrevue_.id)
                ),
                buildSpecification(criteria.getEtudiantId(), root ->
                    root.join(EvaluationRealisee_.etudiant, JoinType.LEFT).get(Etudiant_.id)
                ),
                buildSpecification(criteria.getHistoriqueId(), root ->
                    root.join(EvaluationRealisee_.historiques, JoinType.LEFT).get(HistoriqueNote_.id)
                )
            );
        }
        return specification;
    }
}
