package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.Cours;
import org.forbidec.repository.CoursRepository;
import org.forbidec.service.dto.CoursDTO;
import org.forbidec.service.mapper.CoursMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Cours}.
 */
@Service
@Transactional
public class CoursService {

    private static final Logger LOG = LoggerFactory.getLogger(CoursService.class);

    private final CoursRepository coursRepository;

    private final CoursMapper coursMapper;

    public CoursService(CoursRepository coursRepository, CoursMapper coursMapper) {
        this.coursRepository = coursRepository;
        this.coursMapper = coursMapper;
    }

    /**
     * Save a cours.
     *
     * @param coursDTO the entity to save.
     * @return the persisted entity.
     */
    public CoursDTO save(CoursDTO coursDTO) {
        LOG.debug("Request to save Cours : {}", coursDTO);
        Cours cours = coursMapper.toEntity(coursDTO);
        cours = coursRepository.save(cours);
        return coursMapper.toDto(cours);
    }

    /**
     * Update a cours.
     *
     * @param coursDTO the entity to save.
     * @return the persisted entity.
     */
    public CoursDTO update(CoursDTO coursDTO) {
        LOG.debug("Request to update Cours : {}", coursDTO);
        Cours cours = coursMapper.toEntity(coursDTO);
        cours = coursRepository.save(cours);
        return coursMapper.toDto(cours);
    }

    /**
     * Partially update a cours.
     *
     * @param coursDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CoursDTO> partialUpdate(CoursDTO coursDTO) {
        LOG.debug("Request to partially update Cours : {}", coursDTO);

        return coursRepository
            .findById(coursDTO.getId())
            .map(existingCours -> {
                coursMapper.partialUpdate(existingCours, coursDTO);

                return existingCours;
            })
            .map(coursRepository::save)
            .map(coursMapper::toDto);
    }

    /**
     * Get one cours by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CoursDTO> findOne(Long id) {
        LOG.debug("Request to get Cours : {}", id);
        return coursRepository.findById(id).map(coursMapper::toDto);
    }

    /**
     * Delete the cours by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cours : {}", id);
        coursRepository.deleteById(id);
    }
}
