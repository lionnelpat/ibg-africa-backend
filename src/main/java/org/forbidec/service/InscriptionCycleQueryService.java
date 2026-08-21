package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.repository.InscriptionCycleRepository;
import org.forbidec.service.criteria.InscriptionCycleCriteria;
import org.forbidec.service.dto.InscriptionCycleDTO;
import org.forbidec.service.mapper.InscriptionCycleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InscriptionCycle} entities in the database.
 * The main input is a {@link InscriptionCycleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InscriptionCycleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InscriptionCycleQueryService extends QueryService<InscriptionCycle> {

    private static final Logger LOG = LoggerFactory.getLogger(InscriptionCycleQueryService.class);

    private final InscriptionCycleRepository inscriptionCycleRepository;

    private final InscriptionCycleMapper inscriptionCycleMapper;

    public InscriptionCycleQueryService(
        InscriptionCycleRepository inscriptionCycleRepository,
        InscriptionCycleMapper inscriptionCycleMapper
    ) {
        this.inscriptionCycleRepository = inscriptionCycleRepository;
        this.inscriptionCycleMapper = inscriptionCycleMapper;
    }

    /**
     * Return a {@link Page} of {@link InscriptionCycleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InscriptionCycleDTO> findByCriteria(InscriptionCycleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InscriptionCycle> specification = createSpecification(criteria);
        return inscriptionCycleRepository.findAll(specification, page).map(inscriptionCycleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InscriptionCycleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InscriptionCycle> specification = createSpecification(criteria);
        return inscriptionCycleRepository.count(specification);
    }

    /**
     * Function to convert {@link InscriptionCycleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InscriptionCycle> createSpecification(InscriptionCycleCriteria criteria) {
        Specification<InscriptionCycle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InscriptionCycle_.id),
                buildRangeSpecification(criteria.getDateInscription(), InscriptionCycle_.dateInscription),
                buildSpecification(criteria.getCycleTermine(), InscriptionCycle_.cycleTermine),
                buildStringSpecification(criteria.getGroupe(), InscriptionCycle_.groupe),
                buildStringSpecification(criteria.getCommentaire1(), InscriptionCycle_.commentaire1),
                buildStringSpecification(criteria.getCommentaire2(), InscriptionCycle_.commentaire2),
                buildStringSpecification(criteria.getCommentaire3(), InscriptionCycle_.commentaire3),
                buildStringSpecification(criteria.getCommentaire5(), InscriptionCycle_.commentaire5),
                buildSpecification(criteria.getCycleId(), root -> root.join(InscriptionCycle_.cycle, JoinType.LEFT).get(Cycle_.id)),
                buildSpecification(criteria.getEtudiantId(), root -> root.join(InscriptionCycle_.etudiant, JoinType.LEFT).get(Etudiant_.id))
            );
        }
        return specification;
    }
}
