package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InscriptionCycleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionCycleDTO.class);
        InscriptionCycleDTO inscriptionCycleDTO1 = new InscriptionCycleDTO();
        inscriptionCycleDTO1.setId(1L);
        InscriptionCycleDTO inscriptionCycleDTO2 = new InscriptionCycleDTO();
        assertThat(inscriptionCycleDTO1).isNotEqualTo(inscriptionCycleDTO2);
        inscriptionCycleDTO2.setId(inscriptionCycleDTO1.getId());
        assertThat(inscriptionCycleDTO1).isEqualTo(inscriptionCycleDTO2);
        inscriptionCycleDTO2.setId(2L);
        assertThat(inscriptionCycleDTO1).isNotEqualTo(inscriptionCycleDTO2);
        inscriptionCycleDTO1.setId(null);
        assertThat(inscriptionCycleDTO1).isNotEqualTo(inscriptionCycleDTO2);
    }
}
