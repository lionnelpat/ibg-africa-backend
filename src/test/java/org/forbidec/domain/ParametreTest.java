package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.CentreFormationTestSamples.*;
import static org.forbidec.domain.ParametreTestSamples.*;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParametreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parametre.class);
        Parametre parametre1 = getParametreSample1();
        Parametre parametre2 = new Parametre();
        assertThat(parametre1).isNotEqualTo(parametre2);

        parametre2.setId(parametre1.getId());
        assertThat(parametre1).isEqualTo(parametre2);

        parametre2 = getParametreSample2();
        assertThat(parametre1).isNotEqualTo(parametre2);
    }

    @Test
    void centreTest() {
        Parametre parametre = getParametreRandomSampleGenerator();
        CentreFormation centreFormationBack = getCentreFormationRandomSampleGenerator();

        parametre.setCentre(centreFormationBack);
        assertThat(parametre.getCentre()).isEqualTo(centreFormationBack);

        parametre.centre(null);
        assertThat(parametre.getCentre()).isNull();
    }
}
