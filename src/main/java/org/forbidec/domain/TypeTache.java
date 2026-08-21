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
 * Type d'évaluation. Le code technique évite de dépendre du libellé :
 * les requêtes Access filtraient sur la chaîne « Académique ».
 */
@Entity
@Table(name = "type_tache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeTache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "code", length = 30, nullable = false, unique = true)
    private String code;

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

    @NotNull
    @Column(name = "entre_dans_moyenne", nullable = false)
    private Boolean entreDansMoyenne;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "typeTache")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "enseignant", "matiere", "sousMatiere", "cours", "typeTache", "notes" }, allowSetters = true)
    private Set<EvaluationPrevue> evaluations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeTache id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public TypeTache code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public TypeTache intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getLibelleLong() {
        return this.libelleLong;
    }

    public TypeTache libelleLong(String libelleLong) {
        this.setLibelleLong(libelleLong);
        return this;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return this.libelleCourt;
    }

    public TypeTache libelleCourt(String libelleCourt) {
        this.setLibelleCourt(libelleCourt);
        return this;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public Boolean getEntreDansMoyenne() {
        return this.entreDansMoyenne;
    }

    public TypeTache entreDansMoyenne(Boolean entreDansMoyenne) {
        this.setEntreDansMoyenne(entreDansMoyenne);
        return this;
    }

    public void setEntreDansMoyenne(Boolean entreDansMoyenne) {
        this.entreDansMoyenne = entreDansMoyenne;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public TypeTache commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public TypeTache actif(Boolean actif) {
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
            this.evaluations.forEach(i -> i.setTypeTache(null));
        }
        if (evaluationPrevues != null) {
            evaluationPrevues.forEach(i -> i.setTypeTache(this));
        }
        this.evaluations = evaluationPrevues;
    }

    public TypeTache evaluations(Set<EvaluationPrevue> evaluationPrevues) {
        this.setEvaluations(evaluationPrevues);
        return this;
    }

    public TypeTache addEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.add(evaluationPrevue);
        evaluationPrevue.setTypeTache(this);
        return this;
    }

    public TypeTache removeEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.remove(evaluationPrevue);
        evaluationPrevue.setTypeTache(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeTache)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeTache) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeTache{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", intitule='" + getIntitule() + "'" +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", entreDansMoyenne='" + getEntreDansMoyenne() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
