package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import org.forbidec.domain.enumeration.StatutNote;

/**
 * A DTO for the {@link org.forbidec.domain.EvaluationRealisee} entity.
 */
@Schema(
    description = "Note d'un étudiant sur une évaluation.\nnote nullable + statut : NON_SAISIE et une note de 0 sont enfin\ndeux choses différentes."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationRealiseeDTO implements Serializable {

    private Long id;

    @DecimalMin(value = "0")
    private BigDecimal note;

    @NotNull
    private StatutNote statut;

    @NotNull
    private Boolean compteDansMoyenne;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Size(max = 255)
    private String commentaire1;

    @Size(max = 255)
    private String commentaire2;

    @Size(max = 255)
    private String commentaire3;

    @Size(max = 64)
    private String saisiePar;

    private Instant saisieLe;

    @Size(max = 64)
    private String valideePar;

    private Instant valideeLe;

    @NotNull
    private EvaluationPrevueDTO evaluationPrevue;

    @NotNull
    private EtudiantDTO etudiant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNote() {
        return note;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public StatutNote getStatut() {
        return statut;
    }

    public void setStatut(StatutNote statut) {
        this.statut = statut;
    }

    public Boolean getCompteDansMoyenne() {
        return compteDansMoyenne;
    }

    public void setCompteDansMoyenne(Boolean compteDansMoyenne) {
        this.compteDansMoyenne = compteDansMoyenne;
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

    public String getCommentaire1() {
        return commentaire1;
    }

    public void setCommentaire1(String commentaire1) {
        this.commentaire1 = commentaire1;
    }

    public String getCommentaire2() {
        return commentaire2;
    }

    public void setCommentaire2(String commentaire2) {
        this.commentaire2 = commentaire2;
    }

    public String getCommentaire3() {
        return commentaire3;
    }

    public void setCommentaire3(String commentaire3) {
        this.commentaire3 = commentaire3;
    }

    public String getSaisiePar() {
        return saisiePar;
    }

    public void setSaisiePar(String saisiePar) {
        this.saisiePar = saisiePar;
    }

    public Instant getSaisieLe() {
        return saisieLe;
    }

    public void setSaisieLe(Instant saisieLe) {
        this.saisieLe = saisieLe;
    }

    public String getValideePar() {
        return valideePar;
    }

    public void setValideePar(String valideePar) {
        this.valideePar = valideePar;
    }

    public Instant getValideeLe() {
        return valideeLe;
    }

    public void setValideeLe(Instant valideeLe) {
        this.valideeLe = valideeLe;
    }

    public EvaluationPrevueDTO getEvaluationPrevue() {
        return evaluationPrevue;
    }

    public void setEvaluationPrevue(EvaluationPrevueDTO evaluationPrevue) {
        this.evaluationPrevue = evaluationPrevue;
    }

    public EtudiantDTO getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EtudiantDTO etudiant) {
        this.etudiant = etudiant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationRealiseeDTO)) {
            return false;
        }

        EvaluationRealiseeDTO evaluationRealiseeDTO = (EvaluationRealiseeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evaluationRealiseeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationRealiseeDTO{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", statut='" + getStatut() + "'" +
            ", compteDansMoyenne='" + getCompteDansMoyenne() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", commentaire1='" + getCommentaire1() + "'" +
            ", commentaire2='" + getCommentaire2() + "'" +
            ", commentaire3='" + getCommentaire3() + "'" +
            ", saisiePar='" + getSaisiePar() + "'" +
            ", saisieLe='" + getSaisieLe() + "'" +
            ", valideePar='" + getValideePar() + "'" +
            ", valideeLe='" + getValideeLe() + "'" +
            ", evaluationPrevue=" + getEvaluationPrevue() +
            ", etudiant=" + getEtudiant() +
            "}";
    }
}
