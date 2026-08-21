package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.TypeTache} entity.
 */
@Schema(
    description = "Type d'évaluation. Le code technique évite de dépendre du libellé :\nles requêtes Access filtraient sur la chaîne « Académique »."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeTacheDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String code;

    @NotNull
    @Size(max = 100)
    private String intitule;

    @Size(max = 100)
    private String libelleLong;

    @Size(max = 50)
    private String libelleCourt;

    @NotNull
    private Boolean entreDansMoyenne;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Boolean getEntreDansMoyenne() {
        return entreDansMoyenne;
    }

    public void setEntreDansMoyenne(Boolean entreDansMoyenne) {
        this.entreDansMoyenne = entreDansMoyenne;
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
        if (!(o instanceof TypeTacheDTO)) {
            return false;
        }

        TypeTacheDTO typeTacheDTO = (TypeTacheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeTacheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeTacheDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", intitule='" + getIntitule() + "'" +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", entreDansMoyenne='" + getEntreDansMoyenne() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
