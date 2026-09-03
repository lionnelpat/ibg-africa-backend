package org.forbidec.service;

import jakarta.persistence.criteria.JoinType;
import org.forbidec.domain.*; // for static metamodels
import org.forbidec.domain.Etudiant;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.criteria.EtudiantCriteria;
import org.forbidec.service.dto.EtudiantDTO;
import org.forbidec.service.mapper.EtudiantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Etudiant} entities in the database.
 * The main input is a {@link EtudiantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EtudiantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EtudiantQueryService extends QueryService<Etudiant> {

    private static final Logger LOG = LoggerFactory.getLogger(EtudiantQueryService.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    private final PaysContextService paysContextService;

    public EtudiantQueryService(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper, PaysContextService paysContextService) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
        this.paysContextService = paysContextService;
    }

    /**
     * Return a {@link Page} of {@link EtudiantDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EtudiantDTO> findByCriteria(EtudiantCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        paysContextService.enableFilterForCurrentRequest();
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.findAll(specification, page).map(etudiantMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EtudiantCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        paysContextService.enableFilterForCurrentRequest();
        final Specification<Etudiant> specification = createSpecification(criteria);
        return etudiantRepository.count(specification);
    }

    /**
     * Function to convert {@link EtudiantCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Etudiant> createSpecification(EtudiantCriteria criteria) {
        Specification<Etudiant> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Etudiant_.id),
                buildStringSpecification(criteria.getMatricule(), Etudiant_.matricule),
                buildStringSpecification(criteria.getNom(), Etudiant_.nom),
                buildStringSpecification(criteria.getPrenom(), Etudiant_.prenom),
                buildStringSpecification(criteria.getParticularite(), Etudiant_.particularite),
                buildRangeSpecification(criteria.getDateNaissance(), Etudiant_.dateNaissance),
                buildStringSpecification(criteria.getEmail(), Etudiant_.email),
                buildStringSpecification(criteria.getTelephone(), Etudiant_.telephone),
                buildRangeSpecification(criteria.getAnneeEntree(), Etudiant_.anneeEntree),
                buildSpecification(criteria.getCursusAcheve(), Etudiant_.cursusAcheve),
                buildRangeSpecification(criteria.getAnneeFinale(), Etudiant_.anneeFinale),
                buildStringSpecification(criteria.getKeycloakUserId(), Etudiant_.keycloakUserId),
                buildStringSpecification(criteria.getCommentaire(), Etudiant_.commentaire),
                buildSpecification(criteria.getActif(), Etudiant_.actif),
                buildSpecification(criteria.getPaysId(), root -> root.join(Etudiant_.pays, JoinType.LEFT).get(Pays_.id)),
                buildSpecification(criteria.getInscriptionId(), root ->
                    root.join(Etudiant_.inscriptions, JoinType.LEFT).get(InscriptionCycle_.id)
                ),
                buildSpecification(criteria.getEvenementId(), root ->
                    root.join(Etudiant_.evenements, JoinType.LEFT).get(EvenementEtudiant_.id)
                ),
                buildSpecification(criteria.getNoteId(), root -> root.join(Etudiant_.notes, JoinType.LEFT).get(EvaluationRealisee_.id))
            );
        }
        return specification;
    }
}
