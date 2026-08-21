package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.EvaluationPrevue} entity. This class is used
 * in {@link org.forbidec.web.rest.EvaluationPrevueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evaluation-prevues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationPrevueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter intitule;

    private StringFilter libelleImpression;

    private BigDecimalFilter coefficient;

    private BooleanFilter compteDansMoyenne;

    private BigDecimalFilter noteMaximale;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private StringFilter commentaire;

    private LongFilter cycleId;

    private LongFilter enseignantId;

    private LongFilter matiereId;

    private LongFilter sousMatiereId;

    private LongFilter coursId;

    private LongFilter typeTacheId;

    private LongFilter noteId;

    private Boolean distinct;

    public EvaluationPrevueCriteria() {}

    public EvaluationPrevueCriteria(EvaluationPrevueCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.intitule = other.optionalIntitule().map(StringFilter::copy).orElse(null);
        this.libelleImpression = other.optionalLibelleImpression().map(StringFilter::copy).orElse(null);
        this.coefficient = other.optionalCoefficient().map(BigDecimalFilter::copy).orElse(null);
        this.compteDansMoyenne = other.optionalCompteDansMoyenne().map(BooleanFilter::copy).orElse(null);
        this.noteMaximale = other.optionalNoteMaximale().map(BigDecimalFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(LocalDateFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(LocalDateFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.cycleId = other.optionalCycleId().map(LongFilter::copy).orElse(null);
        this.enseignantId = other.optionalEnseignantId().map(LongFilter::copy).orElse(null);
        this.matiereId = other.optionalMatiereId().map(LongFilter::copy).orElse(null);
        this.sousMatiereId = other.optionalSousMatiereId().map(LongFilter::copy).orElse(null);
        this.coursId = other.optionalCoursId().map(LongFilter::copy).orElse(null);
        this.typeTacheId = other.optionalTypeTacheId().map(LongFilter::copy).orElse(null);
        this.noteId = other.optionalNoteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EvaluationPrevueCriteria copy() {
        return new EvaluationPrevueCriteria(this);
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

    public StringFilter getIntitule() {
        return intitule;
    }

    public Optional<StringFilter> optionalIntitule() {
        return Optional.ofNullable(intitule);
    }

    public StringFilter intitule() {
        if (intitule == null) {
            setIntitule(new StringFilter());
        }
        return intitule;
    }

    public void setIntitule(StringFilter intitule) {
        this.intitule = intitule;
    }

    public StringFilter getLibelleImpression() {
        return libelleImpression;
    }

    public Optional<StringFilter> optionalLibelleImpression() {
        return Optional.ofNullable(libelleImpression);
    }

    public StringFilter libelleImpression() {
        if (libelleImpression == null) {
            setLibelleImpression(new StringFilter());
        }
        return libelleImpression;
    }

    public void setLibelleImpression(StringFilter libelleImpression) {
        this.libelleImpression = libelleImpression;
    }

    public BigDecimalFilter getCoefficient() {
        return coefficient;
    }

    public Optional<BigDecimalFilter> optionalCoefficient() {
        return Optional.ofNullable(coefficient);
    }

    public BigDecimalFilter coefficient() {
        if (coefficient == null) {
            setCoefficient(new BigDecimalFilter());
        }
        return coefficient;
    }

    public void setCoefficient(BigDecimalFilter coefficient) {
        this.coefficient = coefficient;
    }

    public BooleanFilter getCompteDansMoyenne() {
        return compteDansMoyenne;
    }

    public Optional<BooleanFilter> optionalCompteDansMoyenne() {
        return Optional.ofNullable(compteDansMoyenne);
    }

    public BooleanFilter compteDansMoyenne() {
        if (compteDansMoyenne == null) {
            setCompteDansMoyenne(new BooleanFilter());
        }
        return compteDansMoyenne;
    }

    public void setCompteDansMoyenne(BooleanFilter compteDansMoyenne) {
        this.compteDansMoyenne = compteDansMoyenne;
    }

    public BigDecimalFilter getNoteMaximale() {
        return noteMaximale;
    }

    public Optional<BigDecimalFilter> optionalNoteMaximale() {
        return Optional.ofNullable(noteMaximale);
    }

    public BigDecimalFilter noteMaximale() {
        if (noteMaximale == null) {
            setNoteMaximale(new BigDecimalFilter());
        }
        return noteMaximale;
    }

    public void setNoteMaximale(BigDecimalFilter noteMaximale) {
        this.noteMaximale = noteMaximale;
    }

    public LocalDateFilter getDateDebut() {
        return dateDebut;
    }

    public Optional<LocalDateFilter> optionalDateDebut() {
        return Optional.ofNullable(dateDebut);
    }

    public LocalDateFilter dateDebut() {
        if (dateDebut == null) {
            setDateDebut(new LocalDateFilter());
        }
        return dateDebut;
    }

    public void setDateDebut(LocalDateFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateFilter getDateFin() {
        return dateFin;
    }

    public Optional<LocalDateFilter> optionalDateFin() {
        return Optional.ofNullable(dateFin);
    }

    public LocalDateFilter dateFin() {
        if (dateFin == null) {
            setDateFin(new LocalDateFilter());
        }
        return dateFin;
    }

    public void setDateFin(LocalDateFilter dateFin) {
        this.dateFin = dateFin;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public Optional<StringFilter> optionalCommentaire() {
        return Optional.ofNullable(commentaire);
    }

    public StringFilter commentaire() {
        if (commentaire == null) {
            setCommentaire(new StringFilter());
        }
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
    }

    public LongFilter getCycleId() {
        return cycleId;
    }

    public Optional<LongFilter> optionalCycleId() {
        return Optional.ofNullable(cycleId);
    }

    public LongFilter cycleId() {
        if (cycleId == null) {
            setCycleId(new LongFilter());
        }
        return cycleId;
    }

    public void setCycleId(LongFilter cycleId) {
        this.cycleId = cycleId;
    }

    public LongFilter getEnseignantId() {
        return enseignantId;
    }

    public Optional<LongFilter> optionalEnseignantId() {
        return Optional.ofNullable(enseignantId);
    }

    public LongFilter enseignantId() {
        if (enseignantId == null) {
            setEnseignantId(new LongFilter());
        }
        return enseignantId;
    }

    public void setEnseignantId(LongFilter enseignantId) {
        this.enseignantId = enseignantId;
    }

    public LongFilter getMatiereId() {
        return matiereId;
    }

    public Optional<LongFilter> optionalMatiereId() {
        return Optional.ofNullable(matiereId);
    }

    public LongFilter matiereId() {
        if (matiereId == null) {
            setMatiereId(new LongFilter());
        }
        return matiereId;
    }

    public void setMatiereId(LongFilter matiereId) {
        this.matiereId = matiereId;
    }

    public LongFilter getSousMatiereId() {
        return sousMatiereId;
    }

    public Optional<LongFilter> optionalSousMatiereId() {
        return Optional.ofNullable(sousMatiereId);
    }

    public LongFilter sousMatiereId() {
        if (sousMatiereId == null) {
            setSousMatiereId(new LongFilter());
        }
        return sousMatiereId;
    }

    public void setSousMatiereId(LongFilter sousMatiereId) {
        this.sousMatiereId = sousMatiereId;
    }

    public LongFilter getCoursId() {
        return coursId;
    }

    public Optional<LongFilter> optionalCoursId() {
        return Optional.ofNullable(coursId);
    }

    public LongFilter coursId() {
        if (coursId == null) {
            setCoursId(new LongFilter());
        }
        return coursId;
    }

    public void setCoursId(LongFilter coursId) {
        this.coursId = coursId;
    }

    public LongFilter getTypeTacheId() {
        return typeTacheId;
    }

    public Optional<LongFilter> optionalTypeTacheId() {
        return Optional.ofNullable(typeTacheId);
    }

    public LongFilter typeTacheId() {
        if (typeTacheId == null) {
            setTypeTacheId(new LongFilter());
        }
        return typeTacheId;
    }

    public void setTypeTacheId(LongFilter typeTacheId) {
        this.typeTacheId = typeTacheId;
    }

    public LongFilter getNoteId() {
        return noteId;
    }

    public Optional<LongFilter> optionalNoteId() {
        return Optional.ofNullable(noteId);
    }

    public LongFilter noteId() {
        if (noteId == null) {
            setNoteId(new LongFilter());
        }
        return noteId;
    }

    public void setNoteId(LongFilter noteId) {
        this.noteId = noteId;
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
        final EvaluationPrevueCriteria that = (EvaluationPrevueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(intitule, that.intitule) &&
            Objects.equals(libelleImpression, that.libelleImpression) &&
            Objects.equals(coefficient, that.coefficient) &&
            Objects.equals(compteDansMoyenne, that.compteDansMoyenne) &&
            Objects.equals(noteMaximale, that.noteMaximale) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(cycleId, that.cycleId) &&
            Objects.equals(enseignantId, that.enseignantId) &&
            Objects.equals(matiereId, that.matiereId) &&
            Objects.equals(sousMatiereId, that.sousMatiereId) &&
            Objects.equals(coursId, that.coursId) &&
            Objects.equals(typeTacheId, that.typeTacheId) &&
            Objects.equals(noteId, that.noteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            intitule,
            libelleImpression,
            coefficient,
            compteDansMoyenne,
            noteMaximale,
            dateDebut,
            dateFin,
            commentaire,
            cycleId,
            enseignantId,
            matiereId,
            sousMatiereId,
            coursId,
            typeTacheId,
            noteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationPrevueCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIntitule().map(f -> "intitule=" + f + ", ").orElse("") +
            optionalLibelleImpression().map(f -> "libelleImpression=" + f + ", ").orElse("") +
            optionalCoefficient().map(f -> "coefficient=" + f + ", ").orElse("") +
            optionalCompteDansMoyenne().map(f -> "compteDansMoyenne=" + f + ", ").orElse("") +
            optionalNoteMaximale().map(f -> "noteMaximale=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalCycleId().map(f -> "cycleId=" + f + ", ").orElse("") +
            optionalEnseignantId().map(f -> "enseignantId=" + f + ", ").orElse("") +
            optionalMatiereId().map(f -> "matiereId=" + f + ", ").orElse("") +
            optionalSousMatiereId().map(f -> "sousMatiereId=" + f + ", ").orElse("") +
            optionalCoursId().map(f -> "coursId=" + f + ", ").orElse("") +
            optionalTypeTacheId().map(f -> "typeTacheId=" + f + ", ").orElse("") +
            optionalNoteId().map(f -> "noteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
