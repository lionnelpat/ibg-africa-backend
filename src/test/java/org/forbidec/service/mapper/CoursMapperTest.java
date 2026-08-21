package org.forbidec.service.mapper;

import static org.forbidec.domain.CoursAsserts.*;
import static org.forbidec.domain.CoursTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoursMapperTest {

    private CoursMapper coursMapper;

    @BeforeEach
    void setUp() {
        coursMapper = new CoursMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCoursSample1();
        var actual = coursMapper.toEntity(coursMapper.toDto(expected));
        assertCoursAllPropertiesEquals(expected, actual);
    }
}
