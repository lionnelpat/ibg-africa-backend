package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.domain.enumeration.StatutNote;
import org.forbidec.domain.enumeration.StatutNote;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.HistoriqueNote} entity. This class is used
 * in {@link org.forbidec.web.rest.HistoriqueNoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historique-notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriqueNoteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutNote
     */
    public static class StatutNoteFilter extends Filter<StatutNote> {

        public StatutNoteFilter() {}

        public StatutNoteFilter(StatutNoteFilter filter) {
            super(filter);
        }

        @Override
        public StatutNoteFilter copy() {
            return new StatutNoteFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter noteAvant;

    private BigDecimalFilter noteApres;

    private StatutNoteFilter statutAvant;

    private StatutNoteFilter statutApres;

    private StringFilter motif;

    private StringFilter modifiePar;

    private InstantFilter modifieLe;

    private LongFilter evaluationRealiseeId;

    private Boolean distinct;

    public HistoriqueNoteCriteria() {}

    public HistoriqueNoteCriteria(HistoriqueNoteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.noteAvant = other.optionalNoteAvant().map(BigDecimalFilter::copy).orElse(null);
        this.noteApres = other.optionalNoteApres().map(BigDecimalFilter::copy).orElse(null);
        this.statutAvant = other.optionalStatutAvant().map(StatutNoteFilter::copy).orElse(null);
        this.statutApres = other.optionalStatutApres().map(StatutNoteFilter::copy).orElse(null);
        this.motif = other.optionalMotif().map(StringFilter::copy).orElse(null);
        this.modifiePar = other.optionalModifiePar().map(StringFilter::copy).orElse(null);
        this.modifieLe = other.optionalModifieLe().map(InstantFilter::copy).orElse(null);
        this.evaluationRealiseeId = other.optionalEvaluationRealiseeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoriqueNoteCriteria copy() {
        return new HistoriqueNoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getNoteAvant() {
        return noteAvant;
    }

    public Optional<BigDecimalFilter> optionalNoteAvant() {
        return Optional.ofNullable(noteAvant);
    }

    public BigDecimalFilter noteAvant() {
        if (noteAvant == null) {
            setNoteAvant(new BigDecimalFilter());
        }
        return noteAvant;
    }

    public void setNoteAvant(BigDecimalFilter noteAvant) {
        this.noteAvant = noteAvant;
    }

    public BigDecimalFilter getNoteApres() {
        return noteApres;
    }

    public Optional<BigDecimalFilter> optionalNoteApres() {
        return Optional.ofNullable(noteApres);
    }

    public BigDecimalFilter noteApres() {
        if (noteApres == null) {
            setNoteApres(new BigDecimalFilter());
        }
        return noteApres;
    }

    public void setNoteApres(BigDecimalFilter noteApres) {
        this.noteApres = noteApres;
    }

    public StatutNoteFilter getStatutAvant() {
        return statutAvant;
    }

    public Optional<StatutNoteFilter> optionalStatutAvant() {
        return Optional.ofNullable(statutAvant);
    }

    public StatutNoteFilter statutAvant() {
        if (statutAvant == null) {
            setStatutAvant(new StatutNoteFilter());
        }
        return statutAvant;
    }

    public void setStatutAvant(StatutNoteFilter statutAvant) {
        this.statutAvant = statutAvant;
    }

    public StatutNoteFilter getStatutApres() {
        return statutApres;
    }

    public Optional<StatutNoteFilter> optionalStatutApres() {
        return Optional.ofNullable(statutApres);
    }

    public StatutNoteFilter statutApres() {
        if (statutApres == null) {
            setStatutApres(new StatutNoteFilter());
        }
        return statutApres;
    }

    public void setStatutApres(StatutNoteFilter statutApres) {
        this.statutApres = statutApres;
    }

    public StringFilter getMotif() {
        return motif;
    }

    public Optional<StringFilter> optionalMotif() {
        return Optional.ofNullable(motif);
    }

    public StringFilter motif() {
        if (motif == null) {
            setMotif(new StringFilter());
        }
        return motif;
    }

    public void setMotif(StringFilter motif) {
        this.motif = motif;
    }

    public StringFilter getModifiePar() {
        return modifiePar;
    }

    public Optional<StringFilter> optionalModifiePar() {
        return Optional.ofNullable(modifiePar);
    }

    public StringFilter modifiePar() {
        if (modifiePar == null) {
            setModifiePar(new StringFilter());
        }
        return modifiePar;
    }

    public void setModifiePar(StringFilter modifiePar) {
        this.modifiePar = modifiePar;
    }

    public InstantFilter getModifieLe() {
        return modifieLe;
    }

    public Optional<InstantFilter> optionalModifieLe() {
        return Optional.ofNullable(modifieLe);
    }

    public InstantFilter modifieLe() {
        if (modifieLe == null) {
            setModifieLe(new InstantFilter());
        }
        return modifieLe;
    }

    public void setModifieLe(InstantFilter modifieLe) {
        this.modifieLe = modifieLe;
    }

    public LongFilter getEvaluationRealiseeId() {
        return evaluationRealiseeId;
    }

    public Optional<LongFilter> optionalEvaluationRealiseeId() {
        return Optional.ofNullable(evaluationRealiseeId);
    }

    public LongFilter evaluationRealiseeId() {
        if (evaluationRealiseeId == null) {
            setEvaluationRealiseeId(new LongFilter());
        }
        return evaluationRealiseeId;
    }

    public void setEvaluationRealiseeId(LongFilter evaluationRealiseeId) {
        this.evaluationRealiseeId = evaluationRealiseeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoriqueNoteCriteria that = (HistoriqueNoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(noteAvant, that.noteAvant) &&
            Objects.equals(noteApres, that.noteApres) &&
            Objects.equals(statutAvant, that.statutAvant) &&
            Objects.equals(statutApres, that.statutApres) &&
            Objects.equals(motif, that.motif) &&
            Objects.equals(modifiePar, that.modifiePar) &&
            Objects.equals(modifieLe, that.modifieLe) &&
            Objects.equals(evaluationRealiseeId, that.evaluationRealiseeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            noteAvant,
            noteApres,
            statutAvant,
            statutApres,
            motif,
            modifiePar,
            modifieLe,
            evaluationRealiseeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriqueNoteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNoteAvant().map(f -> "noteAvant=" + f + ", ").orElse("") +
            optionalNoteApres().map(f -> "noteApres=" + f + ", ").orElse("") +
            optionalStatutAvant().map(f -> "statutAvant=" + f + ", ").orElse("") +
            optionalStatutApres().map(f -> "statutApres=" + f + ", ").orElse("") +
            optionalMotif().map(f -> "motif=" + f + ", ").orElse("") +
            optionalModifiePar().map(f -> "modifiePar=" + f + ", ").orElse("") +
            optionalModifieLe().map(f -> "modifieLe=" + f + ", ").orElse("") +
            optionalEvaluationRealiseeId().map(f -> "evaluationRealiseeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
