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
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "nom", length = 80, nullable = false)
    private String nom;

    @NotNull
    @Size(max = 80)
    @Column(name = "prenom", length = 80, nullable = false)
    private String prenom;

    @Size(max = 100)
    @Column(name = "libelle_long", length = 100)
    private String libelleLong;

    @Size(max = 50)
    @Column(name = "libelle_court", length = 50)
    private String libelleCourt;

    @Size(max = 150)
    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Size(max = 30)
    @Column(name = "telephone", length = 30)
    private String telephone;

    @Size(max = 64)
    @Column(name = "keycloak_user_id", length = 64, unique = true)
    private String keycloakUserId;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "enseignant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "enseignant", "matiere", "sousMatiere", "cours", "typeTache", "notes" }, allowSetters = true)
    private Set<EvaluationPrevue> evaluations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enseignant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Enseignant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Enseignant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLibelleLong() {
        return this.libelleLong;
    }

    public Enseignant libelleLong(String libelleLong) {
        this.setLibelleLong(libelleLong);
        return this;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return this.libelleCourt;
    }

    public Enseignant libelleCourt(String libelleCourt) {
        this.setLibelleCourt(libelleCourt);
        return this;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getEmail() {
        return this.email;
    }

    public Enseignant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Enseignant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getKeycloakUserId() {
        return this.keycloakUserId;
    }

    public Enseignant keycloakUserId(String keycloakUserId) {
        this.setKeycloakUserId(keycloakUserId);
        return this;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Enseignant commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Enseignant actif(Boolean actif) {
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
            this.evaluations.forEach(i -> i.setEnseignant(null));
        }
        if (evaluationPrevues != null) {
            evaluationPrevues.forEach(i -> i.setEnseignant(this));
        }
        this.evaluations = evaluationPrevues;
    }

    public Enseignant evaluations(Set<EvaluationPrevue> evaluationPrevues) {
        this.setEvaluations(evaluationPrevues);
        return this;
    }

    public Enseignant addEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.add(evaluationPrevue);
        evaluationPrevue.setEnseignant(this);
        return this;
    }

    public Enseignant removeEvaluation(EvaluationPrevue evaluationPrevue) {
        this.evaluations.remove(evaluationPrevue);
        evaluationPrevue.setEnseignant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enseignant)) {
            return false;
        }
        return getId() != null && getId().equals(((Enseignant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enseignant{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", keycloakUserId='" + getKeycloakUserId() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
