package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.HistoriqueNote;
import org.forbidec.repository.HistoriqueNoteRepository;
import org.forbidec.service.criteria.HistoriqueNoteCriteria;
import org.forbidec.service.dto.HistoriqueNoteDTO;
import org.forbidec.service.mapper.HistoriqueNoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HistoriqueNote} entities in the database.
 * The main input is a {@link HistoriqueNoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HistoriqueNoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueNoteQueryService extends QueryService<HistoriqueNote> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueNoteQueryService.class);

    private final HistoriqueNoteRepository historiqueNoteRepository;

    private final HistoriqueNoteMapper historiqueNoteMapper;

    public HistoriqueNoteQueryService(HistoriqueNoteRepository historiqueNoteRepository, HistoriqueNoteMapper historiqueNoteMapper) {
        this.historiqueNoteRepository = historiqueNoteRepository;
        this.historiqueNoteMapper = historiqueNoteMapper;
    }

    /**
     * Return a {@link Page} of {@link HistoriqueNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueNoteDTO> findByCriteria(HistoriqueNoteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueNote> specification = createSpecification(criteria);
        return historiqueNoteRepository.findAll(specification, page).map(historiqueNoteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueNoteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueNote> specification = createSpecification(criteria);
        return historiqueNoteRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueNoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueNote> createSpecification(HistoriqueNoteCriteria criteria) {
        Specification<HistoriqueNote> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), HistoriqueNote_.id),
                buildRangeSpecification(criteria.getNoteAvant(), HistoriqueNote_.noteAvant),
                buildRangeSpecification(criteria.getNoteApres(), HistoriqueNote_.noteApres),
                buildSpecification(criteria.getStatutAvant(), HistoriqueNote_.statutAvant),
                buildSpecification(criteria.getStatutApres(), HistoriqueNote_.statutApres),
                buildStringSpecification(criteria.getMotif(), HistoriqueNote_.motif),
                buildStringSpecification(criteria.getModifiePar(), HistoriqueNote_.modifiePar),
                buildRangeSpecification(criteria.getModifieLe(), HistoriqueNote_.modifieLe),
                buildSpecification(criteria.getEvaluationRealiseeId(), root ->
                    root.join(HistoriqueNote_.evaluationRealisee, JoinType.LEFT).get(EvaluationRealisee_.id)
                )
            );
        }
        return specification;
    }
}
