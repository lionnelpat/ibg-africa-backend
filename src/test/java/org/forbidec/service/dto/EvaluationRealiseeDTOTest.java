package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaluationRealiseeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationRealiseeDTO.class);
        EvaluationRealiseeDTO evaluationRealiseeDTO1 = new EvaluationRealiseeDTO();
        evaluationRealiseeDTO1.setId(1L);
        EvaluationRealiseeDTO evaluationRealiseeDTO2 = new EvaluationRealiseeDTO();
        assertThat(evaluationRealiseeDTO1).isNotEqualTo(evaluationRealiseeDTO2);
        evaluationRealiseeDTO2.setId(evaluationRealiseeDTO1.getId());
        assertThat(evaluationRealiseeDTO1).isEqualTo(evaluationRealiseeDTO2);
        evaluationRealiseeDTO2.setId(2L);
        assertThat(evaluationRealiseeDTO1).isNotEqualTo(evaluationRealiseeDTO2);
        evaluationRealiseeDTO1.setId(null);
        assertThat(evaluationRealiseeDTO1).isNotEqualTo(evaluationRealiseeDTO2);
    }
}
