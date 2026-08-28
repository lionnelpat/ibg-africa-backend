package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.forbidec.domain.enumeration.TypeValeur;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Remplace tVariables. Sans centre rattaché, le paramètre est global.
 */
@Schema(description = "Remplace tVariables. Sans centre rattaché, le paramètre est global.")
@Entity
@Table(name = "parametre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(
    name = "paysFilter",
    condition = "centre_id is null or centre_id in (select cf.id from centre_formation cf where cf.pays_id in (:paysIds))"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Parametre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "cle", length = 80, nullable = false, unique = true)
    private String cle;

    @Size(max = 255)
    @Column(name = "libelle", length = 255)
    private String libelle;

    @Size(max = 500)
    @Column(name = "valeur", length = 500)
    private String valeur;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_valeur", nullable = false)
    private TypeValeur typeValeur;

    @NotNull
    @Column(name = "modifiable_ui", nullable = false)
    private Boolean modifiableUi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pays", "baremes", "parametres", "cycles", "habilitations" }, allowSetters = true)
    private CentreFormation centre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Parametre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCle() {
        return this.cle;
    }

    public Parametre cle(String cle) {
        this.setCle(cle);
        return this;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Parametre libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getValeur() {
        return this.valeur;
    }

    public Parametre valeur(String valeur) {
        this.setValeur(valeur);
        return this;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public TypeValeur getTypeValeur() {
        return this.typeValeur;
    }

    public Parametre typeValeur(TypeValeur typeValeur) {
        this.setTypeValeur(typeValeur);
        return this;
    }

    public void setTypeValeur(TypeValeur typeValeur) {
        this.typeValeur = typeValeur;
    }

    public Boolean getModifiableUi() {
        return this.modifiableUi;
    }

    public Parametre modifiableUi(Boolean modifiableUi) {
        this.setModifiableUi(modifiableUi);
        return this;
    }

    public void setModifiableUi(Boolean modifiableUi) {
        this.modifiableUi = modifiableUi;
    }

    public CentreFormation getCentre() {
        return this.centre;
    }

    public void setCentre(CentreFormation centreFormation) {
        this.centre = centreFormation;
    }

    public Parametre centre(CentreFormation centreFormation) {
        this.setCentre(centreFormation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parametre)) {
            return false;
        }
        return getId() != null && getId().equals(((Parametre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parametre{" +
            "id=" + getId() +
            ", cle='" + getCle() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", valeur='" + getValeur() + "'" +
            ", typeValeur='" + getTypeValeur() + "'" +
            ", modifiableUi='" + getModifiableUi() + "'" +
            "}";
    }
}
