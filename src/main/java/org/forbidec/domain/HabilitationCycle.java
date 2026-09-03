package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.forbidec.domain.enumeration.RoleFonctionnel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;

/**
 * Périmètre fonctionnel d'un utilisateur. L'authentification reste dans
 * Keycloak ; seul le périmètre métier est stocké ici.
 * centre et cycle nuls = portée globale.
 */
@Schema(
    description = "Périmètre fonctionnel d'un utilisateur. L'authentification reste dans\nKeycloak ; seul le périmètre métier est stocké ici.\ncentre et cycle nuls = portée globale."
)
@Entity
@Table(name = "habilitation_cycle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Filter(
    name = "paysFilter",
    condition = "centre_id is null or centre_id in (select cf.id from centre_formation cf where cf.pays_id in (:paysIds))"
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HabilitationCycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "keycloak_user_id", length = 64, nullable = false)
    private String keycloakUserId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role_fonctionnel", nullable = false)
    private RoleFonctionnel roleFonctionnel;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pays", "baremes", "parametres", "cycles", "habilitations" }, allowSetters = true)
    private CentreFormation centre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "centre", "inscriptions", "evaluations", "habilitations" }, allowSetters = true)
    private Cycle cycle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HabilitationCycle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeycloakUserId() {
        return this.keycloakUserId;
    }

    public HabilitationCycle keycloakUserId(String keycloakUserId) {
        this.setKeycloakUserId(keycloakUserId);
        return this;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public RoleFonctionnel getRoleFonctionnel() {
        return this.roleFonctionnel;
    }

    public HabilitationCycle roleFonctionnel(RoleFonctionnel roleFonctionnel) {
        this.setRoleFonctionnel(roleFonctionnel);
        return this;
    }

    public void setRoleFonctionnel(RoleFonctionnel roleFonctionnel) {
        this.roleFonctionnel = roleFonctionnel;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public HabilitationCycle dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public HabilitationCycle dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public CentreFormation getCentre() {
        return this.centre;
    }

    public void setCentre(CentreFormation centreFormation) {
        this.centre = centreFormation;
    }

    public HabilitationCycle centre(CentreFormation centreFormation) {
        this.setCentre(centreFormation);
        return this;
    }

    public Cycle getCycle() {
        return this.cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public HabilitationCycle cycle(Cycle cycle) {
        this.setCycle(cycle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HabilitationCycle)) {
            return false;
        }
        return getId() != null && getId().equals(((HabilitationCycle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HabilitationCycle{" +
            "id=" + getId() +
            ", keycloakUserId='" + getKeycloakUserId() + "'" +
            ", roleFonctionnel='" + getRoleFonctionnel() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
