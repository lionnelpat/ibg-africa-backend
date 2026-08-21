package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnseignantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnseignantDTO.class);
        EnseignantDTO enseignantDTO1 = new EnseignantDTO();
        enseignantDTO1.setId(1L);
        EnseignantDTO enseignantDTO2 = new EnseignantDTO();
        assertThat(enseignantDTO1).isNotEqualTo(enseignantDTO2);
        enseignantDTO2.setId(enseignantDTO1.getId());
        assertThat(enseignantDTO1).isEqualTo(enseignantDTO2);
        enseignantDTO2.setId(2L);
        assertThat(enseignantDTO1).isNotEqualTo(enseignantDTO2);
        enseignantDTO1.setId(null);
        assertThat(enseignantDTO1).isNotEqualTo(enseignantDTO2);
    }
}
