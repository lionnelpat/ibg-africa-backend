package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Barème des mentions. Reconstitué depuis le pied de page du bulletin :
 * [19-20] Excellent · [17-19[ Très-Bien · [15-17[ Bien
 * [12-15[ Assez-Bien · [10-12[ Suffisant · <10 Insuffisant
 * Rattaché à un centre si celui-ci applique un barème propre ;
 * sans rattachement, le barème est commun à tous les centres.
 */
@Entity
@Table(name = "bareme_mention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(
    name = "paysFilter",
    condition = "centre_id is null or centre_id in (select cf.id from centre_formation cf where cf.pays_id in (:paysIds))"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BaremeMention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "libelle_long", length = 100, nullable = false)
    private String libelleLong;

    @NotNull
    @Size(max = 50)
    @Column(name = "libelle_court", length = 50, nullable = false)
    private String libelleCourt;

    @DecimalMin(value = "0")
    @Column(name = "borne_min", precision = 21, scale = 2)
    private BigDecimal borneMin;

    @NotNull
    @Column(name = "min_inclus", nullable = false)
    private Boolean minInclus;

    @DecimalMin(value = "0")
    @Column(name = "borne_max", precision = 21, scale = 2)
    private BigDecimal borneMax;

    @NotNull
    @Column(name = "max_inclus", nullable = false)
    private Boolean maxInclus;

    @NotNull
    @Column(name = "ordre_affichage", nullable = false)
    private Integer ordreAffichage;

    @Size(max = 255)
    @Column(name = "commentaire", length = 255)
    private String commentaire;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pays", "baremes", "parametres", "cycles", "habilitations" }, allowSetters = true)
    private CentreFormation centre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BaremeMention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelleLong() {
        return this.libelleLong;
    }

    public BaremeMention libelleLong(String libelleLong) {
        this.setLibelleLong(libelleLong);
        return this;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public String getLibelleCourt() {
        return this.libelleCourt;
    }

    public BaremeMention libelleCourt(String libelleCourt) {
        this.setLibelleCourt(libelleCourt);
        return this;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public BigDecimal getBorneMin() {
        return this.borneMin;
    }

    public BaremeMention borneMin(BigDecimal borneMin) {
        this.setBorneMin(borneMin);
        return this;
    }

    public void setBorneMin(BigDecimal borneMin) {
        this.borneMin = borneMin;
    }

    public Boolean getMinInclus() {
        return this.minInclus;
    }

    public BaremeMention minInclus(Boolean minInclus) {
        this.setMinInclus(minInclus);
        return this;
    }

    public void setMinInclus(Boolean minInclus) {
        this.minInclus = minInclus;
    }

    public BigDecimal getBorneMax() {
        return this.borneMax;
    }

    public BaremeMention borneMax(BigDecimal borneMax) {
        this.setBorneMax(borneMax);
        return this;
    }

    public void setBorneMax(BigDecimal borneMax) {
        this.borneMax = borneMax;
    }

    public Boolean getMaxInclus() {
        return this.maxInclus;
    }

    public BaremeMention maxInclus(Boolean maxInclus) {
        this.setMaxInclus(maxInclus);
        return this;
    }

    public void setMaxInclus(Boolean maxInclus) {
        this.maxInclus = maxInclus;
    }

    public Integer getOrdreAffichage() {
        return this.ordreAffichage;
    }

    public BaremeMention ordreAffichage(Integer ordreAffichage) {
        this.setOrdreAffichage(ordreAffichage);
        return this;
    }

    public void setOrdreAffichage(Integer ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public BaremeMention commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public BaremeMention actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public CentreFormation getCentre() {
        return this.centre;
    }

    public void setCentre(CentreFormation centreFormation) {
        this.centre = centreFormation;
    }

    public BaremeMention centre(CentreFormation centreFormation) {
        this.setCentre(centreFormation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaremeMention)) {
            return false;
        }
        return getId() != null && getId().equals(((BaremeMention) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaremeMention{" +
            "id=" + getId() +
            ", libelleLong='" + getLibelleLong() + "'" +
            ", libelleCourt='" + getLibelleCourt() + "'" +
            ", borneMin=" + getBorneMin() +
            ", minInclus='" + getMinInclus() + "'" +
            ", borneMax=" + getBorneMax() +
            ", maxInclus='" + getMaxInclus() + "'" +
            ", ordreAffichage=" + getOrdreAffichage() +
            ", commentaire='" + getCommentaire() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
