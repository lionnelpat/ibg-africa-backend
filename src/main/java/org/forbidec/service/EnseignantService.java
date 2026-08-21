package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.Enseignant;
import org.forbidec.repository.EnseignantRepository;
import org.forbidec.service.dto.EnseignantDTO;
import org.forbidec.service.mapper.EnseignantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Enseignant}.
 */
@Service
@Transactional
public class EnseignantService {

    private static final Logger LOG = LoggerFactory.getLogger(EnseignantService.class);

    private final EnseignantRepository enseignantRepository;

    private final EnseignantMapper enseignantMapper;

    public EnseignantService(EnseignantRepository enseignantRepository, EnseignantMapper enseignantMapper) {
        this.enseignantRepository = enseignantRepository;
        this.enseignantMapper = enseignantMapper;
    }

    /**
     * Save a enseignant.
     *
     * @param enseignantDTO the entity to save.
     * @return the persisted entity.
     */
    public EnseignantDTO save(EnseignantDTO enseignantDTO) {
        LOG.debug("Request to save Enseignant : {}", enseignantDTO);
        Enseignant enseignant = enseignantMapper.toEntity(enseignantDTO);
        enseignant = enseignantRepository.save(enseignant);
        return enseignantMapper.toDto(enseignant);
    }

    /**
     * Update a enseignant.
     *
     * @param enseignantDTO the entity to save.
     * @return the persisted entity.
     */
    public EnseignantDTO update(EnseignantDTO enseignantDTO) {
        LOG.debug("Request to update Enseignant : {}", enseignantDTO);
        Enseignant enseignant = enseignantMapper.toEntity(enseignantDTO);
        enseignant = enseignantRepository.save(enseignant);
        return enseignantMapper.toDto(enseignant);
    }

    /**
     * Partially update a enseignant.
     *
     * @param enseignantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EnseignantDTO> partialUpdate(EnseignantDTO enseignantDTO) {
        LOG.debug("Request to partially update Enseignant : {}", enseignantDTO);

        return enseignantRepository
            .findById(enseignantDTO.getId())
            .map(existingEnseignant -> {
                enseignantMapper.partialUpdate(existingEnseignant, enseignantDTO);

                return existingEnseignant;
            })
            .map(enseignantRepository::save)
            .map(enseignantMapper::toDto);
    }

    /**
     * Get one enseignant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EnseignantDTO> findOne(Long id) {
        LOG.debug("Request to get Enseignant : {}", id);
        return enseignantRepository.findById(id).map(enseignantMapper::toDto);
    }

    /**
     * Delete the enseignant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Enseignant : {}", id);
        enseignantRepository.deleteById(id);
    }
}
