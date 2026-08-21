package org.forbidec.service.mapper;

import static org.forbidec.domain.EvaluationRealiseeAsserts.*;
import static org.forbidec.domain.EvaluationRealiseeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluationRealiseeMapperTest {

    private EvaluationRealiseeMapper evaluationRealiseeMapper;

    @BeforeEach
    void setUp() {
        evaluationRealiseeMapper = new EvaluationRealiseeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEvaluationRealiseeSample1();
        var actual = evaluationRealiseeMapper.toEntity(evaluationRealiseeMapper.toDto(expected));
        assertEvaluationRealiseeAllPropertiesEquals(expected, actual);
    }
}
