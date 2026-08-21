package org.forbidec.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.forbidec.domain.Matiere;
import org.forbidec.repository.MatiereRepository;
import org.forbidec.service.dto.MatiereDTO;
import org.forbidec.service.mapper.MatiereMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Matiere}.
 */
@Service
@Transactional
public class MatiereService {

    private static final Logger LOG = LoggerFactory.getLogger(MatiereService.class);

    private final MatiereRepository matiereRepository;

    private final MatiereMapper matiereMapper;

    public MatiereService(MatiereRepository matiereRepository, MatiereMapper matiereMapper) {
        this.matiereRepository = matiereRepository;
        this.matiereMapper = matiereMapper;
    }

    /**
     * Save a matiere.
     *
     * @param matiereDTO the entity to save.
     * @return the persisted entity.
     */
    public MatiereDTO save(MatiereDTO matiereDTO) {
        LOG.debug("Request to save Matiere : {}", matiereDTO);
        Matiere matiere = matiereMapper.toEntity(matiereDTO);
        matiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(matiere);
    }

    /**
     * Update a matiere.
     *
     * @param matiereDTO the entity to save.
     * @return the persisted entity.
     */
    public MatiereDTO update(MatiereDTO matiereDTO) {
        LOG.debug("Request to update Matiere : {}", matiereDTO);
        Matiere matiere = matiereMapper.toEntity(matiereDTO);
        matiere = matiereRepository.save(matiere);
        return matiereMapper.toDto(matiere);
    }

    /**
     * Partially update a matiere.
     *
     * @param matiereDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MatiereDTO> partialUpdate(MatiereDTO matiereDTO) {
        LOG.debug("Request to partially update Matiere : {}", matiereDTO);

        return matiereRepository
            .findById(matiereDTO.getId())
            .map(existingMatiere -> {
                matiereMapper.partialUpdate(existingMatiere, matiereDTO);

                return existingMatiere;
            })
            .map(matiereRepository::save)
            .map(matiereMapper::toDto);
    }

    /**
     * Get all the matieres.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MatiereDTO> findAll() {
        LOG.debug("Request to get all Matieres");
        return matiereRepository.findAll().stream().map(matiereMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one matiere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MatiereDTO> findOne(Long id) {
        LOG.debug("Request to get Matiere : {}", id);
        return matiereRepository.findById(id).map(matiereMapper::toDto);
    }

    /**
     * Delete the matiere by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
    }
}
