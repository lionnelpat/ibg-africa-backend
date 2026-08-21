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
 * Pays d'implantation d'un ou plusieurs centres de formation.
 */
@Entity
@Table(name = "pays")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "code_iso", length = 2, nullable = false, unique = true)
    private String codeIso;

    @NotNull
    @Size(max = 100)
    @Column(name = "nom", length = 100, nullable = false, unique = true)
    private String nom;

    @NotNull
    @Size(max = 5)
    @Column(name = "langue", length = 5, nullable = false)
    private String langue;

    @Size(max = 50)
    @Column(name = "fuseau", length = 50)
    private String fuseau;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "baremes", "parametres", "cycles", "habilitations" }, allowSetters = true)
    private Set<CentreFormation> centres = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pays")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pays", "inscriptions", "evenements", "notes" }, allowSetters = true)
    private Set<Etudiant> etudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pays id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeIso() {
        return this.codeIso;
    }

    public Pays codeIso(String codeIso) {
        this.setCodeIso(codeIso);
        return this;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getNom() {
        return this.nom;
    }

    public Pays nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLangue() {
        return this.langue;
    }

    public Pays langue(String langue) {
        this.setLangue(langue);
        return this;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getFuseau() {
        return this.fuseau;
    }

    public Pays fuseau(String fuseau) {
        this.setFuseau(fuseau);
        return this;
    }

    public void setFuseau(String fuseau) {
        this.fuseau = fuseau;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Pays actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Set<CentreFormation> getCentres() {
        return this.centres;
    }

    public void setCentres(Set<CentreFormation> centreFormations) {
        if (this.centres != null) {
            this.centres.forEach(i -> i.setPays(null));
        }
        if (centreFormations != null) {
            centreFormations.forEach(i -> i.setPays(this));
        }
        this.centres = centreFormations;
    }

    public Pays centres(Set<CentreFormation> centreFormations) {
        this.setCentres(centreFormations);
        return this;
    }

    public Pays addCentre(CentreFormation centreFormation) {
        this.centres.add(centreFormation);
        centreFormation.setPays(this);
        return this;
    }

    public Pays removeCentre(CentreFormation centreFormation) {
        this.centres.remove(centreFormation);
        centreFormation.setPays(null);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.setPays(null));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.setPays(this));
        }
        this.etudiants = etudiants;
    }

    public Pays etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Pays addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setPays(this);
        return this;
    }

    public Pays removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setPays(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pays)) {
            return false;
        }
        return getId() != null && getId().equals(((Pays) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pays{" +
            "id=" + getId() +
            ", codeIso='" + getCodeIso() + "'" +
            ", nom='" + getNom() + "'" +
            ", langue='" + getLangue() + "'" +
            ", fuseau='" + getFuseau() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
