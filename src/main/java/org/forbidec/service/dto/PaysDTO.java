package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.Pays} entity.
 */
@Schema(description = "Pays d'implantation d'un ou plusieurs centres de formation.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaysDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 2)
    private String codeIso;

    @NotNull
    @Size(max = 100)
    private String nom;

    @NotNull
    @Size(max = 5)
    private String langue;

    @Size(max = 50)
    private String fuseau;

    @NotNull
    private Boolean actif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeIso() {
        return codeIso;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getFuseau() {
        return fuseau;
    }

    public void setFuseau(String fuseau) {
        this.fuseau = fuseau;
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
        if (!(o instanceof PaysDTO)) {
            return false;
        }

        PaysDTO paysDTO = (PaysDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paysDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaysDTO{" +
            "id=" + getId() +
            ", codeIso='" + getCodeIso() + "'" +
            ", nom='" + getNom() + "'" +
            ", langue='" + getLangue() + "'" +
            ", fuseau='" + getFuseau() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
