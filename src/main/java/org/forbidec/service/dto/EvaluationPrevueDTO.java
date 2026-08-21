package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.EvaluationPrevue} entity.
 */
@Schema(description = "Évaluation planifiée pour un cycle : le « quoi » et le « par qui ».")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationPrevueDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String intitule;

    @NotNull
    @Size(max = 150)
    private String libelleImpression;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal coefficient;

    @NotNull
    private Boolean compteDansMoyenne;

    @NotNull
    @DecimalMin(value = "1")
    private BigDecimal noteMaximale;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Size(max = 255)
    private String commentaire;

    @NotNull
    private CycleDTO cycle;

    @NotNull
    private EnseignantDTO enseignant;

    @NotNull
    private MatiereDTO matiere;

    @NotNull
    private SousMatiereDTO sousMatiere;

    @NotNull
    private CoursDTO cours;

    @NotNull
    private TypeTacheDTO typeTache;

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

    public String getLibelleImpression() {
        return libelleImpression;
    }

    public void setLibelleImpression(String libelleImpression) {
        this.libelleImpression = libelleImpression;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public Boolean getCompteDansMoyenne() {
        return compteDansMoyenne;
    }

    public void setCompteDansMoyenne(Boolean compteDansMoyenne) {
        this.compteDansMoyenne = compteDansMoyenne;
    }

    public BigDecimal getNoteMaximale() {
        return noteMaximale;
    }

    public void setNoteMaximale(BigDecimal noteMaximale) {
        this.noteMaximale = noteMaximale;
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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public CycleDTO getCycle() {
        return cycle;
    }

    public void setCycle(CycleDTO cycle) {
        this.cycle = cycle;
    }

    public EnseignantDTO getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(EnseignantDTO enseignant) {
        this.enseignant = enseignant;
    }

    public MatiereDTO getMatiere() {
        return matiere;
    }

    public void setMatiere(MatiereDTO matiere) {
        this.matiere = matiere;
    }

    public SousMatiereDTO getSousMatiere() {
        return sousMatiere;
    }

    public void setSousMatiere(SousMatiereDTO sousMatiere) {
        this.sousMatiere = sousMatiere;
    }

    public CoursDTO getCours() {
        return cours;
    }

    public void setCours(CoursDTO cours) {
        this.cours = cours;
    }

    public TypeTacheDTO getTypeTache() {
        return typeTache;
    }

    public void setTypeTache(TypeTacheDTO typeTache) {
        this.typeTache = typeTache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationPrevueDTO)) {
            return false;
        }

        EvaluationPrevueDTO evaluationPrevueDTO = (EvaluationPrevueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evaluationPrevueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationPrevueDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", libelleImpression='" + getLibelleImpression() + "'" +
            ", coefficient=" + getCoefficient() +
            ", compteDansMoyenne='" + getCompteDansMoyenne() + "'" +
            ", noteMaximale=" + getNoteMaximale() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", cycle=" + getCycle() +
            ", enseignant=" + getEnseignant() +
            ", matiere=" + getMatiere() +
            ", sousMatiere=" + getSousMatiere() +
            ", cours=" + getCours() +
            ", typeTache=" + getTypeTache() +
            "}";
    }
}
