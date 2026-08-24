package org.forbidec.service.dto.saisie;

import java.io.Serializable;
import java.math.BigDecimal;
import org.forbidec.domain.enumeration.StatutNote;

/** Une ligne de la grille de saisie : un étudiant, sa note actuelle (si déjà saisie). */
public class SaisieLigneDTO implements Serializable {

    private Long etudiantId;
    private String matricule;
    private String nom;
    private String prenom;
    private Long evaluationRealiseeId;
    private BigDecimal note;
    private StatutNote statut;

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Long getEvaluationRealiseeId() {
        return evaluationRealiseeId;
    }

    public void setEvaluationRealiseeId(Long evaluationRealiseeId) {
        this.evaluationRealiseeId = evaluationRealiseeId;
    }

    public BigDecimal getNote() {
        return note;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public StatutNote getStatut() {
        return statut;
    }

    public void setStatut(StatutNote statut) {
        this.statut = statut;
    }
}
