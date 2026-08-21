package org.forbidec.service.mapper;

import static org.forbidec.domain.HistoriqueNoteAsserts.*;
import static org.forbidec.domain.HistoriqueNoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoriqueNoteMapperTest {

    private HistoriqueNoteMapper historiqueNoteMapper;

    @BeforeEach
    void setUp() {
        historiqueNoteMapper = new HistoriqueNoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHistoriqueNoteSample1();
        var actual = historiqueNoteMapper.toEntity(historiqueNoteMapper.toDto(expected));
        assertHistoriqueNoteAllPropertiesEquals(expected, actual);
    }
}
