package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CentreFormationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CentreFormationDTO.class);
        CentreFormationDTO centreFormationDTO1 = new CentreFormationDTO();
        centreFormationDTO1.setId(1L);
        CentreFormationDTO centreFormationDTO2 = new CentreFormationDTO();
        assertThat(centreFormationDTO1).isNotEqualTo(centreFormationDTO2);
        centreFormationDTO2.setId(centreFormationDTO1.getId());
        assertThat(centreFormationDTO1).isEqualTo(centreFormationDTO2);
        centreFormationDTO2.setId(2L);
        assertThat(centreFormationDTO1).isNotEqualTo(centreFormationDTO2);
        centreFormationDTO1.setId(null);
        assertThat(centreFormationDTO1).isNotEqualTo(centreFormationDTO2);
    }
}
