package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.Etudiant} entity.
 */
@Schema(
    description = "Étudiant.\nmatricule remplace le triplet (nom, prénom, particularité) comme clé\nmétier : la base contient 5 homonymes stricts.\nanneeEntree et cursusAcheve sont dérivés de champs texte Access\n(« Se - 2014 », « Finaliste en 2022 » — 165 étudiants concernés)."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtudiantDTO implements Serializable {

    private Long id;

    @Size(max = 30)
    private String matricule;

    @NotNull
    @Size(max = 80)
    private String nom;

    @NotNull
    @Size(max = 80)
    private String prenom;

    @Size(max = 80)
    private String particularite;

    private LocalDate dateNaissance;

    @Size(max = 150)
    private String email;

    @Size(max = 30)
    private String telephone;

    @Min(value = 1900)
    @Max(value = 2200)
    private Integer anneeEntree;

    @NotNull
    private Boolean cursusAcheve;

    @Min(value = 1900)
    @Max(value = 2200)
    private Integer anneeFinale;

    @Size(max = 64)
    private String keycloakUserId;

    @Size(max = 255)
    private String commentaire;

    @NotNull
    private Boolean actif;

    private PaysDTO pays;

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

    public String getParticularite() {
        return particularite;
    }

    public void setParticularite(String particularite) {
        this.particularite = particularite;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
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

    public Integer getAnneeEntree() {
        return anneeEntree;
    }

    public void setAnneeEntree(Integer anneeEntree) {
        this.anneeEntree = anneeEntree;
    }

    public Boolean getCursusAcheve() {
        return cursusAcheve;
    }

    public void setCursusAcheve(Boolean cursusAcheve) {
        this.cursusAcheve = cursusAcheve;
    }

    public Integer getAnneeFinale() {
        return anneeFinale;
    }

    public void setAnneeFinale(Integer anneeFinale) {
        this.anneeFinale = anneeFinale;
    }

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
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

    public PaysDTO getPays() {
        return pays;
    }

    public void setPays(PaysDTO pays) {
        this.pays = pays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtudiantDTO)) {
            return false;
        }

        EtudiantDTO etudiantDTO = (EtudiantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, etudiantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtudiantDTO{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", particularite='" + getParticularite() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", anneeEntree=" + getAnneeEntree() +
            ", cursusAcheve='" + getCursusAcheve() + "'" +
            ", anneeFinale=" + getAnneeFinale() +
            ", keycloakUserId='" + getKeycloakUserId() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            ", pays=" + getPays() +
            "}";
    }
}
