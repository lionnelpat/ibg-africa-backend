package org.forbidec.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forbidec.domain.BaremeMentionTestSamples.*;
import static org.forbidec.domain.CentreFormationTestSamples.*;

import org.forbidec.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BaremeMentionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BaremeMention.class);
        BaremeMention baremeMention1 = getBaremeMentionSample1();
        BaremeMention baremeMention2 = new BaremeMention();
        assertThat(baremeMention1).isNotEqualTo(baremeMention2);

        baremeMention2.setId(baremeMention1.getId());
        assertThat(baremeMention1).isEqualTo(baremeMention2);

        baremeMention2 = getBaremeMentionSample2();
        assertThat(baremeMention1).isNotEqualTo(baremeMention2);
    }

    @Test
    void centreTest() {
        BaremeMention baremeMention = getBaremeMentionRandomSampleGenerator();
        CentreFormation centreFormationBack = getCentreFormationRandomSampleGenerator();

        baremeMention.setCentre(centreFormationBack);
        assertThat(baremeMention.getCentre()).isEqualTo(centreFormationBack);

        baremeMention.centre(null);
        assertThat(baremeMention.getCentre()).isNull();
    }
}
