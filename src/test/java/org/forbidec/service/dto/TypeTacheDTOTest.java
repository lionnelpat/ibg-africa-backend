package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeTacheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeTacheDTO.class);
        TypeTacheDTO typeTacheDTO1 = new TypeTacheDTO();
        typeTacheDTO1.setId(1L);
        TypeTacheDTO typeTacheDTO2 = new TypeTacheDTO();
        assertThat(typeTacheDTO1).isNotEqualTo(typeTacheDTO2);
        typeTacheDTO2.setId(typeTacheDTO1.getId());
        assertThat(typeTacheDTO1).isEqualTo(typeTacheDTO2);
        typeTacheDTO2.setId(2L);
        assertThat(typeTacheDTO1).isNotEqualTo(typeTacheDTO2);
        typeTacheDTO1.setId(null);
        assertThat(typeTacheDTO1).isNotEqualTo(typeTacheDTO2);
    }
}
