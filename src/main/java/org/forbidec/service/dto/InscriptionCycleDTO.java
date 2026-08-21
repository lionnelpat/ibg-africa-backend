package org.forbidec.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.forbidec.domain.InscriptionCycle} entity.
 */
@Schema(
    description = "Inscription d'un étudiant à un cycle.\ncommentaire1/2/3/5 sont conservés à l'identique le temps que leur\nusage réel soit élucidé ; ils sont quasi vides dans la base migrée."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionCycleDTO implements Serializable {

    private Long id;

    private LocalDate dateInscription;

    @NotNull
    private Boolean cycleTermine;

    @Size(max = 100)
    private String groupe;

    @Size(max = 255)
    private String commentaire1;

    @Size(max = 255)
    private String commentaire2;

    @Size(max = 255)
    private String commentaire3;

    @Size(max = 255)
    private String commentaire5;

    @NotNull
    private CycleDTO cycle;

    @NotNull
    private EtudiantDTO etudiant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getCycleTermine() {
        return cycleTermine;
    }

    public void setCycleTermine(Boolean cycleTermine) {
        this.cycleTermine = cycleTermine;
    }

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
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

    public String getCommentaire5() {
        return commentaire5;
    }

    public void setCommentaire5(String commentaire5) {
        this.commentaire5 = commentaire5;
    }

    public CycleDTO getCycle() {
        return cycle;
    }

    public void setCycle(CycleDTO cycle) {
        this.cycle = cycle;
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
        if (!(o instanceof InscriptionCycleDTO)) {
            return false;
        }

        InscriptionCycleDTO inscriptionCycleDTO = (InscriptionCycleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inscriptionCycleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionCycleDTO{" +
            "id=" + getId() +
            ", dateInscription='" + getDateInscription() + "'" +
            ", cycleTermine='" + getCycleTermine() + "'" +
            ", groupe='" + getGroupe() + "'" +
            ", commentaire1='" + getCommentaire1() + "'" +
            ", commentaire2='" + getCommentaire2() + "'" +
            ", commentaire3='" + getCommentaire3() + "'" +
            ", commentaire5='" + getCommentaire5() + "'" +
            ", cycle=" + getCycle() +
            ", etudiant=" + getEtudiant() +
            "}";
    }
}
