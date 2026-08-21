package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.Etudiant;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.service.dto.EtudiantDTO;
import org.forbidec.service.mapper.EtudiantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Etudiant}.
 */
@Service
@Transactional
public class EtudiantService {

    private static final Logger LOG = LoggerFactory.getLogger(EtudiantService.class);

    private final EtudiantRepository etudiantRepository;

    private final EtudiantMapper etudiantMapper;

    public EtudiantService(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
    }

    /**
     * Save a etudiant.
     *
     * @param etudiantDTO the entity to save.
     * @return the persisted entity.
     */
    public EtudiantDTO save(EtudiantDTO etudiantDTO) {
        LOG.debug("Request to save Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    /**
     * Update a etudiant.
     *
     * @param etudiantDTO the entity to save.
     * @return the persisted entity.
     */
    public EtudiantDTO update(EtudiantDTO etudiantDTO) {
        LOG.debug("Request to update Etudiant : {}", etudiantDTO);
        Etudiant etudiant = etudiantMapper.toEntity(etudiantDTO);
        etudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.toDto(etudiant);
    }

    /**
     * Partially update a etudiant.
     *
     * @param etudiantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EtudiantDTO> partialUpdate(EtudiantDTO etudiantDTO) {
        LOG.debug("Request to partially update Etudiant : {}", etudiantDTO);

        return etudiantRepository
            .findById(etudiantDTO.getId())
            .map(existingEtudiant -> {
                etudiantMapper.partialUpdate(existingEtudiant, etudiantDTO);

                return existingEtudiant;
            })
            .map(etudiantRepository::save)
            .map(etudiantMapper::toDto);
    }

    /**
     * Get all the etudiants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EtudiantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return etudiantRepository.findAllWithEagerRelationships(pageable).map(etudiantMapper::toDto);
    }

    /**
     * Get one etudiant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EtudiantDTO> findOne(Long id) {
        LOG.debug("Request to get Etudiant : {}", id);
        return etudiantRepository.findOneWithEagerRelationships(id).map(etudiantMapper::toDto);
    }

    /**
     * Delete the etudiant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Etudiant : {}", id);
        etudiantRepository.deleteById(id);
    }
}
