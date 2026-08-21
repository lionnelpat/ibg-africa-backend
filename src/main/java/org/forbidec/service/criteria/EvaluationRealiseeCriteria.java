package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.forbidec.domain.enumeration.StatutNote;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.EvaluationRealisee} entity. This class is used
 * in {@link org.forbidec.web.rest.EvaluationRealiseeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evaluation-realisees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvaluationRealiseeCriteria implements Serializable, Criteria {

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

    private BigDecimalFilter note;

    private StatutNoteFilter statut;

    private BooleanFilter compteDansMoyenne;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private StringFilter commentaire1;

    private StringFilter commentaire2;

    private StringFilter commentaire3;

    private StringFilter saisiePar;

    private InstantFilter saisieLe;

    private StringFilter valideePar;

    private InstantFilter valideeLe;

    private LongFilter evaluationPrevueId;

    private LongFilter etudiantId;

    private LongFilter historiqueId;

    private Boolean distinct;

    public EvaluationRealiseeCriteria() {}

    public EvaluationRealiseeCriteria(EvaluationRealiseeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.note = other.optionalNote().map(BigDecimalFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutNoteFilter::copy).orElse(null);
        this.compteDansMoyenne = other.optionalCompteDansMoyenne().map(BooleanFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(LocalDateFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(LocalDateFilter::copy).orElse(null);
        this.commentaire1 = other.optionalCommentaire1().map(StringFilter::copy).orElse(null);
        this.commentaire2 = other.optionalCommentaire2().map(StringFilter::copy).orElse(null);
        this.commentaire3 = other.optionalCommentaire3().map(StringFilter::copy).orElse(null);
        this.saisiePar = other.optionalSaisiePar().map(StringFilter::copy).orElse(null);
        this.saisieLe = other.optionalSaisieLe().map(InstantFilter::copy).orElse(null);
        this.valideePar = other.optionalValideePar().map(StringFilter::copy).orElse(null);
        this.valideeLe = other.optionalValideeLe().map(InstantFilter::copy).orElse(null);
        this.evaluationPrevueId = other.optionalEvaluationPrevueId().map(LongFilter::copy).orElse(null);
        this.etudiantId = other.optionalEtudiantId().map(LongFilter::copy).orElse(null);
        this.historiqueId = other.optionalHistoriqueId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EvaluationRealiseeCriteria copy() {
        return new EvaluationRealiseeCriteria(this);
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

    public BigDecimalFilter getNote() {
        return note;
    }

    public Optional<BigDecimalFilter> optionalNote() {
        return Optional.ofNullable(note);
    }

    public BigDecimalFilter note() {
        if (note == null) {
            setNote(new BigDecimalFilter());
        }
        return note;
    }

    public void setNote(BigDecimalFilter note) {
        this.note = note;
    }

    public StatutNoteFilter getStatut() {
        return statut;
    }

    public Optional<StatutNoteFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutNoteFilter statut() {
        if (statut == null) {
            setStatut(new StatutNoteFilter());
        }
        return statut;
    }

    public void setStatut(StatutNoteFilter statut) {
        this.statut = statut;
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

    public StringFilter getCommentaire1() {
        return commentaire1;
    }

    public Optional<StringFilter> optionalCommentaire1() {
        return Optional.ofNullable(commentaire1);
    }

    public StringFilter commentaire1() {
        if (commentaire1 == null) {
            setCommentaire1(new StringFilter());
        }
        return commentaire1;
    }

    public void setCommentaire1(StringFilter commentaire1) {
        this.commentaire1 = commentaire1;
    }

    public StringFilter getCommentaire2() {
        return commentaire2;
    }

    public Optional<StringFilter> optionalCommentaire2() {
        return Optional.ofNullable(commentaire2);
    }

    public StringFilter commentaire2() {
        if (commentaire2 == null) {
            setCommentaire2(new StringFilter());
        }
        return commentaire2;
    }

    public void setCommentaire2(StringFilter commentaire2) {
        this.commentaire2 = commentaire2;
    }

    public StringFilter getCommentaire3() {
        return commentaire3;
    }

    public Optional<StringFilter> optionalCommentaire3() {
        return Optional.ofNullable(commentaire3);
    }

    public StringFilter commentaire3() {
        if (commentaire3 == null) {
            setCommentaire3(new StringFilter());
        }
        return commentaire3;
    }

    public void setCommentaire3(StringFilter commentaire3) {
        this.commentaire3 = commentaire3;
    }

    public StringFilter getSaisiePar() {
        return saisiePar;
    }

    public Optional<StringFilter> optionalSaisiePar() {
        return Optional.ofNullable(saisiePar);
    }

    public StringFilter saisiePar() {
        if (saisiePar == null) {
            setSaisiePar(new StringFilter());
        }
        return saisiePar;
    }

    public void setSaisiePar(StringFilter saisiePar) {
        this.saisiePar = saisiePar;
    }

    public InstantFilter getSaisieLe() {
        return saisieLe;
    }

    public Optional<InstantFilter> optionalSaisieLe() {
        return Optional.ofNullable(saisieLe);
    }

    public InstantFilter saisieLe() {
        if (saisieLe == null) {
            setSaisieLe(new InstantFilter());
        }
        return saisieLe;
    }

    public void setSaisieLe(InstantFilter saisieLe) {
        this.saisieLe = saisieLe;
    }

    public StringFilter getValideePar() {
        return valideePar;
    }

    public Optional<StringFilter> optionalValideePar() {
        return Optional.ofNullable(valideePar);
    }

    public StringFilter valideePar() {
        if (valideePar == null) {
            setValideePar(new StringFilter());
        }
        return valideePar;
    }

    public void setValideePar(StringFilter valideePar) {
        this.valideePar = valideePar;
    }

    public InstantFilter getValideeLe() {
        return valideeLe;
    }

    public Optional<InstantFilter> optionalValideeLe() {
        return Optional.ofNullable(valideeLe);
    }

    public InstantFilter valideeLe() {
        if (valideeLe == null) {
            setValideeLe(new InstantFilter());
        }
        return valideeLe;
    }

    public void setValideeLe(InstantFilter valideeLe) {
        this.valideeLe = valideeLe;
    }

    public LongFilter getEvaluationPrevueId() {
        return evaluationPrevueId;
    }

    public Optional<LongFilter> optionalEvaluationPrevueId() {
        return Optional.ofNullable(evaluationPrevueId);
    }

    public LongFilter evaluationPrevueId() {
        if (evaluationPrevueId == null) {
            setEvaluationPrevueId(new LongFilter());
        }
        return evaluationPrevueId;
    }

    public void setEvaluationPrevueId(LongFilter evaluationPrevueId) {
        this.evaluationPrevueId = evaluationPrevueId;
    }

    public LongFilter getEtudiantId() {
        return etudiantId;
    }

    public Optional<LongFilter> optionalEtudiantId() {
        return Optional.ofNullable(etudiantId);
    }

    public LongFilter etudiantId() {
        if (etudiantId == null) {
            setEtudiantId(new LongFilter());
        }
        return etudiantId;
    }

    public void setEtudiantId(LongFilter etudiantId) {
        this.etudiantId = etudiantId;
    }

    public LongFilter getHistoriqueId() {
        return historiqueId;
    }

    public Optional<LongFilter> optionalHistoriqueId() {
        return Optional.ofNullable(historiqueId);
    }

    public LongFilter historiqueId() {
        if (historiqueId == null) {
            setHistoriqueId(new LongFilter());
        }
        return historiqueId;
    }

    public void setHistoriqueId(LongFilter historiqueId) {
        this.historiqueId = historiqueId;
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
        final EvaluationRealiseeCriteria that = (EvaluationRealiseeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(note, that.note) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(compteDansMoyenne, that.compteDansMoyenne) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(commentaire1, that.commentaire1) &&
            Objects.equals(commentaire2, that.commentaire2) &&
            Objects.equals(commentaire3, that.commentaire3) &&
            Objects.equals(saisiePar, that.saisiePar) &&
            Objects.equals(saisieLe, that.saisieLe) &&
            Objects.equals(valideePar, that.valideePar) &&
            Objects.equals(valideeLe, that.valideeLe) &&
            Objects.equals(evaluationPrevueId, that.evaluationPrevueId) &&
            Objects.equals(etudiantId, that.etudiantId) &&
            Objects.equals(historiqueId, that.historiqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            note,
            statut,
            compteDansMoyenne,
            dateDebut,
            dateFin,
            commentaire1,
            commentaire2,
            commentaire3,
            saisiePar,
            saisieLe,
            valideePar,
            valideeLe,
            evaluationPrevueId,
            etudiantId,
            historiqueId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvaluationRealiseeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNote().map(f -> "note=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalCompteDansMoyenne().map(f -> "compteDansMoyenne=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalCommentaire1().map(f -> "commentaire1=" + f + ", ").orElse("") +
            optionalCommentaire2().map(f -> "commentaire2=" + f + ", ").orElse("") +
            optionalCommentaire3().map(f -> "commentaire3=" + f + ", ").orElse("") +
            optionalSaisiePar().map(f -> "saisiePar=" + f + ", ").orElse("") +
            optionalSaisieLe().map(f -> "saisieLe=" + f + ", ").orElse("") +
            optionalValideePar().map(f -> "valideePar=" + f + ", ").orElse("") +
            optionalValideeLe().map(f -> "valideeLe=" + f + ", ").orElse("") +
            optionalEvaluationPrevueId().map(f -> "evaluationPrevueId=" + f + ", ").orElse("") +
            optionalEtudiantId().map(f -> "etudiantId=" + f + ", ").orElse("") +
            optionalHistoriqueId().map(f -> "historiqueId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
