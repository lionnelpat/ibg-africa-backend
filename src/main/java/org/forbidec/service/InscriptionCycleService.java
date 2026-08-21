package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.InscriptionCycle;
import org.forbidec.repository.InscriptionCycleRepository;
import org.forbidec.service.dto.InscriptionCycleDTO;
import org.forbidec.service.mapper.InscriptionCycleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.InscriptionCycle}.
 */
@Service
@Transactional
public class InscriptionCycleService {

    private static final Logger LOG = LoggerFactory.getLogger(InscriptionCycleService.class);

    private final InscriptionCycleRepository inscriptionCycleRepository;

    private final InscriptionCycleMapper inscriptionCycleMapper;

    public InscriptionCycleService(InscriptionCycleRepository inscriptionCycleRepository, InscriptionCycleMapper inscriptionCycleMapper) {
        this.inscriptionCycleRepository = inscriptionCycleRepository;
        this.inscriptionCycleMapper = inscriptionCycleMapper;
    }

    /**
     * Save a inscriptionCycle.
     *
     * @param inscriptionCycleDTO the entity to save.
     * @return the persisted entity.
     */
    public InscriptionCycleDTO save(InscriptionCycleDTO inscriptionCycleDTO) {
        LOG.debug("Request to save InscriptionCycle : {}", inscriptionCycleDTO);
        InscriptionCycle inscriptionCycle = inscriptionCycleMapper.toEntity(inscriptionCycleDTO);
        inscriptionCycle = inscriptionCycleRepository.save(inscriptionCycle);
        return inscriptionCycleMapper.toDto(inscriptionCycle);
    }

    /**
     * Update a inscriptionCycle.
     *
     * @param inscriptionCycleDTO the entity to save.
     * @return the persisted entity.
     */
    public InscriptionCycleDTO update(InscriptionCycleDTO inscriptionCycleDTO) {
        LOG.debug("Request to update InscriptionCycle : {}", inscriptionCycleDTO);
        InscriptionCycle inscriptionCycle = inscriptionCycleMapper.toEntity(inscriptionCycleDTO);
        inscriptionCycle = inscriptionCycleRepository.save(inscriptionCycle);
        return inscriptionCycleMapper.toDto(inscriptionCycle);
    }

    /**
     * Partially update a inscriptionCycle.
     *
     * @param inscriptionCycleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InscriptionCycleDTO> partialUpdate(InscriptionCycleDTO inscriptionCycleDTO) {
        LOG.debug("Request to partially update InscriptionCycle : {}", inscriptionCycleDTO);

        return inscriptionCycleRepository
            .findById(inscriptionCycleDTO.getId())
            .map(existingInscriptionCycle -> {
                inscriptionCycleMapper.partialUpdate(existingInscriptionCycle, inscriptionCycleDTO);

                return existingInscriptionCycle;
            })
            .map(inscriptionCycleRepository::save)
            .map(inscriptionCycleMapper::toDto);
    }

    /**
     * Get all the inscriptionCycles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InscriptionCycleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return inscriptionCycleRepository.findAllWithEagerRelationships(pageable).map(inscriptionCycleMapper::toDto);
    }

    /**
     * Get one inscriptionCycle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InscriptionCycleDTO> findOne(Long id) {
        LOG.debug("Request to get InscriptionCycle : {}", id);
        return inscriptionCycleRepository.findOneWithEagerRelationships(id).map(inscriptionCycleMapper::toDto);
    }

    /**
     * Delete the inscriptionCycle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InscriptionCycle : {}", id);
        inscriptionCycleRepository.deleteById(id);
    }
}
