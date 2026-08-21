package org.forbidec.service.mapper;

import static org.forbidec.domain.EvaluationPrevueAsserts.*;
import static org.forbidec.domain.EvaluationPrevueTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluationPrevueMapperTest {

    private EvaluationPrevueMapper evaluationPrevueMapper;

    @BeforeEach
    void setUp() {
        evaluationPrevueMapper = new EvaluationPrevueMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEvaluationPrevueSample1();
        var actual = evaluationPrevueMapper.toEntity(evaluationPrevueMapper.toDto(expected));
        assertEvaluationPrevueAllPropertiesEquals(expected, actual);
    }
}
