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

/**
 * Étudiant.
 * matricule remplace le triplet (nom, prénom, particularité) comme clé
 * métier : la base contient 5 homonymes stricts.
 * anneeEntree et cursusAcheve sont dérivés de champs texte Access
 * (« Se - 2014 », « Finaliste en 2022 » — 165 étudiants concernés).
 */
@Entity
@Table(name = "etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 30)
    @Column(name = "matricule", length = 30, unique = true)
    private String matricule;

    @NotNull
    @Size(max = 80)
    @Column(name = "nom", length = 80, nullable = false)
    private String nom;

    @NotNull
    @Size(max = 80)
    @Column(name = "prenom", length = 80, nullable = false)
    private String prenom;

    @Size(max = 80)
    @Column(name = "particularite", length = 80)
    private String particularite;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Size(max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Size(max = 30)
    @Column(name = "telephone", length = 30)
    private String telephone;

    @Min(value = 1900)
    @Max(value = 2200)
    @Column(name = "annee_entree")
    private Integer anneeEntree;

    @NotNull
    @Column(name = "cursus_acheve", nullable = false)
    private Boolean cursusAcheve;

    @Min(value = 1900)
    @Max(value = 2200)
    @Column(name = "annee_finale")
    private Integer anneeFinale;

    @Size(max = 64)
    @Column(name = "keycloak_user_id", length = 64, unique = true)
    private String keycloakUserId;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "centres", "etudiants" }, allowSetters = true)
    private Pays pays;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cycle", "etudiant" }, allowSetters = true)
    private Set<InscriptionCycle> inscriptions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiant" }, allowSetters = true)
    private Set<EvenementEtudiant> evenements = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "evaluationPrevue", "etudiant", "historiques" }, allowSetters = true)
    private Set<EvaluationRealisee> notes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Etudiant matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return this.nom;
    }

    public Etudiant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Etudiant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getParticularite() {
        return this.particularite;
    }

    public Etudiant particularite(String particularite) {
        this.setParticularite(particularite);
        return this;
    }

    public void setParticularite(String particularite) {
        this.particularite = particularite;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Etudiant dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getEmail() {
        return this.email;
    }

    public Etudiant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Etudiant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getAnneeEntree() {
        return this.anneeEntree;
    }

    public Etudiant anneeEntree(Integer anneeEntree) {
        this.setAnneeEntree(anneeEntree);
        return this;
    }

    public void setAnneeEntree(Integer anneeEntree) {
        this.anneeEntree = anneeEntree;
    }

    public Boolean getCursusAcheve() {
        return this.cursusAcheve;
    }

    public Etudiant cursusAcheve(Boolean cursusAcheve) {
        this.setCursusAcheve(cursusAcheve);
        return this;
    }

    public void setCursusAcheve(Boolean cursusAcheve) {
        this.cursusAcheve = cursusAcheve;
    }

    public Integer getAnneeFinale() {
        return this.anneeFinale;
    }

    public Etudiant anneeFinale(Integer anneeFinale) {
        this.setAnneeFinale(anneeFinale);
        return this;
    }

    public void setAnneeFinale(Integer anneeFinale) {
        this.anneeFinale = anneeFinale;
    }

    public String getKeycloakUserId() {
        return this.keycloakUserId;
    }

    public Etudiant keycloakUserId(String keycloakUserId) {
        this.setKeycloakUserId(keycloakUserId);
        return this;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Etudiant commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Etudiant actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Pays getPays() {
        return this.pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

    public Etudiant pays(Pays pays) {
        this.setPays(pays);
        return this;
    }

    public Set<InscriptionCycle> getInscriptions() {
        return this.inscriptions;
    }

    public void setInscriptions(Set<InscriptionCycle> inscriptionCycles) {
        if (this.inscriptions != null) {
            this.inscriptions.forEach(i -> i.setEtudiant(null));
        }
        if (inscriptionCycles != null) {
            inscriptionCycles.forEach(i -> i.setEtudiant(this));
        }
        this.inscriptions = inscriptionCycles;
    }

    public Etudiant inscriptions(Set<InscriptionCycle> inscriptionCycles) {
        this.setInscriptions(inscriptionCycles);
        return this;
    }

    public Etudiant addInscription(InscriptionCycle inscriptionCycle) {
        this.inscriptions.add(inscriptionCycle);
        inscriptionCycle.setEtudiant(this);
        return this;
    }

    public Etudiant removeInscription(InscriptionCycle inscriptionCycle) {
        this.inscriptions.remove(inscriptionCycle);
        inscriptionCycle.setEtudiant(null);
        return this;
    }

    public Set<EvenementEtudiant> getEvenements() {
        return this.evenements;
    }

    public void setEvenements(Set<EvenementEtudiant> evenementEtudiants) {
        if (this.evenements != null) {
            this.evenements.forEach(i -> i.setEtudiant(null));
        }
        if (evenementEtudiants != null) {
            evenementEtudiants.forEach(i -> i.setEtudiant(this));
        }
        this.evenements = evenementEtudiants;
    }

    public Etudiant evenements(Set<EvenementEtudiant> evenementEtudiants) {
        this.setEvenements(evenementEtudiants);
        return this;
    }

    public Etudiant addEvenement(EvenementEtudiant evenementEtudiant) {
        this.evenements.add(evenementEtudiant);
        evenementEtudiant.setEtudiant(this);
        return this;
    }

    public Etudiant removeEvenement(EvenementEtudiant evenementEtudiant) {
        this.evenements.remove(evenementEtudiant);
        evenementEtudiant.setEtudiant(null);
        return this;
    }

    public Set<EvaluationRealisee> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<EvaluationRealisee> evaluationRealisees) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setEtudiant(null));
        }
        if (evaluationRealisees != null) {
            evaluationRealisees.forEach(i -> i.setEtudiant(this));
        }
        this.notes = evaluationRealisees;
    }

    public Etudiant notes(Set<EvaluationRealisee> evaluationRealisees) {
        this.setNotes(evaluationRealisees);
        return this;
    }

    public Etudiant addNote(EvaluationRealisee evaluationRealisee) {
        this.notes.add(evaluationRealisee);
        evaluationRealisee.setEtudiant(this);
        return this;
    }

    public Etudiant removeNote(EvaluationRealisee evaluationRealisee) {
        this.notes.remove(evaluationRealisee);
        evaluationRealisee.setEtudiant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((Etudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", matricule='" + getMatricule() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", particularite='" + getParticularite() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", email='" + getEmail() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", anneeEntree=" + getAnneeEntree() +
            ", cursusAcheve='" + getCursusAcheve() + "'" +
            ", anneeFinale=" + getAnneeFinale() +
            ", keycloakUserId='" + getKeycloakUserId() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
