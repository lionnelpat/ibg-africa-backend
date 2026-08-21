package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Évaluation planifiée pour un cycle : le « quoi » et le « par qui ».
 */
@Entity
@Table(name = "evaluation_prevue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationPrevue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "intitule", length = 150, nullable = false)
    private String intitule;

    @NotNull
    @Size(max = 150)
    @Column(name = "libelle_impression", length = 150, nullable = false)
    private String libelleImpression;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "coefficient", precision = 21, scale = 2, nullable = false)
    private BigDecimal coefficient;

    @NotNull
    @Column(name = "compte_dans_moyenne", nullable = false)
    private Boolean compteDansMoyenne;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "note_maximale", precision = 21, scale = 2, nullable = false)
    private BigDecimal noteMaximale;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "centre", "inscriptions", "evaluations", "habilitations" }, allowSetters = true)
    private Cycle cycle;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluations" }, allowSetters = true)
    private Enseignant enseignant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluations" }, allowSetters = true)
    private Matiere matiere;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluations" }, allowSetters = true)
    private SousMatiere sousMatiere;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluations" }, allowSetters = true)
    private Cours cours;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluations" }, allowSetters = true)
    private TypeTache typeTache;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evaluationPrevue")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluationPrevue", "etudiant", "historiques" }, allowSetters = true)
    private Set<EvaluationRealisee> notes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EvaluationPrevue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public EvaluationPrevue intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getLibelleImpression() {
        return this.libelleImpression;
    }

    public EvaluationPrevue libelleImpression(String libelleImpression) {
        this.setLibelleImpression(libelleImpression);
        return this;
    }

    public void setLibelleImpression(String libelleImpression) {
        this.libelleImpression = libelleImpression;
    }

    public BigDecimal getCoefficient() {
        return this.coefficient;
    }

    public EvaluationPrevue coefficient(BigDecimal coefficient) {
        this.setCoefficient(coefficient);
        return this;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public Boolean getCompteDansMoyenne() {
        return this.compteDansMoyenne;
    }

    public EvaluationPrevue compteDansMoyenne(Boolean compteDansMoyenne) {
        this.setCompteDansMoyenne(compteDansMoyenne);
        return this;
    }

    public void setCompteDansMoyenne(Boolean compteDansMoyenne) {
        this.compteDansMoyenne = compteDansMoyenne;
    }

    public BigDecimal getNoteMaximale() {
        return this.noteMaximale;
    }

    public EvaluationPrevue noteMaximale(BigDecimal noteMaximale) {
        this.setNoteMaximale(noteMaximale);
        return this;
    }

    public void setNoteMaximale(BigDecimal noteMaximale) {
        this.noteMaximale = noteMaximale;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public EvaluationPrevue dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public EvaluationPrevue dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public EvaluationPrevue commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Cycle getCycle() {
        return this.cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public EvaluationPrevue cycle(Cycle cycle) {
        this.setCycle(cycle);
        return this;
    }

    public Enseignant getEnseignant() {
        return this.enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public EvaluationPrevue enseignant(Enseignant enseignant) {
        this.setEnseignant(enseignant);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public EvaluationPrevue matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public SousMatiere getSousMatiere() {
        return this.sousMatiere;
    }

    public void setSousMatiere(SousMatiere sousMatiere) {
        this.sousMatiere = sousMatiere;
    }

    public EvaluationPrevue sousMatiere(SousMatiere sousMatiere) {
        this.setSousMatiere(sousMatiere);
        return this;
    }

    public Cours getCours() {
        return this.cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public EvaluationPrevue cours(Cours cours) {
        this.setCours(cours);
        return this;
    }

    public TypeTache getTypeTache() {
        return this.typeTache;
    }

    public void setTypeTache(TypeTache typeTache) {
        this.typeTache = typeTache;
    }

    public EvaluationPrevue typeTache(TypeTache typeTache) {
        this.setTypeTache(typeTache);
        return this;
    }

    public Set<EvaluationRealisee> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<EvaluationRealisee> evaluationRealisees) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setEvaluationPrevue(null));
        }
        if (evaluationRealisees != null) {
            evaluationRealisees.forEach(i -> i.setEvaluationPrevue(this));
        }
        this.notes = evaluationRealisees;
    }

    public EvaluationPrevue notes(Set<EvaluationRealisee> evaluationRealisees) {
        this.setNotes(evaluationRealisees);
        return this;
    }

    public EvaluationPrevue addNote(EvaluationRealisee evaluationRealisee) {
        this.notes.add(evaluationRealisee);
        evaluationRealisee.setEvaluationPrevue(this);
        return this;
    }

    public EvaluationPrevue removeNote(EvaluationRealisee evaluationRealisee) {
        this.notes.remove(evaluationRealisee);
        evaluationRealisee.setEvaluationPrevue(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationPrevue)) {
            return false;
        }
        return getId() != null && getId().equals(((EvaluationPrevue) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationPrevue{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", libelleImpression='" + getLibelleImpression() + "'" +
            ", coefficient=" + getCoefficient() +
            ", compteDansMoyenne='" + getCompteDansMoyenne() + "'" +
            ", noteMaximale=" + getNoteMaximale() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
