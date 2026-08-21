package org.forbidec.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.forbidec.domain.BaremeMention;
import org.forbidec.repository.BaremeMentionRepository;
import org.forbidec.service.dto.BaremeMentionDTO;
import org.forbidec.service.mapper.BaremeMentionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.BaremeMention}.
 */
@Service
@Transactional
public class BaremeMentionService {

    private static final Logger LOG = LoggerFactory.getLogger(BaremeMentionService.class);

    private final BaremeMentionRepository baremeMentionRepository;

    private final BaremeMentionMapper baremeMentionMapper;

    public BaremeMentionService(BaremeMentionRepository baremeMentionRepository, BaremeMentionMapper baremeMentionMapper) {
        this.baremeMentionRepository = baremeMentionRepository;
        this.baremeMentionMapper = baremeMentionMapper;
    }

    /**
     * Save a baremeMention.
     *
     * @param baremeMentionDTO the entity to save.
     * @return the persisted entity.
     */
    public BaremeMentionDTO save(BaremeMentionDTO baremeMentionDTO) {
        LOG.debug("Request to save BaremeMention : {}", baremeMentionDTO);
        BaremeMention baremeMention = baremeMentionMapper.toEntity(baremeMentionDTO);
        baremeMention = baremeMentionRepository.save(baremeMention);
        return baremeMentionMapper.toDto(baremeMention);
    }

    /**
     * Update a baremeMention.
     *
     * @param baremeMentionDTO the entity to save.
     * @return the persisted entity.
     */
    public BaremeMentionDTO update(BaremeMentionDTO baremeMentionDTO) {
        LOG.debug("Request to update BaremeMention : {}", baremeMentionDTO);
        BaremeMention baremeMention = baremeMentionMapper.toEntity(baremeMentionDTO);
        baremeMention = baremeMentionRepository.save(baremeMention);
        return baremeMentionMapper.toDto(baremeMention);
    }

    /**
     * Partially update a baremeMention.
     *
     * @param baremeMentionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BaremeMentionDTO> partialUpdate(BaremeMentionDTO baremeMentionDTO) {
        LOG.debug("Request to partially update BaremeMention : {}", baremeMentionDTO);

        return baremeMentionRepository
            .findById(baremeMentionDTO.getId())
            .map(existingBaremeMention -> {
                baremeMentionMapper.partialUpdate(existingBaremeMention, baremeMentionDTO);

                return existingBaremeMention;
            })
            .map(baremeMentionRepository::save)
            .map(baremeMentionMapper::toDto);
    }

    /**
     * Get all the baremeMentions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BaremeMentionDTO> findAll() {
        LOG.debug("Request to get all BaremeMentions");
        return baremeMentionRepository.findAll().stream().map(baremeMentionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the baremeMentions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BaremeMentionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return baremeMentionRepository.findAllWithEagerRelationships(pageable).map(baremeMentionMapper::toDto);
    }

    /**
     * Get one baremeMention by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BaremeMentionDTO> findOne(Long id) {
        LOG.debug("Request to get BaremeMention : {}", id);
        return baremeMentionRepository.findOneWithEagerRelationships(id).map(baremeMentionMapper::toDto);
    }

    /**
     * Delete the baremeMention by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BaremeMention : {}", id);
        baremeMentionRepository.deleteById(id);
    }
}
