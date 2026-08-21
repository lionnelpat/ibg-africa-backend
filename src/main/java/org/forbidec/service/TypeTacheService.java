package org.forbidec.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.forbidec.domain.TypeTache;
import org.forbidec.repository.TypeTacheRepository;
import org.forbidec.service.dto.TypeTacheDTO;
import org.forbidec.service.mapper.TypeTacheMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.TypeTache}.
 */
@Service
@Transactional
public class TypeTacheService {

    private static final Logger LOG = LoggerFactory.getLogger(TypeTacheService.class);

    private final TypeTacheRepository typeTacheRepository;

    private final TypeTacheMapper typeTacheMapper;

    public TypeTacheService(TypeTacheRepository typeTacheRepository, TypeTacheMapper typeTacheMapper) {
        this.typeTacheRepository = typeTacheRepository;
        this.typeTacheMapper = typeTacheMapper;
    }

    /**
     * Save a typeTache.
     *
     * @param typeTacheDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeTacheDTO save(TypeTacheDTO typeTacheDTO) {
        LOG.debug("Request to save TypeTache : {}", typeTacheDTO);
        TypeTache typeTache = typeTacheMapper.toEntity(typeTacheDTO);
        typeTache = typeTacheRepository.save(typeTache);
        return typeTacheMapper.toDto(typeTache);
    }

    /**
     * Update a typeTache.
     *
     * @param typeTacheDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeTacheDTO update(TypeTacheDTO typeTacheDTO) {
        LOG.debug("Request to update TypeTache : {}", typeTacheDTO);
        TypeTache typeTache = typeTacheMapper.toEntity(typeTacheDTO);
        typeTache = typeTacheRepository.save(typeTache);
        return typeTacheMapper.toDto(typeTache);
    }

    /**
     * Partially update a typeTache.
     *
     * @param typeTacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeTacheDTO> partialUpdate(TypeTacheDTO typeTacheDTO) {
        LOG.debug("Request to partially update TypeTache : {}", typeTacheDTO);

        return typeTacheRepository
            .findById(typeTacheDTO.getId())
            .map(existingTypeTache -> {
                typeTacheMapper.partialUpdate(existingTypeTache, typeTacheDTO);

                return existingTypeTache;
            })
            .map(typeTacheRepository::save)
            .map(typeTacheMapper::toDto);
    }

    /**
     * Get all the typeTaches.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeTacheDTO> findAll() {
        LOG.debug("Request to get all TypeTaches");
        return typeTacheRepository.findAll().stream().map(typeTacheMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one typeTache by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeTacheDTO> findOne(Long id) {
        LOG.debug("Request to get TypeTache : {}", id);
        return typeTacheRepository.findById(id).map(typeTacheMapper::toDto);
    }

    /**
     * Delete the typeTache by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TypeTache : {}", id);
        typeTacheRepository.deleteById(id);
    }
}
