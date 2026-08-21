package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.Cours;
import org.forbidec.repository.CoursRepository;
import org.forbidec.service.criteria.CoursCriteria;
import org.forbidec.service.dto.CoursDTO;
import org.forbidec.service.mapper.CoursMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cours} entities in the database.
 * The main input is a {@link CoursCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CoursDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CoursQueryService extends QueryService<Cours> {

    private static final Logger LOG = LoggerFactory.getLogger(CoursQueryService.class);

    private final CoursRepository coursRepository;

    private final CoursMapper coursMapper;

    public CoursQueryService(CoursRepository coursRepository, CoursMapper coursMapper) {
        this.coursRepository = coursRepository;
        this.coursMapper = coursMapper;
    }

    /**
     * Return a {@link Page} of {@link CoursDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CoursDTO> findByCriteria(CoursCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cours> specification = createSpecification(criteria);
        return coursRepository.findAll(specification, page).map(coursMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CoursCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cours> specification = createSpecification(criteria);
        return coursRepository.count(specification);
    }

    /**
     * Function to convert {@link CoursCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cours> createSpecification(CoursCriteria criteria) {
        Specification<Cours> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Cours_.id),
                buildStringSpecification(criteria.getIntitule(), Cours_.intitule),
                buildStringSpecification(criteria.getLibelleLong(), Cours_.libelleLong),
                buildStringSpecification(criteria.getLibelleCourt(), Cours_.libelleCourt),
                buildRangeSpecification(criteria.getOrdreAffichage(), Cours_.ordreAffichage),
                buildRangeSpecification(criteria.getNbPeriodes(), Cours_.nbPeriodes),
                buildRangeSpecification(criteria.getCoefficient(), Cours_.coefficient),
                buildRangeSpecification(criteria.getDateDebut(), Cours_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), Cours_.dateFin),
                buildStringSpecification(criteria.getCommentaire(), Cours_.commentaire),
                buildSpecification(criteria.getActif(), Cours_.actif),
                buildSpecification(criteria.getEvaluationId(), root ->
                    root.join(Cours_.evaluations, JoinType.LEFT).get(EvaluationPrevue_.id)
                )
            );
        }
        return specification;
    }
}
