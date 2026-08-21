package org.forbidec.service.mapper;

import static org.forbidec.domain.EtudiantAsserts.*;
import static org.forbidec.domain.EtudiantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EtudiantMapperTest {

    private EtudiantMapper etudiantMapper;

    @BeforeEach
    void setUp() {
        etudiantMapper = new EtudiantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEtudiantSample1();
        var actual = etudiantMapper.toEntity(etudiantMapper.toDto(expected));
        assertEtudiantAllPropertiesEquals(expected, actual);
    }
}
