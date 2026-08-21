package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.EvenementEtudiant;
import org.forbidec.repository.EvenementEtudiantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.EvenementEtudiant}.
 */
@Service
@Transactional
public class EvenementEtudiantService {

    private static final Logger LOG = LoggerFactory.getLogger(EvenementEtudiantService.class);

    private final EvenementEtudiantRepository evenementEtudiantRepository;

    public EvenementEtudiantService(EvenementEtudiantRepository evenementEtudiantRepository) {
        this.evenementEtudiantRepository = evenementEtudiantRepository;
    }

    /**
     * Save a evenementEtudiant.
     *
     * @param evenementEtudiant the entity to save.
     * @return the persisted entity.
     */
    public EvenementEtudiant save(EvenementEtudiant evenementEtudiant) {
        LOG.debug("Request to save EvenementEtudiant : {}", evenementEtudiant);
        return evenementEtudiantRepository.save(evenementEtudiant);
    }

    /**
     * Update a evenementEtudiant.
     *
     * @param evenementEtudiant the entity to save.
     * @return the persisted entity.
     */
    public EvenementEtudiant update(EvenementEtudiant evenementEtudiant) {
        LOG.debug("Request to update EvenementEtudiant : {}", evenementEtudiant);
        return evenementEtudiantRepository.save(evenementEtudiant);
    }

    /**
     * Partially update a evenementEtudiant.
     *
     * @param evenementEtudiant the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EvenementEtudiant> partialUpdate(EvenementEtudiant evenementEtudiant) {
        LOG.debug("Request to partially update EvenementEtudiant : {}", evenementEtudiant);

        return evenementEtudiantRepository
            .findById(evenementEtudiant.getId())
            .map(existingEvenementEtudiant -> {
                if (evenementEtudiant.getDateEvenement() != null) {
                    existingEvenementEtudiant.setDateEvenement(evenementEtudiant.getDateEvenement());
                }
                if (evenementEtudiant.getIntitule() != null) {
                    existingEvenementEtudiant.setIntitule(evenementEtudiant.getIntitule());
                }
                if (evenementEtudiant.getCommentaire() != null) {
                    existingEvenementEtudiant.setCommentaire(evenementEtudiant.getCommentaire());
                }

                return existingEvenementEtudiant;
            })
            .map(evenementEtudiantRepository::save);
    }

    /**
     * Get all the evenementEtudiants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EvenementEtudiant> findAll(Pageable pageable) {
        LOG.debug("Request to get all EvenementEtudiants");
        return evenementEtudiantRepository.findAll(pageable);
    }

    /**
     * Get all the evenementEtudiants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EvenementEtudiant> findAllWithEagerRelationships(Pageable pageable) {
        return evenementEtudiantRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one evenementEtudiant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EvenementEtudiant> findOne(Long id) {
        LOG.debug("Request to get EvenementEtudiant : {}", id);
        return evenementEtudiantRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the evenementEtudiant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EvenementEtudiant : {}", id);
        evenementEtudiantRepository.deleteById(id);
    }
}
