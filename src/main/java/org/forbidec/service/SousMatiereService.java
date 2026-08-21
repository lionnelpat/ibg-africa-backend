package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.SousMatiere;
import org.forbidec.repository.SousMatiereRepository;
import org.forbidec.service.dto.SousMatiereDTO;
import org.forbidec.service.mapper.SousMatiereMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.SousMatiere}.
 */
@Service
@Transactional
public class SousMatiereService {

    private static final Logger LOG = LoggerFactory.getLogger(SousMatiereService.class);

    private final SousMatiereRepository sousMatiereRepository;

    private final SousMatiereMapper sousMatiereMapper;

    public SousMatiereService(SousMatiereRepository sousMatiereRepository, SousMatiereMapper sousMatiereMapper) {
        this.sousMatiereRepository = sousMatiereRepository;
        this.sousMatiereMapper = sousMatiereMapper;
    }

    /**
     * Save a sousMatiere.
     *
     * @param sousMatiereDTO the entity to save.
     * @return the persisted entity.
     */
    public SousMatiereDTO save(SousMatiereDTO sousMatiereDTO) {
        LOG.debug("Request to save SousMatiere : {}", sousMatiereDTO);
        SousMatiere sousMatiere = sousMatiereMapper.toEntity(sousMatiereDTO);
        sousMatiere = sousMatiereRepository.save(sousMatiere);
        return sousMatiereMapper.toDto(sousMatiere);
    }

    /**
     * Update a sousMatiere.
     *
     * @param sousMatiereDTO the entity to save.
     * @return the persisted entity.
     */
    public SousMatiereDTO update(SousMatiereDTO sousMatiereDTO) {
        LOG.debug("Request to update SousMatiere : {}", sousMatiereDTO);
        SousMatiere sousMatiere = sousMatiereMapper.toEntity(sousMatiereDTO);
        sousMatiere = sousMatiereRepository.save(sousMatiere);
        return sousMatiereMapper.toDto(sousMatiere);
    }

    /**
     * Partially update a sousMatiere.
     *
     * @param sousMatiereDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SousMatiereDTO> partialUpdate(SousMatiereDTO sousMatiereDTO) {
        LOG.debug("Request to partially update SousMatiere : {}", sousMatiereDTO);

        return sousMatiereRepository
            .findById(sousMatiereDTO.getId())
            .map(existingSousMatiere -> {
                sousMatiereMapper.partialUpdate(existingSousMatiere, sousMatiereDTO);

                return existingSousMatiere;
            })
            .map(sousMatiereRepository::save)
            .map(sousMatiereMapper::toDto);
    }

    /**
     * Get all the sousMatieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SousMatiereDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SousMatieres");
        return sousMatiereRepository.findAll(pageable).map(sousMatiereMapper::toDto);
    }

    /**
     * Get one sousMatiere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SousMatiereDTO> findOne(Long id) {
        LOG.debug("Request to get SousMatiere : {}", id);
        return sousMatiereRepository.findById(id).map(sousMatiereMapper::toDto);
    }

    /**
     * Delete the sousMatiere by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SousMatiere : {}", id);
        sousMatiereRepository.deleteById(id);
    }
}
