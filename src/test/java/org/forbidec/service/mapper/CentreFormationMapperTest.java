package org.forbidec.service.mapper;

import static org.forbidec.domain.CentreFormationAsserts.*;
import static org.forbidec.domain.CentreFormationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CentreFormationMapperTest {

    private CentreFormationMapper centreFormationMapper;

    @BeforeEach
    void setUp() {
        centreFormationMapper = new CentreFormationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCentreFormationSample1();
        var actual = centreFormationMapper.toEntity(centreFormationMapper.toDto(expected));
        assertCentreFormationAllPropertiesEquals(expected, actual);
    }
}
