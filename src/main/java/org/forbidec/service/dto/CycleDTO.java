package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.Cycle} entity.
 */
@Schema(description = "Promotion annuelle d'un centre. cloture verrouille toute saisie.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CycleDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2200)
    private Integer annee;

    @Size(max = 100)
    private String libelle;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @NotNull
    private Boolean cloture;

    @Size(max = 255)
    private String commentaire;

    @NotNull
    private CentreFormationDTO centre;

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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public CentreFormationDTO getCentre() {
        return centre;
    }

    public void setCentre(CentreFormationDTO centre) {
        this.centre = centre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CycleDTO)) {
            return false;
        }

        CycleDTO cycleDTO = (CycleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cycleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleDTO{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", libelle='" + getLibelle() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", cloture='" + getCloture() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", centre=" + getCentre() +
            "}";
    }
}
