package org.forbidec.service.mapper;

import static org.forbidec.domain.MatiereAsserts.*;
import static org.forbidec.domain.MatiereTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatiereMapperTest {

    private MatiereMapper matiereMapper;

    @BeforeEach
    void setUp() {
        matiereMapper = new MatiereMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMatiereSample1();
        var actual = matiereMapper.toEntity(matiereMapper.toDto(expected));
        assertMatiereAllPropertiesEquals(expected, actual);
    }
}
