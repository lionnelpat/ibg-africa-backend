package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.forbidec.domain.enumeration.StatutNote;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Note d'un étudiant sur une évaluation.
 * note nullable + statut : NON_SAISIE et une note de 0 sont enfin
 * deux choses différentes.
 */
@Entity
@Table(name = "evaluation_realisee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(name = "paysFilter", condition = "etudiant_id in (select e.id from etudiant e where e.pays_id in (:paysIds))")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationRealisee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "note", precision = 21, scale = 2)
    private BigDecimal note;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutNote statut;

    @NotNull
    @Column(name = "compte_dans_moyenne", nullable = false)
    private Boolean compteDansMoyenne;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Size(max = 255)
    @Column(name = "commentaire_1", length = 255)
    private String commentaire1;

    @Size(max = 255)
    @Column(name = "commentaire_2", length = 255)
    private String commentaire2;

    @Size(max = 255)
    @Column(name = "commentaire_3", length = 255)
    private String commentaire3;

    @Size(max = 64)
    @Column(name = "saisie_par", length = 64)
    private String saisiePar;

    @Column(name = "saisie_le")
    private Instant saisieLe;

    @Size(max = 64)
    @Column(name = "validee_par", length = 64)
    private String valideePar;

    @Column(name = "validee_le")
    private Instant valideeLe;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cycle", "enseignant", "matiere", "sousMatiere", "cours", "typeTache", "notes" }, allowSetters = true)
    private EvaluationPrevue evaluationPrevue;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pays", "inscriptions", "evenements", "notes" }, allowSetters = true)
    private Etudiant etudiant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "evaluationRealisee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluationRealisee" }, allowSetters = true)
    private Set<HistoriqueNote> historiques = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EvaluationRealisee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNote() {
        return this.note;
    }

    public EvaluationRealisee note(BigDecimal note) {
        this.setNote(note);
        return this;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public StatutNote getStatut() {
        return this.statut;
    }

    public EvaluationRealisee statut(StatutNote statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutNote statut) {
        this.statut = statut;
    }

    public Boolean getCompteDansMoyenne() {
        return this.compteDansMoyenne;
    }

    public EvaluationRealisee compteDansMoyenne(Boolean compteDansMoyenne) {
        this.setCompteDansMoyenne(compteDansMoyenne);
        return this;
    }

    public void setCompteDansMoyenne(Boolean compteDansMoyenne) {
        this.compteDansMoyenne = compteDansMoyenne;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public EvaluationRealisee dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public EvaluationRealisee dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getCommentaire1() {
        return this.commentaire1;
    }

    public EvaluationRealisee commentaire1(String commentaire1) {
        this.setCommentaire1(commentaire1);
        return this;
    }

    public void setCommentaire1(String commentaire1) {
        this.commentaire1 = commentaire1;
    }

    public String getCommentaire2() {
        return this.commentaire2;
    }

    public EvaluationRealisee commentaire2(String commentaire2) {
        this.setCommentaire2(commentaire2);
        return this;
    }

    public void setCommentaire2(String commentaire2) {
        this.commentaire2 = commentaire2;
    }

    public String getCommentaire3() {
        return this.commentaire3;
    }

    public EvaluationRealisee commentaire3(String commentaire3) {
        this.setCommentaire3(commentaire3);
        return this;
    }

    public void setCommentaire3(String commentaire3) {
        this.commentaire3 = commentaire3;
    }

    public String getSaisiePar() {
        return this.saisiePar;
    }

    public EvaluationRealisee saisiePar(String saisiePar) {
        this.setSaisiePar(saisiePar);
        return this;
    }

    public void setSaisiePar(String saisiePar) {
        this.saisiePar = saisiePar;
    }

    public Instant getSaisieLe() {
        return this.saisieLe;
    }

    public EvaluationRealisee saisieLe(Instant saisieLe) {
        this.setSaisieLe(saisieLe);
        return this;
    }

    public void setSaisieLe(Instant saisieLe) {
        this.saisieLe = saisieLe;
    }

    public String getValideePar() {
        return this.valideePar;
    }

    public EvaluationRealisee valideePar(String valideePar) {
        this.setValideePar(valideePar);
        return this;
    }

    public void setValideePar(String valideePar) {
        this.valideePar = valideePar;
    }

    public Instant getValideeLe() {
        return this.valideeLe;
    }

    public EvaluationRealisee valideeLe(Instant valideeLe) {
        this.setValideeLe(valideeLe);
        return this;
    }

    public void setValideeLe(Instant valideeLe) {
        this.valideeLe = valideeLe;
    }

    public EvaluationPrevue getEvaluationPrevue() {
        return this.evaluationPrevue;
    }

    public void setEvaluationPrevue(EvaluationPrevue evaluationPrevue) {
        this.evaluationPrevue = evaluationPrevue;
    }

    public EvaluationRealisee evaluationPrevue(EvaluationPrevue evaluationPrevue) {
        this.setEvaluationPrevue(evaluationPrevue);
        return this;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public EvaluationRealisee etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    public Set<HistoriqueNote> getHistoriques() {
        return this.historiques;
    }

    public void setHistoriques(Set<HistoriqueNote> historiqueNotes) {
        if (this.historiques != null) {
            this.historiques.forEach(i -> i.setEvaluationRealisee(null));
        }
        if (historiqueNotes != null) {
            historiqueNotes.forEach(i -> i.setEvaluationRealisee(this));
        }
        this.historiques = historiqueNotes;
    }

    public EvaluationRealisee historiques(Set<HistoriqueNote> historiqueNotes) {
        this.setHistoriques(historiqueNotes);
        return this;
    }

    public EvaluationRealisee addHistorique(HistoriqueNote historiqueNote) {
        this.historiques.add(historiqueNote);
        historiqueNote.setEvaluationRealisee(this);
        return this;
    }

    public EvaluationRealisee removeHistorique(HistoriqueNote historiqueNote) {
        this.historiques.remove(historiqueNote);
        historiqueNote.setEvaluationRealisee(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvaluationRealisee)) {
            return false;
        }
        return getId() != null && getId().equals(((EvaluationRealisee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationRealisee{" +
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
            "}";
    }
}
