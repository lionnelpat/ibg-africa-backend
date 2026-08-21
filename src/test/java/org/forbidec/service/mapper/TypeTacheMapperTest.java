package org.forbidec.service.mapper;

import static org.forbidec.domain.TypeTacheAsserts.*;
import static org.forbidec.domain.TypeTacheTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeTacheMapperTest {

    private TypeTacheMapper typeTacheMapper;

    @BeforeEach
    void setUp() {
        typeTacheMapper = new TypeTacheMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTypeTacheSample1();
        var actual = typeTacheMapper.toEntity(typeTacheMapper.toDto(expected));
        assertTypeTacheAllPropertiesEquals(expected, actual);
    }
}
