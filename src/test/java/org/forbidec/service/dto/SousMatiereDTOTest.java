package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousMatiereDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousMatiereDTO.class);
        SousMatiereDTO sousMatiereDTO1 = new SousMatiereDTO();
        sousMatiereDTO1.setId(1L);
        SousMatiereDTO sousMatiereDTO2 = new SousMatiereDTO();
        assertThat(sousMatiereDTO1).isNotEqualTo(sousMatiereDTO2);
        sousMatiereDTO2.setId(sousMatiereDTO1.getId());
        assertThat(sousMatiereDTO1).isEqualTo(sousMatiereDTO2);
        sousMatiereDTO2.setId(2L);
        assertThat(sousMatiereDTO1).isNotEqualTo(sousMatiereDTO2);
        sousMatiereDTO1.setId(null);
        assertThat(sousMatiereDTO1).isNotEqualTo(sousMatiereDTO2);
    }
}
