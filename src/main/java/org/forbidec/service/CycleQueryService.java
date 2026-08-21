package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.Cycle;
import org.forbidec.repository.CycleRepository;
import org.forbidec.service.criteria.CycleCriteria;
import org.forbidec.service.dto.CycleDTO;
import org.forbidec.service.mapper.CycleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cycle} entities in the database.
 * The main input is a {@link CycleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CycleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CycleQueryService extends QueryService<Cycle> {

    private static final Logger LOG = LoggerFactory.getLogger(CycleQueryService.class);

    private final CycleRepository cycleRepository;

    private final CycleMapper cycleMapper;

    public CycleQueryService(CycleRepository cycleRepository, CycleMapper cycleMapper) {
        this.cycleRepository = cycleRepository;
        this.cycleMapper = cycleMapper;
    }

    /**
     * Return a {@link Page} of {@link CycleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CycleDTO> findByCriteria(CycleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cycle> specification = createSpecification(criteria);
        return cycleRepository.findAll(specification, page).map(cycleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CycleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cycle> specification = createSpecification(criteria);
        return cycleRepository.count(specification);
    }

    /**
     * Function to convert {@link CycleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cycle> createSpecification(CycleCriteria criteria) {
        Specification<Cycle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Cycle_.id),
                buildRangeSpecification(criteria.getAnnee(), Cycle_.annee),
                buildStringSpecification(criteria.getLibelle(), Cycle_.libelle),
                buildRangeSpecification(criteria.getDateDebut(), Cycle_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), Cycle_.dateFin),
                buildSpecification(criteria.getCloture(), Cycle_.cloture),
                buildStringSpecification(criteria.getCommentaire(), Cycle_.commentaire),
                buildSpecification(criteria.getCentreId(), root -> root.join(Cycle_.centre, JoinType.LEFT).get(CentreFormation_.id)),
                buildSpecification(criteria.getInscriptionId(), root ->
                    root.join(Cycle_.inscriptions, JoinType.LEFT).get(InscriptionCycle_.id)
                ),
                buildSpecification(criteria.getEvaluationId(), root ->
                    root.join(Cycle_.evaluations, JoinType.LEFT).get(EvaluationPrevue_.id)
                ),
                buildSpecification(criteria.getHabilitationId(), root ->
                    root.join(Cycle_.habilitations, JoinType.LEFT).get(HabilitationCycle_.id)
                )
            );
        }
        return specification;
    }
}
