package org.forbidec.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EvaluationPrevueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EvaluationPrevueDTO.class);
        EvaluationPrevueDTO evaluationPrevueDTO1 = new EvaluationPrevueDTO();
        evaluationPrevueDTO1.setId(1L);
        EvaluationPrevueDTO evaluationPrevueDTO2 = new EvaluationPrevueDTO();
        assertThat(evaluationPrevueDTO1).isNotEqualTo(evaluationPrevueDTO2);
        evaluationPrevueDTO2.setId(evaluationPrevueDTO1.getId());
        assertThat(evaluationPrevueDTO1).isEqualTo(evaluationPrevueDTO2);
        evaluationPrevueDTO2.setId(2L);
        assertThat(evaluationPrevueDTO1).isNotEqualTo(evaluationPrevueDTO2);
        evaluationPrevueDTO1.setId(null);
        assertThat(evaluationPrevueDTO1).isNotEqualTo(evaluationPrevueDTO2);
    }
}
