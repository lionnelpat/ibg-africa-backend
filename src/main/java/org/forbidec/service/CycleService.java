package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.Cycle;
import org.forbidec.repository.CycleRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.CycleDTO;
import org.forbidec.service.mapper.CycleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Cycle}.
 */
@Service
@Transactional
public class CycleService {

    private static final Logger LOG = LoggerFactory.getLogger(CycleService.class);

    private final CycleRepository cycleRepository;

    private final CycleMapper cycleMapper;

    private final PaysContextService paysContextService;

    public CycleService(CycleRepository cycleRepository, CycleMapper cycleMapper, PaysContextService paysContextService) {
        this.cycleRepository = cycleRepository;
        this.cycleMapper = cycleMapper;
        this.paysContextService = paysContextService;
    }

    /**
     * Save a cycle.
     *
     * @param cycleDTO the entity to save.
     * @return the persisted entity.
     */
    public CycleDTO save(CycleDTO cycleDTO) {
        LOG.debug("Request to save Cycle : {}", cycleDTO);
        Cycle cycle = cycleMapper.toEntity(cycleDTO);
        cycle = cycleRepository.save(cycle);
        return cycleMapper.toDto(cycle);
    }

    /**
     * Update a cycle.
     *
     * @param cycleDTO the entity to save.
     * @return the persisted entity.
     */
    public CycleDTO update(CycleDTO cycleDTO) {
        LOG.debug("Request to update Cycle : {}", cycleDTO);
        cycleRepository.findById(cycleDTO.getId()).ifPresent(paysContextService::verifierAccesCycle);
        Cycle cycle = cycleMapper.toEntity(cycleDTO);
        cycle = cycleRepository.save(cycle);
        return cycleMapper.toDto(cycle);
    }

    /**
     * Partially update a cycle.
     *
     * @param cycleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CycleDTO> partialUpdate(CycleDTO cycleDTO) {
        LOG.debug("Request to partially update Cycle : {}", cycleDTO);

        return cycleRepository
            .findById(cycleDTO.getId())
            .map(existingCycle -> {
                paysContextService.verifierAccesCycle(existingCycle);
                cycleMapper.partialUpdate(existingCycle, cycleDTO);

                return existingCycle;
            })
            .map(cycleRepository::save)
            .map(cycleMapper::toDto);
    }

    /**
     * Get all the cycles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CycleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cycleRepository.findAllWithEagerRelationships(pageable).map(cycleMapper::toDto);
    }

    /**
     * Get one cycle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CycleDTO> findOne(Long id) {
        LOG.debug("Request to get Cycle : {}", id);
        Optional<Cycle> cycle = cycleRepository.findOneWithEagerRelationships(id);
        cycle.ifPresent(paysContextService::verifierAccesCycle);
        return cycle.map(cycleMapper::toDto);
    }

    /**
     * Delete the cycle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cycle : {}", id);
        cycleRepository.findById(id).ifPresent(paysContextService::verifierAccesCycle);
        cycleRepository.deleteById(id);
    }
}
