package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaremeMentionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaremeMentionDTO.class);
        BaremeMentionDTO baremeMentionDTO1 = new BaremeMentionDTO();
        baremeMentionDTO1.setId(1L);
        BaremeMentionDTO baremeMentionDTO2 = new BaremeMentionDTO();
        assertThat(baremeMentionDTO1).isNotEqualTo(baremeMentionDTO2);
        baremeMentionDTO2.setId(baremeMentionDTO1.getId());
        assertThat(baremeMentionDTO1).isEqualTo(baremeMentionDTO2);
        baremeMentionDTO2.setId(2L);
        assertThat(baremeMentionDTO1).isNotEqualTo(baremeMentionDTO2);
        baremeMentionDTO1.setId(null);
        assertThat(baremeMentionDTO1).isNotEqualTo(baremeMentionDTO2);
    }
}
