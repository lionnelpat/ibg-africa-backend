package org.forbidec.service.dto.bulletin;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Vue détaillée d'un cycle : contexte (année, centre, pays), matières
 * dispensées avec leur enseignant, et étudiants inscrits.
 */
public class CycleDetailDTO implements Serializable {

    private Long id;
    private Integer annee;
    private String libelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Boolean cloture;

    private Long centreId;
    private String centreCode;
    private String centreNom;
    private String centreVille;

    private String paysNom;

    private List<MatiereDispenseeDTO> matieresDispensees;
    private List<EtudiantResumeDTO> etudiants;

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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getCloture() {
        return cloture;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
    }

    public Long getCentreId() {
        return centreId;
    }

    public void setCentreId(Long centreId) {
        this.centreId = centreId;
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

    public String getCentreVille() {
        return centreVille;
    }

    public void setCentreVille(String centreVille) {
        this.centreVille = centreVille;
    }

    public String getPaysNom() {
        return paysNom;
    }

    public void setPaysNom(String paysNom) {
        this.paysNom = paysNom;
    }

    public List<MatiereDispenseeDTO> getMatieresDispensees() {
        return matieresDispensees;
    }

    public void setMatieresDispensees(List<MatiereDispenseeDTO> matieresDispensees) {
        this.matieresDispensees = matieresDispensees;
    }

    public List<EtudiantResumeDTO> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<EtudiantResumeDTO> etudiants) {
        this.etudiants = etudiants;
    }
}
