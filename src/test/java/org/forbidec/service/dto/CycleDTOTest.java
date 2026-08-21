package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CycleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CycleDTO.class);
        CycleDTO cycleDTO1 = new CycleDTO();
        cycleDTO1.setId(1L);
        CycleDTO cycleDTO2 = new CycleDTO();
        assertThat(cycleDTO1).isNotEqualTo(cycleDTO2);
        cycleDTO2.setId(cycleDTO1.getId());
        assertThat(cycleDTO1).isEqualTo(cycleDTO2);
        cycleDTO2.setId(2L);
        assertThat(cycleDTO1).isNotEqualTo(cycleDTO2);
        cycleDTO1.setId(null);
        assertThat(cycleDTO1).isNotEqualTo(cycleDTO2);
    }
}
