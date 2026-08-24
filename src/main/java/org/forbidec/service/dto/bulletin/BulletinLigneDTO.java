package org.forbidec.service.dto.bulletin;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Une ligne du bulletin : la moyenne obtenue par l'étudiant sur un cours,
 * pour un cycle donné (cf. v_bulletin_ligne dans le script de migration).
 */
public class BulletinLigneDTO implements Serializable {

    private Integer cycleAnnee;
    private String coursIntitule;
    private BigDecimal moyenneCours;
    private String mentionLongue;
    private String mentionCourte;

    public Integer getCycleAnnee() {
        return cycleAnnee;
    }

    public void setCycleAnnee(Integer cycleAnnee) {
        this.cycleAnnee = cycleAnnee;
    }

    public String getCoursIntitule() {
        return coursIntitule;
    }

    public void setCoursIntitule(String coursIntitule) {
        this.coursIntitule = coursIntitule;
    }

    public BigDecimal getMoyenneCours() {
        return moyenneCours;
    }

    public void setMoyenneCours(BigDecimal moyenneCours) {
        this.moyenneCours = moyenneCours;
    }

    public String getMentionLongue() {
        return mentionLongue;
    }

    public void setMentionLongue(String mentionLongue) {
        this.mentionLongue = mentionLongue;
    }

    public String getMentionCourte() {
        return mentionCourte;
    }

    public void setMentionCourte(String mentionCourte) {
        this.mentionCourte = mentionCourte;
    }
}
