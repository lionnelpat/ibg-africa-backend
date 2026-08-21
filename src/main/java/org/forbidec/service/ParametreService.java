package org.forbidec.service;

import java.util.List;
import java.util.Optional;
import org.forbidec.domain.Parametre;
import org.forbidec.repository.ParametreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Parametre}.
 */
@Service
@Transactional
public class ParametreService {

    private static final Logger LOG = LoggerFactory.getLogger(ParametreService.class);

    private final ParametreRepository parametreRepository;

    public ParametreService(ParametreRepository parametreRepository) {
        this.parametreRepository = parametreRepository;
    }

    /**
     * Save a parametre.
     *
     * @param parametre the entity to save.
     * @return the persisted entity.
     */
    public Parametre save(Parametre parametre) {
        LOG.debug("Request to save Parametre : {}", parametre);
        return parametreRepository.save(parametre);
    }

    /**
     * Update a parametre.
     *
     * @param parametre the entity to save.
     * @return the persisted entity.
     */
    public Parametre update(Parametre parametre) {
        LOG.debug("Request to update Parametre : {}", parametre);
        return parametreRepository.save(parametre);
    }

    /**
     * Partially update a parametre.
     *
     * @param parametre the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Parametre> partialUpdate(Parametre parametre) {
        LOG.debug("Request to partially update Parametre : {}", parametre);

        return parametreRepository
            .findById(parametre.getId())
            .map(existingParametre -> {
                if (parametre.getCle() != null) {
                    existingParametre.setCle(parametre.getCle());
                }
                if (parametre.getLibelle() != null) {
                    existingParametre.setLibelle(parametre.getLibelle());
                }
                if (parametre.getValeur() != null) {
                    existingParametre.setValeur(parametre.getValeur());
                }
                if (parametre.getTypeValeur() != null) {
                    existingParametre.setTypeValeur(parametre.getTypeValeur());
                }
                if (parametre.getModifiableUi() != null) {
                    existingParametre.setModifiableUi(parametre.getModifiableUi());
                }

                return existingParametre;
            })
            .map(parametreRepository::save);
    }

    /**
     * Get all the parametres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Parametre> findAll() {
        LOG.debug("Request to get all Parametres");
        return parametreRepository.findAll();
    }

    /**
     * Get all the parametres with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Parametre> findAllWithEagerRelationships(Pageable pageable) {
        return parametreRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one parametre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Parametre> findOne(Long id) {
        LOG.debug("Request to get Parametre : {}", id);
        return parametreRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the parametre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Parametre : {}", id);
        parametreRepository.deleteById(id);
    }
}
