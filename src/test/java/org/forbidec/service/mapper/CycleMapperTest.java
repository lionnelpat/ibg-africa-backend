package org.forbidec.service.mapper;

import static org.forbidec.domain.CycleAsserts.*;
import static org.forbidec.domain.CycleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CycleMapperTest {

    private CycleMapper cycleMapper;

    @BeforeEach
    void setUp() {
        cycleMapper = new CycleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCycleSample1();
        var actual = cycleMapper.toEntity(cycleMapper.toDto(expected));
        assertCycleAllPropertiesEquals(expected, actual);
    }
}
