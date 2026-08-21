package org.forbidec.service.mapper;

import static org.forbidec.domain.InscriptionCycleAsserts.*;
import static org.forbidec.domain.InscriptionCycleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InscriptionCycleMapperTest {

    private InscriptionCycleMapper inscriptionCycleMapper;

    @BeforeEach
    void setUp() {
        inscriptionCycleMapper = new InscriptionCycleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInscriptionCycleSample1();
        var actual = inscriptionCycleMapper.toEntity(inscriptionCycleMapper.toDto(expected));
        assertInscriptionCycleAllPropertiesEquals(expected, actual);
    }
}
