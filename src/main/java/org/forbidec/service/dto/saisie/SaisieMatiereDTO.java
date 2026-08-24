package org.forbidec.service.dto.saisie;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** Contexte + grille de saisie pour une matière (EvaluationPrevue) d'un cycle. */
public class SaisieMatiereDTO implements Serializable {

    private Long evaluationPrevueId;
    private String intitule;
    private String coursIntitule;
    private BigDecimal coefficient;
    private BigDecimal noteMaximale;
    private Long cycleId;
    private Integer cycleAnnee;
    private List<SaisieLigneDTO> lignes;

    public Long getEvaluationPrevueId() {
        return evaluationPrevueId;
    }

    public void setEvaluationPrevueId(Long evaluationPrevueId) {
        this.evaluationPrevueId = evaluationPrevueId;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCoursIntitule() {
        return coursIntitule;
    }

    public void setCoursIntitule(String coursIntitule) {
        this.coursIntitule = coursIntitule;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public BigDecimal getNoteMaximale() {
        return noteMaximale;
    }

    public void setNoteMaximale(BigDecimal noteMaximale) {
        this.noteMaximale = noteMaximale;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getCycleAnnee() {
        return cycleAnnee;
    }

    public void setCycleAnnee(Integer cycleAnnee) {
        this.cycleAnnee = cycleAnnee;
    }

    public List<SaisieLigneDTO> getLignes() {
        return lignes;
    }

    public void setLignes(List<SaisieLigneDTO> lignes) {
        this.lignes = lignes;
    }
}
