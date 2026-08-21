package org.forbidec.service.mapper;

import static org.forbidec.domain.SousMatiereAsserts.*;
import static org.forbidec.domain.SousMatiereTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SousMatiereMapperTest {

    private SousMatiereMapper sousMatiereMapper;

    @BeforeEach
    void setUp() {
        sousMatiereMapper = new SousMatiereMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSousMatiereSample1();
        var actual = sousMatiereMapper.toEntity(sousMatiereMapper.toDto(expected));
        assertSousMatiereAllPropertiesEquals(expected, actual);
    }
}
