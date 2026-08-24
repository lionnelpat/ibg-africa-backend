package org.forbidec.repository.bulletin;

import java.math.BigDecimal;

/**
 * Une évaluation réalisée comptant dans la moyenne, projetée avec juste ce
 * qu'il faut pour calculer le bulletin (regroupement cycle/cours en Java,
 * cf. {@link org.forbidec.service.BulletinService}).
 */
public interface EvaluationLigneProjection {
    Long getCycleId();
    Integer getCycleAnnee();
    Long getCoursId();
    String getCoursIntitule();
    Integer getCoursOrdreAffichage();
    BigDecimal getCoefficient();
    BigDecimal getNote();
}
