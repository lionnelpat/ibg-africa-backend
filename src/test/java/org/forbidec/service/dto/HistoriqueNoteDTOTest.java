package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HistoriqueNoteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoriqueNoteDTO.class);
        HistoriqueNoteDTO historiqueNoteDTO1 = new HistoriqueNoteDTO();
        historiqueNoteDTO1.setId(1L);
        HistoriqueNoteDTO historiqueNoteDTO2 = new HistoriqueNoteDTO();
        assertThat(historiqueNoteDTO1).isNotEqualTo(historiqueNoteDTO2);
        historiqueNoteDTO2.setId(historiqueNoteDTO1.getId());
        assertThat(historiqueNoteDTO1).isEqualTo(historiqueNoteDTO2);
        historiqueNoteDTO2.setId(2L);
        assertThat(historiqueNoteDTO1).isNotEqualTo(historiqueNoteDTO2);
        historiqueNoteDTO1.setId(null);
        assertThat(historiqueNoteDTO1).isNotEqualTo(historiqueNoteDTO2);
    }
}
