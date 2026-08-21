package org.forbidec.service.mapper;

import static org.forbidec.domain.EnseignantAsserts.*;
import static org.forbidec.domain.EnseignantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnseignantMapperTest {

    private EnseignantMapper enseignantMapper;

    @BeforeEach
    void setUp() {
        enseignantMapper = new EnseignantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnseignantSample1();
        var actual = enseignantMapper.toEntity(enseignantMapper.toDto(expected));
        assertEnseignantAllPropertiesEquals(expected, actual);
    }
}
