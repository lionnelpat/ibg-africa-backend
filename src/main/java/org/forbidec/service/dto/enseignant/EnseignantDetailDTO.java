package org.forbidec.service.dto.enseignant;

import java.io.Serializable;
import java.util.List;

/**
 * Fiche détail d'un enseignant : identité + les matières dispensées,
 * groupées par cycle.
 */
public class EnseignantDetailDTO implements Serializable {

    private Long id;
    private String nom;
    private String prenom;
    private String libelleLong;
    private String libelleCourt;
    private String email;
    private String telephone;
    private String commentaire;
    private Boolean actif;
    private byte[] photo;
    private String photoContentType;
    private List<CycleEnseignementDTO> coursParCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public List<CycleEnseignementDTO> getCoursParCycle() {
        return coursParCycle;
    }

    public void setCoursParCycle(List<CycleEnseignementDTO> coursParCycle) {
        this.coursParCycle = coursParCycle;
    }
}
