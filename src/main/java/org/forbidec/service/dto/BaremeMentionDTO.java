package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.BaremeMention} entity.
 */
@Schema(
    description = "Barème des mentions. Reconstitué depuis le pied de page du bulletin :\n[19-20] Excellent · [17-19[ Très-Bien · [15-17[ Bien\n[12-15[ Assez-Bien · [10-12[ Suffisant · <10 Insuffisant\nRattaché à un centre si celui-ci applique un barème propre ;\nsans rattachement, le barème est commun à tous les centres."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaremeMentionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String libelleLong;

    @NotNull
    @Size(max = 50)
    private String libelleCourt;

    @DecimalMin(value = "0")
    private BigDecimal borneMin;

    @NotNull
    private Boolean minInclus;

    @DecimalMin(value = "0")
    private BigDecimal borneMax;

    @NotNull
    private Boolean maxInclus;

    @NotNull
    private Integer ordreAffichage;

    @Size(max = 255)
    private String commentaire;

    @NotNull
    private Boolean actif;

    private CentreFormationDTO centre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getBorneMin() {
        return borneMin;
    }

    public void setBorneMin(BigDecimal borneMin) {
        this.borneMin = borneMin;
    }

    public Boolean getMinInclus() {
        return minInclus;
    }

    public void setMinInclus(Boolean minInclus) {
        this.minInclus = minInclus;
    }

    public BigDecimal getBorneMax() {
        return borneMax;
    }

    public void setBorneMax(BigDecimal borneMax) {
        this.borneMax = borneMax;
    }

    public Boolean getMaxInclus() {
        return maxInclus;
    }

    public void setMaxInclus(Boolean maxInclus) {
        this.maxInclus = maxInclus;
    }

    public Integer getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
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
        if (!(o instanceof BaremeMentionDTO)) {
            return false;
        }

        BaremeMentionDTO baremeMentionDTO = (BaremeMentionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, baremeMentionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaremeMentionDTO{" +
            "id=" + getId() +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", borneMin=" + getBorneMin() +
            ", minInclus='" + getMinInclus() + "'" +
            ", borneMax=" + getBorneMax() +
            ", maxInclus='" + getMaxInclus() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            ", centre=" + getCentre() +
            "}";
    }
}
