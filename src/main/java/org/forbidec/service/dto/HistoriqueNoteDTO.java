package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import org.forbidec.domain.enumeration.StatutNote;

/**
 * A DTO for the {@link org.forbidec.domain.HistoriqueNote} entity.
 */
@Schema(description = "Journal des modifications de note. Absent d'Access, requis en multi-utilisateur.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueNoteDTO implements Serializable {

    private Long id;

    private BigDecimal noteAvant;

    private BigDecimal noteApres;

    private StatutNote statutAvant;

    private StatutNote statutApres;

    @Size(max = 255)
    private String motif;

    @NotNull
    @Size(max = 64)
    private String modifiePar;

    @NotNull
    private Instant modifieLe;

    @NotNull
    private EvaluationRealiseeDTO evaluationRealisee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNoteAvant() {
        return noteAvant;
    }

    public void setNoteAvant(BigDecimal noteAvant) {
        this.noteAvant = noteAvant;
    }

    public BigDecimal getNoteApres() {
        return noteApres;
    }

    public void setNoteApres(BigDecimal noteApres) {
        this.noteApres = noteApres;
    }

    public StatutNote getStatutAvant() {
        return statutAvant;
    }

    public void setStatutAvant(StatutNote statutAvant) {
        this.statutAvant = statutAvant;
    }

    public StatutNote getStatutApres() {
        return statutApres;
    }

    public void setStatutApres(StatutNote statutApres) {
        this.statutApres = statutApres;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getModifiePar() {
        return modifiePar;
    }

    public void setModifiePar(String modifiePar) {
        this.modifiePar = modifiePar;
    }

    public Instant getModifieLe() {
        return modifieLe;
    }

    public void setModifieLe(Instant modifieLe) {
        this.modifieLe = modifieLe;
    }

    public EvaluationRealiseeDTO getEvaluationRealisee() {
        return evaluationRealisee;
    }

    public void setEvaluationRealisee(EvaluationRealiseeDTO evaluationRealisee) {
        this.evaluationRealisee = evaluationRealisee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueNoteDTO)) {
            return false;
        }

        HistoriqueNoteDTO historiqueNoteDTO = (HistoriqueNoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiqueNoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueNoteDTO{" +
            "id=" + getId() +
            ", noteAvant=" + getNoteAvant() +
            ", noteApres=" + getNoteApres() +
            ", statutAvant='" + getStatutAvant() + "'" +
            ", statutApres='" + getStatutApres() + "'" +
            ", motif='" + getMotif() + "'" +
            ", modifiePar='" + getModifiePar() + "'" +
            ", modifieLe='" + getModifieLe() + "'" +
            ", evaluationRealisee=" + getEvaluationRealisee() +
            "}";
    }
}
