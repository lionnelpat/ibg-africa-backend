package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.repository.EvaluationPrevueRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.EvaluationPrevueDTO;
import org.forbidec.service.mapper.EvaluationPrevueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.EvaluationPrevue}.
 */
@Service
@Transactional
public class EvaluationPrevueService {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationPrevueService.class);

    private final EvaluationPrevueRepository evaluationPrevueRepository;

    private final EvaluationPrevueMapper evaluationPrevueMapper;

    private final PaysContextService paysContextService;

    public EvaluationPrevueService(
        EvaluationPrevueRepository evaluationPrevueRepository,
        EvaluationPrevueMapper evaluationPrevueMapper,
        PaysContextService paysContextService
    ) {
        this.evaluationPrevueRepository = evaluationPrevueRepository;
        this.evaluationPrevueMapper = evaluationPrevueMapper;
        this.paysContextService = paysContextService;
    }

    /**
     * Save a evaluationPrevue.
     *
     * @param evaluationPrevueDTO the entity to save.
     * @return the persisted entity.
     */
    public EvaluationPrevueDTO save(EvaluationPrevueDTO evaluationPrevueDTO) {
        LOG.debug("Request to save EvaluationPrevue : {}", evaluationPrevueDTO);
        EvaluationPrevue evaluationPrevue = evaluationPrevueMapper.toEntity(evaluationPrevueDTO);
        evaluationPrevue = evaluationPrevueRepository.save(evaluationPrevue);
        return evaluationPrevueMapper.toDto(evaluationPrevue);
    }

    /**
     * Update a evaluationPrevue.
     *
     * @param evaluationPrevueDTO the entity to save.
     * @return the persisted entity.
     */
    public EvaluationPrevueDTO update(EvaluationPrevueDTO evaluationPrevueDTO) {
        LOG.debug("Request to update EvaluationPrevue : {}", evaluationPrevueDTO);
        evaluationPrevueRepository.findById(evaluationPrevueDTO.getId()).ifPresent(paysContextService::verifierAccesEvaluationPrevue);
        EvaluationPrevue evaluationPrevue = evaluationPrevueMapper.toEntity(evaluationPrevueDTO);
        evaluationPrevue = evaluationPrevueRepository.save(evaluationPrevue);
        return evaluationPrevueMapper.toDto(evaluationPrevue);
    }

    /**
     * Partially update a evaluationPrevue.
     *
     * @param evaluationPrevueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EvaluationPrevueDTO> partialUpdate(EvaluationPrevueDTO evaluationPrevueDTO) {
        LOG.debug("Request to partially update EvaluationPrevue : {}", evaluationPrevueDTO);

        return evaluationPrevueRepository
            .findById(evaluationPrevueDTO.getId())
            .map(existingEvaluationPrevue -> {
                paysContextService.verifierAccesEvaluationPrevue(existingEvaluationPrevue);
                evaluationPrevueMapper.partialUpdate(existingEvaluationPrevue, evaluationPrevueDTO);

                return existingEvaluationPrevue;
            })
            .map(evaluationPrevueRepository::save)
            .map(evaluationPrevueMapper::toDto);
    }

    /**
     * Get all the evaluationPrevues with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EvaluationPrevueDTO> findAllWithEagerRelationships(Pageable pageable) {
        return evaluationPrevueRepository.findAllWithEagerRelationships(pageable).map(evaluationPrevueMapper::toDto);
    }

    /**
     * Get one evaluationPrevue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EvaluationPrevueDTO> findOne(Long id) {
        LOG.debug("Request to get EvaluationPrevue : {}", id);
        Optional<EvaluationPrevue> evaluationPrevue = evaluationPrevueRepository.findOneWithEagerRelationships(id);
        evaluationPrevue.ifPresent(paysContextService::verifierAccesEvaluationPrevue);
        return evaluationPrevue.map(evaluationPrevueMapper::toDto);
    }

    /**
     * Delete the evaluationPrevue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EvaluationPrevue : {}", id);
        evaluationPrevueRepository.findById(id).ifPresent(paysContextService::verifierAccesEvaluationPrevue);
        evaluationPrevueRepository.deleteById(id);
    }
}
