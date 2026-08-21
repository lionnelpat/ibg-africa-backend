package org.forbidec.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.Matiere} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MatiereDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String intitule;

    @Size(max = 100)
    private String libelleLong;

    @Size(max = 50)
    private String libelleCourt;

    @Size(max = 255)
    private String commentaire;

    @NotNull
    private Boolean actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatiereDTO)) {
            return false;
        }

        MatiereDTO matiereDTO = (MatiereDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matiereDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatiereDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
