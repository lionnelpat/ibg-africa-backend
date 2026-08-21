package org.forbidec.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.forbidec.domain.enumeration.StatutNote;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Journal des modifications de note. Absent d'Access, requis en multi-utilisateur.
 */
@Entity
@Table(name = "historique_note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "note_avant", precision = 21, scale = 2)
    private BigDecimal noteAvant;

    @Column(name = "note_apres", precision = 21, scale = 2)
    private BigDecimal noteApres;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_avant")
    private StatutNote statutAvant;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_apres")
    private StatutNote statutApres;

    @Size(max = 255)
    @Column(name = "motif", length = 255)
    private String motif;

    @NotNull
    @Size(max = 64)
    @Column(name = "modifie_par", length = 64, nullable = false)
    private String modifiePar;

    @NotNull
    @Column(name = "modifie_le", nullable = false)
    private Instant modifieLe;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "evaluationPrevue", "etudiant", "historiques" }, allowSetters = true)
    private EvaluationRealisee evaluationRealisee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriqueNote id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNoteAvant() {
        return this.noteAvant;
    }

    public HistoriqueNote noteAvant(BigDecimal noteAvant) {
        this.setNoteAvant(noteAvant);
        return this;
    }

    public void setNoteAvant(BigDecimal noteAvant) {
        this.noteAvant = noteAvant;
    }

    public BigDecimal getNoteApres() {
        return this.noteApres;
    }

    public HistoriqueNote noteApres(BigDecimal noteApres) {
        this.setNoteApres(noteApres);
        return this;
    }

    public void setNoteApres(BigDecimal noteApres) {
        this.noteApres = noteApres;
    }

    public StatutNote getStatutAvant() {
        return this.statutAvant;
    }

    public HistoriqueNote statutAvant(StatutNote statutAvant) {
        this.setStatutAvant(statutAvant);
        return this;
    }

    public void setStatutAvant(StatutNote statutAvant) {
        this.statutAvant = statutAvant;
    }

    public StatutNote getStatutApres() {
        return this.statutApres;
    }

    public HistoriqueNote statutApres(StatutNote statutApres) {
        this.setStatutApres(statutApres);
        return this;
    }

    public void setStatutApres(StatutNote statutApres) {
        this.statutApres = statutApres;
    }

    public String getMotif() {
        return this.motif;
    }

    public HistoriqueNote motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getModifiePar() {
        return this.modifiePar;
    }

    public HistoriqueNote modifiePar(String modifiePar) {
        this.setModifiePar(modifiePar);
        return this;
    }

    public void setModifiePar(String modifiePar) {
        this.modifiePar = modifiePar;
    }

    public Instant getModifieLe() {
        return this.modifieLe;
    }

    public HistoriqueNote modifieLe(Instant modifieLe) {
        this.setModifieLe(modifieLe);
        return this;
    }

    public void setModifieLe(Instant modifieLe) {
        this.modifieLe = modifieLe;
    }

    public EvaluationRealisee getEvaluationRealisee() {
        return this.evaluationRealisee;
    }

    public void setEvaluationRealisee(EvaluationRealisee evaluationRealisee) {
        this.evaluationRealisee = evaluationRealisee;
    }

    public HistoriqueNote evaluationRealisee(EvaluationRealisee evaluationRealisee) {
        this.setEvaluationRealisee(evaluationRealisee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriqueNote)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriqueNote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueNote{" +
            "id=" + getId() +
            ", noteAvant=" + getNoteAvant() +
            ", noteApres=" + getNoteApres() +
            ", statutAvant='" + getStatutAvant() + "'" +
            ", statutApres='" + getStatutApres() + "'" +
            ", motif='" + getMotif() + "'" +
            ", modifiePar='" + getModifiePar() + "'" +
            ", modifieLe='" + getModifieLe() + "'" +
            "}";
    }
}
