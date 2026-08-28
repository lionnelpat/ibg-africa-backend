package org.forbidec.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.forbidec.domain.CentreFormation;
import org.forbidec.repository.CentreFormationRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.CentreFormationDTO;
import org.forbidec.service.mapper.CentreFormationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.CentreFormation}.
 */
@Service
@Transactional
public class CentreFormationService {

    private static final Logger LOG = LoggerFactory.getLogger(CentreFormationService.class);

    private final CentreFormationRepository centreFormationRepository;

    private final CentreFormationMapper centreFormationMapper;

    private final PaysContextService paysContextService;

    public CentreFormationService(
        CentreFormationRepository centreFormationRepository,
        CentreFormationMapper centreFormationMapper,
        PaysContextService paysContextService
    ) {
        this.centreFormationRepository = centreFormationRepository;
        this.centreFormationMapper = centreFormationMapper;
        this.paysContextService = paysContextService;
    }

    /**
     * Save a centreFormation.
     *
     * @param centreFormationDTO the entity to save.
     * @return the persisted entity.
     */
    public CentreFormationDTO save(CentreFormationDTO centreFormationDTO) {
        LOG.debug("Request to save CentreFormation : {}", centreFormationDTO);
        CentreFormation centreFormation = centreFormationMapper.toEntity(centreFormationDTO);
        centreFormation = centreFormationRepository.save(centreFormation);
        return centreFormationMapper.toDto(centreFormation);
    }

    /**
     * Update a centreFormation.
     *
     * @param centreFormationDTO the entity to save.
     * @return the persisted entity.
     */
    public CentreFormationDTO update(CentreFormationDTO centreFormationDTO) {
        LOG.debug("Request to update CentreFormation : {}", centreFormationDTO);
        CentreFormation centreFormation = centreFormationMapper.toEntity(centreFormationDTO);
        centreFormation = centreFormationRepository.save(centreFormation);
        return centreFormationMapper.toDto(centreFormation);
    }

    /**
     * Partially update a centreFormation.
     *
     * @param centreFormationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CentreFormationDTO> partialUpdate(CentreFormationDTO centreFormationDTO) {
        LOG.debug("Request to partially update CentreFormation : {}", centreFormationDTO);

        return centreFormationRepository
            .findById(centreFormationDTO.getId())
            .map(existingCentreFormation -> {
                centreFormationMapper.partialUpdate(existingCentreFormation, centreFormationDTO);

                return existingCentreFormation;
            })
            .map(centreFormationRepository::save)
            .map(centreFormationMapper::toDto);
    }

    /**
     * Get all the centreFormations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CentreFormationDTO> findAll() {
        LOG.debug("Request to get all CentreFormations");
        paysContextService.enableFilterForCurrentRequest();
        return centreFormationRepository
            .findAll()
            .stream()
            .map(centreFormationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the centreFormations with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CentreFormationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return centreFormationRepository.findAllWithEagerRelationships(pageable).map(centreFormationMapper::toDto);
    }

    /**
     * Get one centreFormation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CentreFormationDTO> findOne(Long id) {
        LOG.debug("Request to get CentreFormation : {}", id);
        return centreFormationRepository.findOneWithEagerRelationships(id).map(centreFormationMapper::toDto);
    }

    /**
     * Delete the centreFormation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CentreFormation : {}", id);
        centreFormationRepository.deleteById(id);
    }
}
