package org.forbidec.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.forbidec.domain.Pays;
import org.forbidec.repository.PaysRepository;
import org.forbidec.service.dto.PaysDTO;
import org.forbidec.service.mapper.PaysMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.Pays}.
 */
@Service
@Transactional
public class PaysService {

    private static final Logger LOG = LoggerFactory.getLogger(PaysService.class);

    private final PaysRepository paysRepository;

    private final PaysMapper paysMapper;

    public PaysService(PaysRepository paysRepository, PaysMapper paysMapper) {
        this.paysRepository = paysRepository;
        this.paysMapper = paysMapper;
    }

    /**
     * Save a pays.
     *
     * @param paysDTO the entity to save.
     * @return the persisted entity.
     */
    public PaysDTO save(PaysDTO paysDTO) {
        LOG.debug("Request to save Pays : {}", paysDTO);
        Pays pays = paysMapper.toEntity(paysDTO);
        pays = paysRepository.save(pays);
        return paysMapper.toDto(pays);
    }

    /**
     * Update a pays.
     *
     * @param paysDTO the entity to save.
     * @return the persisted entity.
     */
    public PaysDTO update(PaysDTO paysDTO) {
        LOG.debug("Request to update Pays : {}", paysDTO);
        Pays pays = paysMapper.toEntity(paysDTO);
        pays = paysRepository.save(pays);
        return paysMapper.toDto(pays);
    }

    /**
     * Partially update a pays.
     *
     * @param paysDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaysDTO> partialUpdate(PaysDTO paysDTO) {
        LOG.debug("Request to partially update Pays : {}", paysDTO);

        return paysRepository
            .findById(paysDTO.getId())
            .map(existingPays -> {
                paysMapper.partialUpdate(existingPays, paysDTO);

                return existingPays;
            })
            .map(paysRepository::save)
            .map(paysMapper::toDto);
    }

    /**
     * Get all the pays.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaysDTO> findAll() {
        LOG.debug("Request to get all Pays");
        return paysRepository.findAll().stream().map(paysMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pays by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaysDTO> findOne(Long id) {
        LOG.debug("Request to get Pays : {}", id);
        return paysRepository.findById(id).map(paysMapper::toDto);
    }

    /**
     * Delete the pays by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pays : {}", id);
        paysRepository.deleteById(id);
    }
}
