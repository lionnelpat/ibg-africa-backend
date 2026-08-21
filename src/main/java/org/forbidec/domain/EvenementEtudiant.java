package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EvenementEtudiant.
 */
@Entity
@Table(name = "evenement_etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvenementEtudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_evenement")
    private LocalDate dateEvenement;

    @NotNull
    @Size(max = 150)
    @Column(name = "intitule", length = 150, nullable = false)
    private String intitule;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pays", "inscriptions", "evenements", "notes" }, allowSetters = true)
    private Etudiant etudiant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EvenementEtudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateEvenement() {
        return this.dateEvenement;
    }

    public EvenementEtudiant dateEvenement(LocalDate dateEvenement) {
        this.setDateEvenement(dateEvenement);
        return this;
    }

    public void setDateEvenement(LocalDate dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public EvenementEtudiant intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public EvenementEtudiant commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public EvenementEtudiant etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvenementEtudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((EvenementEtudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvenementEtudiant{" +
            "id=" + getId() +
            ", dateEvenement='" + getDateEvenement() + "'" +
            ", intitule='" + getIntitule() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
