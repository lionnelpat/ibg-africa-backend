package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.Cycle} entity. This class is used
 * in {@link org.forbidec.web.rest.CycleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cycles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CycleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter annee;

    private StringFilter libelle;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private BooleanFilter cloture;

    private StringFilter commentaire;

    private LongFilter centreId;

    private LongFilter inscriptionId;

    private LongFilter evaluationId;

    private LongFilter habilitationId;

    private Boolean distinct;

    public CycleCriteria() {}

    public CycleCriteria(CycleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.annee = other.optionalAnnee().map(IntegerFilter::copy).orElse(null);
        this.libelle = other.optionalLibelle().map(StringFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(LocalDateFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(LocalDateFilter::copy).orElse(null);
        this.cloture = other.optionalCloture().map(BooleanFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.centreId = other.optionalCentreId().map(LongFilter::copy).orElse(null);
        this.inscriptionId = other.optionalInscriptionId().map(LongFilter::copy).orElse(null);
        this.evaluationId = other.optionalEvaluationId().map(LongFilter::copy).orElse(null);
        this.habilitationId = other.optionalHabilitationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CycleCriteria copy() {
        return new CycleCriteria(this);
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

    public IntegerFilter getAnnee() {
        return annee;
    }

    public Optional<IntegerFilter> optionalAnnee() {
        return Optional.ofNullable(annee);
    }

    public IntegerFilter annee() {
        if (annee == null) {
            setAnnee(new IntegerFilter());
        }
        return annee;
    }

    public void setAnnee(IntegerFilter annee) {
        this.annee = annee;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public Optional<StringFilter> optionalLibelle() {
        return Optional.ofNullable(libelle);
    }

    public StringFilter libelle() {
        if (libelle == null) {
            setLibelle(new StringFilter());
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
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

    public BooleanFilter getCloture() {
        return cloture;
    }

    public Optional<BooleanFilter> optionalCloture() {
        return Optional.ofNullable(cloture);
    }

    public BooleanFilter cloture() {
        if (cloture == null) {
            setCloture(new BooleanFilter());
        }
        return cloture;
    }

    public void setCloture(BooleanFilter cloture) {
        this.cloture = cloture;
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

    public LongFilter getCentreId() {
        return centreId;
    }

    public Optional<LongFilter> optionalCentreId() {
        return Optional.ofNullable(centreId);
    }

    public LongFilter centreId() {
        if (centreId == null) {
            setCentreId(new LongFilter());
        }
        return centreId;
    }

    public void setCentreId(LongFilter centreId) {
        this.centreId = centreId;
    }

    public LongFilter getInscriptionId() {
        return inscriptionId;
    }

    public Optional<LongFilter> optionalInscriptionId() {
        return Optional.ofNullable(inscriptionId);
    }

    public LongFilter inscriptionId() {
        if (inscriptionId == null) {
            setInscriptionId(new LongFilter());
        }
        return inscriptionId;
    }

    public void setInscriptionId(LongFilter inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public LongFilter getEvaluationId() {
        return evaluationId;
    }

    public Optional<LongFilter> optionalEvaluationId() {
        return Optional.ofNullable(evaluationId);
    }

    public LongFilter evaluationId() {
        if (evaluationId == null) {
            setEvaluationId(new LongFilter());
        }
        return evaluationId;
    }

    public void setEvaluationId(LongFilter evaluationId) {
        this.evaluationId = evaluationId;
    }

    public LongFilter getHabilitationId() {
        return habilitationId;
    }

    public Optional<LongFilter> optionalHabilitationId() {
        return Optional.ofNullable(habilitationId);
    }

    public LongFilter habilitationId() {
        if (habilitationId == null) {
            setHabilitationId(new LongFilter());
        }
        return habilitationId;
    }

    public void setHabilitationId(LongFilter habilitationId) {
        this.habilitationId = habilitationId;
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
        final CycleCriteria that = (CycleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(annee, that.annee) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(cloture, that.cloture) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(centreId, that.centreId) &&
            Objects.equals(inscriptionId, that.inscriptionId) &&
            Objects.equals(evaluationId, that.evaluationId) &&
            Objects.equals(habilitationId, that.habilitationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            annee,
            libelle,
            dateDebut,
            dateFin,
            cloture,
            commentaire,
            centreId,
            inscriptionId,
            evaluationId,
            habilitationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CycleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAnnee().map(f -> "annee=" + f + ", ").orElse("") +
            optionalLibelle().map(f -> "libelle=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalCloture().map(f -> "cloture=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalCentreId().map(f -> "centreId=" + f + ", ").orElse("") +
            optionalInscriptionId().map(f -> "inscriptionId=" + f + ", ").orElse("") +
            optionalEvaluationId().map(f -> "evaluationId=" + f + ", ").orElse("") +
            optionalHabilitationId().map(f -> "habilitationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
