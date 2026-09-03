package org.forbidec.service;

import java.util.Optional;
import org.forbidec.domain.EvaluationRealisee;
import org.forbidec.repository.EvaluationRealiseeRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.EvaluationRealiseeDTO;
import org.forbidec.service.mapper.EvaluationRealiseeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.forbidec.domain.EvaluationRealisee}.
 */
@Service
@Transactional
public class EvaluationRealiseeService {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationRealiseeService.class);

    private final EvaluationRealiseeRepository evaluationRealiseeRepository;

    private final EvaluationRealiseeMapper evaluationRealiseeMapper;

    private final PaysContextService paysContextService;

    public EvaluationRealiseeService(
        EvaluationRealiseeRepository evaluationRealiseeRepository,
        EvaluationRealiseeMapper evaluationRealiseeMapper,
        PaysContextService paysContextService
    ) {
        this.evaluationRealiseeRepository = evaluationRealiseeRepository;
        this.evaluationRealiseeMapper = evaluationRealiseeMapper;
        this.paysContextService = paysContextService;
    }

    /**
     * Save a evaluationRealisee.
     *
     * @param evaluationRealiseeDTO the entity to save.
     * @return the persisted entity.
     */
    public EvaluationRealiseeDTO save(EvaluationRealiseeDTO evaluationRealiseeDTO) {
        LOG.debug("Request to save EvaluationRealisee : {}", evaluationRealiseeDTO);
        EvaluationRealisee evaluationRealisee = evaluationRealiseeMapper.toEntity(evaluationRealiseeDTO);
        evaluationRealisee = evaluationRealiseeRepository.save(evaluationRealisee);
        return evaluationRealiseeMapper.toDto(evaluationRealisee);
    }

    /**
     * Update a evaluationRealisee.
     *
     * @param evaluationRealiseeDTO the entity to save.
     * @return the persisted entity.
     */
    public EvaluationRealiseeDTO update(EvaluationRealiseeDTO evaluationRealiseeDTO) {
        LOG.debug("Request to update EvaluationRealisee : {}", evaluationRealiseeDTO);
        evaluationRealiseeRepository.findById(evaluationRealiseeDTO.getId()).ifPresent(paysContextService::verifierAccesEvaluationRealisee);
        EvaluationRealisee evaluationRealisee = evaluationRealiseeMapper.toEntity(evaluationRealiseeDTO);
        evaluationRealisee = evaluationRealiseeRepository.save(evaluationRealisee);
        return evaluationRealiseeMapper.toDto(evaluationRealisee);
    }

    /**
     * Partially update a evaluationRealisee.
     *
     * @param evaluationRealiseeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EvaluationRealiseeDTO> partialUpdate(EvaluationRealiseeDTO evaluationRealiseeDTO) {
        LOG.debug("Request to partially update EvaluationRealisee : {}", evaluationRealiseeDTO);

        return evaluationRealiseeRepository
            .findById(evaluationRealiseeDTO.getId())
            .map(existingEvaluationRealisee -> {
                paysContextService.verifierAccesEvaluationRealisee(existingEvaluationRealisee);
                evaluationRealiseeMapper.partialUpdate(existingEvaluationRealisee, evaluationRealiseeDTO);

                return existingEvaluationRealisee;
            })
            .map(evaluationRealiseeRepository::save)
            .map(evaluationRealiseeMapper::toDto);
    }

    /**
     * Get all the evaluationRealisees with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EvaluationRealiseeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return evaluationRealiseeRepository.findAllWithEagerRelationships(pageable).map(evaluationRealiseeMapper::toDto);
    }

    /**
     * Get one evaluationRealisee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EvaluationRealiseeDTO> findOne(Long id) {
        LOG.debug("Request to get EvaluationRealisee : {}", id);
        Optional<EvaluationRealisee> evaluationRealisee = evaluationRealiseeRepository.findOneWithEagerRelationships(id);
        evaluationRealisee.ifPresent(paysContextService::verifierAccesEvaluationRealisee);
        return evaluationRealisee.map(evaluationRealiseeMapper::toDto);
    }

    /**
     * Delete the evaluationRealisee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EvaluationRealisee : {}", id);
        evaluationRealiseeRepository.findById(id).ifPresent(paysContextService::verifierAccesEvaluationRealisee);
        evaluationRealiseeRepository.deleteById(id);
    }
}
