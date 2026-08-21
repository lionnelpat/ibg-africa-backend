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
 * Unité d'enseignement. Le coefficient porté ici sert de valeur par défaut.
 */
@Entity
@Table(name = "cours")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 150)
    @Column(name = "intitule", length = 150, nullable = false, unique = true)
    private String intitule;

    @Size(max = 150)
    @Column(name = "libelle_long", length = 150)
    private String libelleLong;

    @Size(max = 60)
    @Column(name = "libelle_court", length = 60)
    private String libelleCourt;

    @NotNull
    @Column(name = "ordre_affichage", nullable = false)
    private Integer ordreAffichage;

    @Min(value = 0)
    @Column(name = "nb_periodes")
    private Integer nbPeriodes;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "coefficient", precision = 21, scale = 2, nullable = false)
    private BigDecimal coefficient;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cours")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "enseignant", "matiere", "sousMatiere", "cours", "typeTache", "notes" }, allowSetters = true)
    private Set<EvaluationPrevue> evaluations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cours id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public Cours intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getLibelleLong() {
        return this.libelleLong;
    }

    public Cours libelleLong(String libelleLong) {
        this.setLibelleLong(libelleLong);
        return this;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return this.libelleCourt;
    }

    public Cours libelleCourt(String libelleCourt) {
        this.setLibelleCourt(libelleCourt);
        return this;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public Integer getOrdreAffichage() {
        return this.ordreAffichage;
    }

    public Cours ordreAffichage(Integer ordreAffichage) {
        this.setOrdreAffichage(ordreAffichage);
        return this;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    public Integer getNbPeriodes() {
        return this.nbPeriodes;
    }

    public Cours nbPeriodes(Integer nbPeriodes) {
        this.setNbPeriodes(nbPeriodes);
        return this;
    }

    public void setNbPeriodes(Integer nbPeriodes) {
        this.nbPeriodes = nbPeriodes;
    }

    public BigDecimal getCoefficient() {
        return this.coefficient;
    }

    public Cours coefficient(BigDecimal coefficient) {
        this.setCoefficient(coefficient);
        return this;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Cours dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Cours dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Cours commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Cours actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Set<EvaluationPrevue> getEvaluations() {
        return this.evaluations;
    }

    public void setEvaluations(Set<EvaluationPrevue> evaluationPrevues) {
        if (this.evaluations != null) {
            this.evaluations.forEach(i -> i.setCours(null));
        }
        if (evaluationPrevues != null) {
            evaluationPrevues.forEach(i -> i.setCours(this));
        }
        this.evaluations = evaluationPrevues;
    }

    public Cours evaluations(Set<EvaluationPrevue> evaluationPrevues) {
        this.setEvaluations(evaluationPrevues);
        return this;
    }

    public Cours addEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.add(evaluationPrevue);
        evaluationPrevue.setCours(this);
        return this;
    }

    public Cours removeEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.remove(evaluationPrevue);
        evaluationPrevue.setCours(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cours)) {
            return false;
        }
        return getId() != null && getId().equals(((Cours) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cours{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            ", nbPeriodes=" + getNbPeriodes() +
            ", coefficient=" + getCoefficient() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
