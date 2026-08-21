package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.Enseignant;
import org.forbidec.repository.EnseignantRepository;
import org.forbidec.service.criteria.EnseignantCriteria;
import org.forbidec.service.dto.EnseignantDTO;
import org.forbidec.service.mapper.EnseignantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Enseignant} entities in the database.
 * The main input is a {@link EnseignantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EnseignantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnseignantQueryService extends QueryService<Enseignant> {

    private static final Logger LOG = LoggerFactory.getLogger(EnseignantQueryService.class);

    private final EnseignantRepository enseignantRepository;

    private final EnseignantMapper enseignantMapper;

    public EnseignantQueryService(EnseignantRepository enseignantRepository, EnseignantMapper enseignantMapper) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantMapper = enseignantMapper;
    }

    /**
     * Return a {@link Page} of {@link EnseignantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnseignantDTO> findByCriteria(EnseignantCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.findAll(specification, page).map(enseignantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnseignantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Enseignant> specification = createSpecification(criteria);
        return enseignantRepository.count(specification);
    }

    /**
     * Function to convert {@link EnseignantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Enseignant> createSpecification(EnseignantCriteria criteria) {
        Specification<Enseignant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Enseignant_.id),
                buildStringSpecification(criteria.getNom(), Enseignant_.nom),
                buildStringSpecification(criteria.getPrenom(), Enseignant_.prenom),
                buildStringSpecification(criteria.getLibelleLong(), Enseignant_.libelleLong),
                buildStringSpecification(criteria.getLibelleCourt(), Enseignant_.libelleCourt),
                buildStringSpecification(criteria.getEmail(), Enseignant_.email),
                buildStringSpecification(criteria.getTelephone(), Enseignant_.telephone),
                buildStringSpecification(criteria.getKeycloakUserId(), Enseignant_.keycloakUserId),
                buildStringSpecification(criteria.getCommentaire(), Enseignant_.commentaire),
                buildSpecification(criteria.getActif(), Enseignant_.actif),
                buildSpecification(criteria.getEvaluationId(), root ->
                    root.join(Enseignant_.evaluations, JoinType.LEFT).get(EvaluationPrevue_.id)
                )
            );
        }
        return specification;
    }
}
