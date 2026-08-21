package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.HistoriqueNote;
import org.forbidec.repository.HistoriqueNoteRepository;
import org.forbidec.service.dto.HistoriqueNoteDTO;
import org.forbidec.service.mapper.HistoriqueNoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.HistoriqueNote}.
 */
@Service
@Transactional
public class HistoriqueNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueNoteService.class);

    private final HistoriqueNoteRepository historiqueNoteRepository;

    private final HistoriqueNoteMapper historiqueNoteMapper;

    public HistoriqueNoteService(HistoriqueNoteRepository historiqueNoteRepository, HistoriqueNoteMapper historiqueNoteMapper) {
        this.historiqueNoteRepository = historiqueNoteRepository;
        this.historiqueNoteMapper = historiqueNoteMapper;
    }

    /**
     * Save a historiqueNote.
     *
     * @param historiqueNoteDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriqueNoteDTO save(HistoriqueNoteDTO historiqueNoteDTO) {
        LOG.debug("Request to save HistoriqueNote : {}", historiqueNoteDTO);
        HistoriqueNote historiqueNote = historiqueNoteMapper.toEntity(historiqueNoteDTO);
        historiqueNote = historiqueNoteRepository.save(historiqueNote);
        return historiqueNoteMapper.toDto(historiqueNote);
    }

    /**
     * Update a historiqueNote.
     *
     * @param historiqueNoteDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoriqueNoteDTO update(HistoriqueNoteDTO historiqueNoteDTO) {
        LOG.debug("Request to update HistoriqueNote : {}", historiqueNoteDTO);
        HistoriqueNote historiqueNote = historiqueNoteMapper.toEntity(historiqueNoteDTO);
        historiqueNote = historiqueNoteRepository.save(historiqueNote);
        return historiqueNoteMapper.toDto(historiqueNote);
    }

    /**
     * Partially update a historiqueNote.
     *
     * @param historiqueNoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoriqueNoteDTO> partialUpdate(HistoriqueNoteDTO historiqueNoteDTO) {
        LOG.debug("Request to partially update HistoriqueNote : {}", historiqueNoteDTO);

        return historiqueNoteRepository
            .findById(historiqueNoteDTO.getId())
            .map(existingHistoriqueNote -> {
                historiqueNoteMapper.partialUpdate(existingHistoriqueNote, historiqueNoteDTO);

                return existingHistoriqueNote;
            })
            .map(historiqueNoteRepository::save)
            .map(historiqueNoteMapper::toDto);
    }

    /**
     * Get all the historiqueNotes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HistoriqueNoteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return historiqueNoteRepository.findAllWithEagerRelationships(pageable).map(historiqueNoteMapper::toDto);
    }

    /**
     * Get one historiqueNote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoriqueNoteDTO> findOne(Long id) {
        LOG.debug("Request to get HistoriqueNote : {}", id);
        return historiqueNoteRepository.findOneWithEagerRelationships(id).map(historiqueNoteMapper::toDto);
    }

    /**
     * Delete the historiqueNote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HistoriqueNote : {}", id);
        historiqueNoteRepository.deleteById(id);
    }
}
