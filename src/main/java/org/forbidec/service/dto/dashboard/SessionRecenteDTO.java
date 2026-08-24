package org.forbidec.service.dto.dashboard;

import java.io.Serializable;

public class SessionRecenteDTO implements Serializable {

    private Long id;
    private Integer annee;
    private String libelle;
    private Boolean cloture;
    private String centreCode;
    private String centreNom;
    private Long nbEtudiants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getCloture() {
        return cloture;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
    }

    public String getCentreCode() {
        return centreCode;
    }

    public void setCentreCode(String centreCode) {
        this.centreCode = centreCode;
    }

    public String getCentreNom() {
        return centreNom;
    }

    public void setCentreNom(String centreNom) {
        this.centreNom = centreNom;
    }

    public Long getNbEtudiants() {
        return nbEtudiants;
    }

    public void setNbEtudiants(Long nbEtudiants) {
        this.nbEtudiants = nbEtudiants;
    }
}
