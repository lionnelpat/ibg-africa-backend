package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Inscription d'un étudiant à un cycle.
 * commentaire1/2/3/5 sont conservés à l'identique le temps que leur
 * usage réel soit élucidé ; ils sont quasi vides dans la base migrée.
 */
@Entity
@Table(name = "inscription_cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(name = "paysFilter", condition = "etudiant_id in (select e.id from etudiant e where e.pays_id in (:paysIds))")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionCycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    @NotNull
    @Column(name = "cycle_termine", nullable = false)
    private Boolean cycleTermine;

    @Size(max = 100)
    @Column(name = "groupe", length = 100)
    private String groupe;

    @Size(max = 255)
    @Column(name = "commentaire_1", length = 255)
    private String commentaire1;

    @Size(max = 255)
    @Column(name = "commentaire_2", length = 255)
    private String commentaire2;

    @Size(max = 255)
    @Column(name = "commentaire_3", length = 255)
    private String commentaire3;

    @Size(max = 255)
    @Column(name = "commentaire_5", length = 255)
    private String commentaire5;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "centre", "inscriptions", "evaluations", "habilitations" }, allowSetters = true)
    private Cycle cycle;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pays", "inscriptions", "evenements", "notes" }, allowSetters = true)
    private Etudiant etudiant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InscriptionCycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateInscription() {
        return this.dateInscription;
    }

    public InscriptionCycle dateInscription(LocalDate dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getCycleTermine() {
        return this.cycleTermine;
    }

    public InscriptionCycle cycleTermine(Boolean cycleTermine) {
        this.setCycleTermine(cycleTermine);
        return this;
    }

    public void setCycleTermine(Boolean cycleTermine) {
        this.cycleTermine = cycleTermine;
    }

    public String getGroupe() {
        return this.groupe;
    }

    public InscriptionCycle groupe(String groupe) {
        this.setGroupe(groupe);
        return this;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getCommentaire1() {
        return this.commentaire1;
    }

    public InscriptionCycle commentaire1(String commentaire1) {
        this.setCommentaire1(commentaire1);
        return this;
    }

    public void setCommentaire1(String commentaire1) {
        this.commentaire1 = commentaire1;
    }

    public String getCommentaire2() {
        return this.commentaire2;
    }

    public InscriptionCycle commentaire2(String commentaire2) {
        this.setCommentaire2(commentaire2);
        return this;
    }

    public void setCommentaire2(String commentaire2) {
        this.commentaire2 = commentaire2;
    }

    public String getCommentaire3() {
        return this.commentaire3;
    }

    public InscriptionCycle commentaire3(String commentaire3) {
        this.setCommentaire3(commentaire3);
        return this;
    }

    public void setCommentaire3(String commentaire3) {
        this.commentaire3 = commentaire3;
    }

    public String getCommentaire5() {
        return this.commentaire5;
    }

    public InscriptionCycle commentaire5(String commentaire5) {
        this.setCommentaire5(commentaire5);
        return this;
    }

    public void setCommentaire5(String commentaire5) {
        this.commentaire5 = commentaire5;
    }

    public Cycle getCycle() {
        return this.cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public InscriptionCycle cycle(Cycle cycle) {
        this.setCycle(cycle);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public InscriptionCycle etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InscriptionCycle)) {
            return false;
        }
        return getId() != null && getId().equals(((InscriptionCycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionCycle{" +
            "id=" + getId() +
            ", dateInscription='" + getDateInscription() + "'" +
            ", cycleTermine='" + getCycleTermine() + "'" +
            ", groupe='" + getGroupe() + "'" +
            ", commentaire1='" + getCommentaire1() + "'" +
            ", commentaire2='" + getCommentaire2() + "'" +
            ", commentaire3='" + getCommentaire3() + "'" +
            ", commentaire5='" + getCommentaire5() + "'" +
            "}";
    }
}
