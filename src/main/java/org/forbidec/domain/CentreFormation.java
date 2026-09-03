package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Centre de formation. Porte les blocs d'impression du bulletin
 * (en-tête, ville de signature, signataire) et les règles pédagogiques
 * locales : nombre de cycles du cursus, note maximale.
 */
@Entity
@Table(name = "centre_formation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(name = "paysFilter", condition = "pays_id in (:paysIds)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CentreFormation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @NotNull
    @Size(max = 150)
    @Column(name = "nom", length = 150, nullable = false)
    private String nom;

    @NotNull
    @Size(max = 100)
    @Column(name = "ville", length = 100, nullable = false)
    private String ville;

    @Size(max = 255)
    @Column(name = "adresse", length = 255)
    private String adresse;

    @Lob
    @Column(name = "entete_document")
    private String enteteDocument;

    @NotNull
    @Size(max = 100)
    @Column(name = "signataire", length = 100, nullable = false)
    private String signataire;

    @Size(max = 255)
    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @NotNull
    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "nb_cycles_cursus", nullable = false)
    private Integer nbCyclesCursus;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "note_maximale", precision = 21, scale = 2, nullable = false)
    private BigDecimal noteMaximale;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "centres", "etudiants" }, allowSetters = true)
    private Pays pays;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "centre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "centre" }, allowSetters = true)
    private Set<BaremeMention> baremes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "centre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "centre" }, allowSetters = true)
    private Set<Parametre> parametres = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "centre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "centre", "inscriptions", "evaluations", "habilitations" }, allowSetters = true)
    private Set<Cycle> cycles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "centre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "centre", "cycle" }, allowSetters = true)
    private Set<HabilitationCycle> habilitations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CentreFormation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CentreFormation code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public CentreFormation nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return this.ville;
    }

    public CentreFormation ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public CentreFormation adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEnteteDocument() {
        return this.enteteDocument;
    }

    public CentreFormation enteteDocument(String enteteDocument) {
        this.setEnteteDocument(enteteDocument);
        return this;
    }

    public void setEnteteDocument(String enteteDocument) {
        this.enteteDocument = enteteDocument;
    }

    public String getSignataire() {
        return this.signataire;
    }

    public CentreFormation signataire(String signataire) {
        this.setSignataire(signataire);
        return this;
    }

    public void setSignataire(String signataire) {
        this.signataire = signataire;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public CentreFormation logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getNbCyclesCursus() {
        return this.nbCyclesCursus;
    }

    public CentreFormation nbCyclesCursus(Integer nbCyclesCursus) {
        this.setNbCyclesCursus(nbCyclesCursus);
        return this;
    }

    public void setNbCyclesCursus(Integer nbCyclesCursus) {
        this.nbCyclesCursus = nbCyclesCursus;
    }

    public BigDecimal getNoteMaximale() {
        return this.noteMaximale;
    }

    public CentreFormation noteMaximale(BigDecimal noteMaximale) {
        this.setNoteMaximale(noteMaximale);
        return this;
    }

    public void setNoteMaximale(BigDecimal noteMaximale) {
        this.noteMaximale = noteMaximale;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public CentreFormation actif(Boolean actif) {
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

    public CentreFormation pays(Pays pays) {
        this.setPays(pays);
        return this;
    }

    public Set<BaremeMention> getBaremes() {
        return this.baremes;
    }

    public void setBaremes(Set<BaremeMention> baremeMentions) {
        if (this.baremes != null) {
            this.baremes.forEach(i -> i.setCentre(null));
        }
        if (baremeMentions != null) {
            baremeMentions.forEach(i -> i.setCentre(this));
        }
        this.baremes = baremeMentions;
    }

    public CentreFormation baremes(Set<BaremeMention> baremeMentions) {
        this.setBaremes(baremeMentions);
        return this;
    }

    public CentreFormation addBareme(BaremeMention baremeMention) {
        this.baremes.add(baremeMention);
        baremeMention.setCentre(this);
        return this;
    }

    public CentreFormation removeBareme(BaremeMention baremeMention) {
        this.baremes.remove(baremeMention);
        baremeMention.setCentre(null);
        return this;
    }

    public Set<Parametre> getParametres() {
        return this.parametres;
    }

    public void setParametres(Set<Parametre> parametres) {
        if (this.parametres != null) {
            this.parametres.forEach(i -> i.setCentre(null));
        }
        if (parametres != null) {
            parametres.forEach(i -> i.setCentre(this));
        }
        this.parametres = parametres;
    }

    public CentreFormation parametres(Set<Parametre> parametres) {
        this.setParametres(parametres);
        return this;
    }

    public CentreFormation addParametre(Parametre parametre) {
        this.parametres.add(parametre);
        parametre.setCentre(this);
        return this;
    }

    public CentreFormation removeParametre(Parametre parametre) {
        this.parametres.remove(parametre);
        parametre.setCentre(null);
        return this;
    }

    public Set<Cycle> getCycles() {
        return this.cycles;
    }

    public void setCycles(Set<Cycle> cycles) {
        if (this.cycles != null) {
            this.cycles.forEach(i -> i.setCentre(null));
        }
        if (cycles != null) {
            cycles.forEach(i -> i.setCentre(this));
        }
        this.cycles = cycles;
    }

    public CentreFormation cycles(Set<Cycle> cycles) {
        this.setCycles(cycles);
        return this;
    }

    public CentreFormation addCycle(Cycle cycle) {
        this.cycles.add(cycle);
        cycle.setCentre(this);
        return this;
    }

    public CentreFormation removeCycle(Cycle cycle) {
        this.cycles.remove(cycle);
        cycle.setCentre(null);
        return this;
    }

    public Set<HabilitationCycle> getHabilitations() {
        return this.habilitations;
    }

    public void setHabilitations(Set<HabilitationCycle> habilitationCycles) {
        if (this.habilitations != null) {
            this.habilitations.forEach(i -> i.setCentre(null));
        }
        if (habilitationCycles != null) {
            habilitationCycles.forEach(i -> i.setCentre(this));
        }
        this.habilitations = habilitationCycles;
    }

    public CentreFormation habilitations(Set<HabilitationCycle> habilitationCycles) {
        this.setHabilitations(habilitationCycles);
        return this;
    }

    public CentreFormation addHabilitation(HabilitationCycle habilitationCycle) {
        this.habilitations.add(habilitationCycle);
        habilitationCycle.setCentre(this);
        return this;
    }

    public CentreFormation removeHabilitation(HabilitationCycle habilitationCycle) {
        this.habilitations.remove(habilitationCycle);
        habilitationCycle.setCentre(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CentreFormation)) {
            return false;
        }
        return getId() != null && getId().equals(((CentreFormation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CentreFormation{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", ville='" + getVille() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", enteteDocument='" + getEnteteDocument() + "'" +
            ", signataire='" + getSignataire() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", nbCyclesCursus=" + getNbCyclesCursus() +
            ", noteMaximale=" + getNoteMaximale() +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
