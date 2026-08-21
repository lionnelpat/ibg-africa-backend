package org.forbidec.service.mapper;

import static org.forbidec.domain.PaysAsserts.*;
import static org.forbidec.domain.PaysTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaysMapperTest {

    private PaysMapper paysMapper;

    @BeforeEach
    void setUp() {
        paysMapper = new PaysMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaysSample1();
        var actual = paysMapper.toEntity(paysMapper.toDto(expected));
        assertPaysAllPropertiesEquals(expected, actual);
    }
}
