package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.HabilitationCycle;
import org.forbidec.repository.HabilitationCycleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.HabilitationCycle}.
 */
@Service
@Transactional
public class HabilitationCycleService {

    private static final Logger LOG = LoggerFactory.getLogger(HabilitationCycleService.class);

    private final HabilitationCycleRepository habilitationCycleRepository;

    public HabilitationCycleService(HabilitationCycleRepository habilitationCycleRepository) {
        this.habilitationCycleRepository = habilitationCycleRepository;
    }

    /**
     * Save a habilitationCycle.
     *
     * @param habilitationCycle the entity to save.
     * @return the persisted entity.
     */
    public HabilitationCycle save(HabilitationCycle habilitationCycle) {
        LOG.debug("Request to save HabilitationCycle : {}", habilitationCycle);
        return habilitationCycleRepository.save(habilitationCycle);
    }

    /**
     * Update a habilitationCycle.
     *
     * @param habilitationCycle the entity to save.
     * @return the persisted entity.
     */
    public HabilitationCycle update(HabilitationCycle habilitationCycle) {
        LOG.debug("Request to update HabilitationCycle : {}", habilitationCycle);
        return habilitationCycleRepository.save(habilitationCycle);
    }

    /**
     * Partially update a habilitationCycle.
     *
     * @param habilitationCycle the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HabilitationCycle> partialUpdate(HabilitationCycle habilitationCycle) {
        LOG.debug("Request to partially update HabilitationCycle : {}", habilitationCycle);

        return habilitationCycleRepository
            .findById(habilitationCycle.getId())
            .map(existingHabilitationCycle -> {
                if (habilitationCycle.getKeycloakUserId() != null) {
                    existingHabilitationCycle.setKeycloakUserId(habilitationCycle.getKeycloakUserId());
                }
                if (habilitationCycle.getRoleFonctionnel() != null) {
                    existingHabilitationCycle.setRoleFonctionnel(habilitationCycle.getRoleFonctionnel());
                }
                if (habilitationCycle.getDateDebut() != null) {
                    existingHabilitationCycle.setDateDebut(habilitationCycle.getDateDebut());
                }
                if (habilitationCycle.getDateFin() != null) {
                    existingHabilitationCycle.setDateFin(habilitationCycle.getDateFin());
                }

                return existingHabilitationCycle;
            })
            .map(habilitationCycleRepository::save);
    }

    /**
     * Get all the habilitationCycles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HabilitationCycle> findAll(Pageable pageable) {
        LOG.debug("Request to get all HabilitationCycles");
        return habilitationCycleRepository.findAll(pageable);
    }

    /**
     * Get all the habilitationCycles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HabilitationCycle> findAllWithEagerRelationships(Pageable pageable) {
        return habilitationCycleRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one habilitationCycle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HabilitationCycle> findOne(Long id) {
        LOG.debug("Request to get HabilitationCycle : {}", id);
        return habilitationCycleRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the habilitationCycle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HabilitationCycle : {}", id);
        habilitationCycleRepository.deleteById(id);
    }
}
