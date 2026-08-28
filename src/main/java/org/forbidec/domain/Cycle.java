package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Promotion annuelle d'un centre. cloture verrouille toute saisie.
 */
@Entity
@Table(name = "cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(name = "paysFilter", condition = "centre_id in (select cf.id from centre_formation cf where cf.pays_id in (:paysIds))")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2200)
    @Column(name = "annee", nullable = false)
    private Integer annee;

    @Size(max = 100)
    @Column(name = "libelle", length = 100)
    private String libelle;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @NotNull
    @Column(name = "cloture", nullable = false)
    private Boolean cloture;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pays", "baremes", "parametres", "cycles", "habilitations" }, allowSetters = true)
    private CentreFormation centre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cycle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "etudiant" }, allowSetters = true)
    private Set<InscriptionCycle> inscriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cycle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "enseignant", "matiere", "sousMatiere", "cours", "typeTache", "notes" }, allowSetters = true)
    private Set<EvaluationPrevue> evaluations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cycle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "centre", "cycle" }, allowSetters = true)
    private Set<HabilitationCycle> habilitations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return this.annee;
    }

    public Cycle annee(Integer annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Cycle libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Cycle dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Cycle dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getCloture() {
        return this.cloture;
    }

    public Cycle cloture(Boolean cloture) {
        this.setCloture(cloture);
        return this;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Cycle commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public CentreFormation getCentre() {
        return this.centre;
    }

    public void setCentre(CentreFormation centreFormation) {
        this.centre = centreFormation;
    }

    public Cycle centre(CentreFormation centreFormation) {
        this.setCentre(centreFormation);
        return this;
    }

    public Set<InscriptionCycle> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<InscriptionCycle> inscriptionCycles) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setCycle(null));
        }
        if (inscriptionCycles != null) {
            inscriptionCycles.forEach(i -> i.setCycle(this));
        }
        this.inscriptions = inscriptionCycles;
    }

    public Cycle inscriptions(Set<InscriptionCycle> inscriptionCycles) {
        this.setInscriptions(inscriptionCycles);
        return this;
    }

    public Cycle addInscription(InscriptionCycle inscriptionCycle) {
        this.inscriptions.add(inscriptionCycle);
        inscriptionCycle.setCycle(this);
        return this;
    }

    public Cycle removeInscription(InscriptionCycle inscriptionCycle) {
        this.inscriptions.remove(inscriptionCycle);
        inscriptionCycle.setCycle(null);
        return this;
    }

    public Set<EvaluationPrevue> getEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(Set<EvaluationPrevue> evaluationPrevues) {
        if (this.evaluations != null) {
            this.evaluations.forEach(i -> i.setCycle(null));
        }
        if (evaluationPrevues != null) {
            evaluationPrevues.forEach(i -> i.setCycle(this));
        }
        this.evaluations = evaluationPrevues;
    }

    public Cycle evaluations(Set<EvaluationPrevue> evaluationPrevues) {
        this.setEvaluations(evaluationPrevues);
        return this;
    }

    public Cycle addEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.add(evaluationPrevue);
        evaluationPrevue.setCycle(this);
        return this;
    }

    public Cycle removeEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.remove(evaluationPrevue);
        evaluationPrevue.setCycle(null);
        return this;
    }

    public Set<HabilitationCycle> getHabilitations() {
        return this.habilitations;
    }

    public void setHabilitations(Set<HabilitationCycle> habilitationCycles) {
        if (this.habilitations != null) {
            this.habilitations.forEach(i -> i.setCycle(null));
        }
        if (habilitationCycles != null) {
            habilitationCycles.forEach(i -> i.setCycle(this));
        }
        this.habilitations = habilitationCycles;
    }

    public Cycle habilitations(Set<HabilitationCycle> habilitationCycles) {
        this.setHabilitations(habilitationCycles);
        return this;
    }

    public Cycle addHabilitation(HabilitationCycle habilitationCycle) {
        this.habilitations.add(habilitationCycle);
        habilitationCycle.setCycle(this);
        return this;
    }

    public Cycle removeHabilitation(HabilitationCycle habilitationCycle) {
        this.habilitations.remove(habilitationCycle);
        habilitationCycle.setCycle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cycle)) {
            return false;
        }
        return getId() != null && getId().equals(((Cycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cycle{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", libelle='" + getLibelle() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", cloture='" + getCloture() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
