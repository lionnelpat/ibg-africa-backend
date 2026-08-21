package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.CentreFormation} entity.
 */
@Schema(
    description = "Centre de formation. Porte les blocs d'impression du bulletin\n(en-tête, ville de signature, signataire) et les règles pédagogiques\nlocales : nombre de cycles du cursus, note maximale."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CentreFormationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String code;

    @NotNull
    @Size(max = 150)
    private String nom;

    @NotNull
    @Size(max = 100)
    private String ville;

    @Size(max = 255)
    private String adresse;

    @Lob
    private String enteteDocument;

    @NotNull
    @Size(max = 100)
    private String signataire;

    @Size(max = 255)
    private String logoUrl;

    @NotNull
    @Min(value = 1)
    @Max(value = 20)
    private Integer nbCyclesCursus;

    @NotNull
    @DecimalMin(value = "1")
    private BigDecimal noteMaximale;

    @NotNull
    private Boolean actif;

    @NotNull
    private PaysDTO pays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEnteteDocument() {
        return enteteDocument;
    }

    public void setEnteteDocument(String enteteDocument) {
        this.enteteDocument = enteteDocument;
    }

    public String getSignataire() {
        return signataire;
    }

    public void setSignataire(String signataire) {
        this.signataire = signataire;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getNbCyclesCursus() {
        return nbCyclesCursus;
    }

    public void setNbCyclesCursus(Integer nbCyclesCursus) {
        this.nbCyclesCursus = nbCyclesCursus;
    }

    public BigDecimal getNoteMaximale() {
        return noteMaximale;
    }

    public void setNoteMaximale(BigDecimal noteMaximale) {
        this.noteMaximale = noteMaximale;
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
        if (!(o instanceof CentreFormationDTO)) {
            return false;
        }

        CentreFormationDTO centreFormationDTO = (CentreFormationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, centreFormationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentreFormationDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", ville='" + getVille() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", enteteDocument='" + getEnteteDocument() + "'" +
            ", signataire='" + getSignataire() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", nbCyclesCursus=" + getNbCyclesCursus() +
            ", noteMaximale=" + getNoteMaximale() +
            ", actif='" + getActif() + "'" +
            ", pays=" + getPays() +
            "}";
    }
}
