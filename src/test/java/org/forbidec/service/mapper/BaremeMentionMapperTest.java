package org.forbidec.service.mapper;

import static org.forbidec.domain.BaremeMentionAsserts.*;
import static org.forbidec.domain.BaremeMentionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaremeMentionMapperTest {

    private BaremeMentionMapper baremeMentionMapper;

    @BeforeEach
    void setUp() {
        baremeMentionMapper = new BaremeMentionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBaremeMentionSample1();
        var actual = baremeMentionMapper.toEntity(baremeMentionMapper.toDto(expected));
        assertBaremeMentionAllPropertiesEquals(expected, actual);
    }
}
