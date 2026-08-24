package org.forbidec.service.dto.bulletin;

import java.io.Serializable;

/**
 * Identité minimale d'un étudiant, pour les listes (inscrits à un cycle...).
 */
public class EtudiantResumeDTO implements Serializable {

    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private Boolean actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}
