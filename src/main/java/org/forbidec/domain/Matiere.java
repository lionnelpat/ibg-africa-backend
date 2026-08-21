package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "intitule", length = 100, nullable = false, unique = true)
    private String intitule;

    @Size(max = 100)
    @Column(name = "libelle_long", length = 100)
    private String libelleLong;

    @Size(max = 50)
    @Column(name = "libelle_court", length = 50)
    private String libelleCourt;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "enseignant", "matiere", "sousMatiere", "cours", "typeTache", "notes" }, allowSetters = true)
    private Set<EvaluationPrevue> evaluations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public Matiere intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getLibelleLong() {
        return this.libelleLong;
    }

    public Matiere libelleLong(String libelleLong) {
        this.setLibelleLong(libelleLong);
        return this;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return this.libelleCourt;
    }

    public Matiere libelleCourt(String libelleCourt) {
        this.setLibelleCourt(libelleCourt);
        return this;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Matiere commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Matiere actif(Boolean actif) {
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
            this.evaluations.forEach(i -> i.setMatiere(null));
        }
        if (evaluationPrevues != null) {
            evaluationPrevues.forEach(i -> i.setMatiere(this));
        }
        this.evaluations = evaluationPrevues;
    }

    public Matiere evaluations(Set<EvaluationPrevue> evaluationPrevues) {
        this.setEvaluations(evaluationPrevues);
        return this;
    }

    public Matiere addEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.add(evaluationPrevue);
        evaluationPrevue.setMatiere(this);
        return this;
    }

    public Matiere removeEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.remove(evaluationPrevue);
        evaluationPrevue.setMatiere(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return getId() != null && getId().equals(((Matiere) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
