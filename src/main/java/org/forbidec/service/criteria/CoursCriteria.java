package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.Cours} entity. This class is used
 * in {@link org.forbidec.web.rest.CoursResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cours?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CoursCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter intitule;

    private StringFilter libelleLong;

    private StringFilter libelleCourt;

    private IntegerFilter ordreAffichage;

    private IntegerFilter nbPeriodes;

    private BigDecimalFilter coefficient;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private StringFilter commentaire;

    private BooleanFilter actif;

    private LongFilter evaluationId;

    private Boolean distinct;

    public CoursCriteria() {}

    public CoursCriteria(CoursCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.intitule = other.optionalIntitule().map(StringFilter::copy).orElse(null);
        this.libelleLong = other.optionalLibelleLong().map(StringFilter::copy).orElse(null);
        this.libelleCourt = other.optionalLibelleCourt().map(StringFilter::copy).orElse(null);
        this.ordreAffichage = other.optionalOrdreAffichage().map(IntegerFilter::copy).orElse(null);
        this.nbPeriodes = other.optionalNbPeriodes().map(IntegerFilter::copy).orElse(null);
        this.coefficient = other.optionalCoefficient().map(BigDecimalFilter::copy).orElse(null);
        this.dateDebut = other.optionalDateDebut().map(LocalDateFilter::copy).orElse(null);
        this.dateFin = other.optionalDateFin().map(LocalDateFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.evaluationId = other.optionalEvaluationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CoursCriteria copy() {
        return new CoursCriteria(this);
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

    public StringFilter getLibelleLong() {
        return libelleLong;
    }

    public Optional<StringFilter> optionalLibelleLong() {
        return Optional.ofNullable(libelleLong);
    }

    public StringFilter libelleLong() {
        if (libelleLong == null) {
            setLibelleLong(new StringFilter());
        }
        return libelleLong;
    }

    public void setLibelleLong(StringFilter libelleLong) {
        this.libelleLong = libelleLong;
    }

    public StringFilter getLibelleCourt() {
        return libelleCourt;
    }

    public Optional<StringFilter> optionalLibelleCourt() {
        return Optional.ofNullable(libelleCourt);
    }

    public StringFilter libelleCourt() {
        if (libelleCourt == null) {
            setLibelleCourt(new StringFilter());
        }
        return libelleCourt;
    }

    public void setLibelleCourt(StringFilter libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public IntegerFilter getOrdreAffichage() {
        return ordreAffichage;
    }

    public Optional<IntegerFilter> optionalOrdreAffichage() {
        return Optional.ofNullable(ordreAffichage);
    }

    public IntegerFilter ordreAffichage() {
        if (ordreAffichage == null) {
            setOrdreAffichage(new IntegerFilter());
        }
        return ordreAffichage;
    }

    public void setOrdreAffichage(IntegerFilter ordreAffichage) {
        this.ordreAffichage = ordreAffichage;
    }

    public IntegerFilter getNbPeriodes() {
        return nbPeriodes;
    }

    public Optional<IntegerFilter> optionalNbPeriodes() {
        return Optional.ofNullable(nbPeriodes);
    }

    public IntegerFilter nbPeriodes() {
        if (nbPeriodes == null) {
            setNbPeriodes(new IntegerFilter());
        }
        return nbPeriodes;
    }

    public void setNbPeriodes(IntegerFilter nbPeriodes) {
        this.nbPeriodes = nbPeriodes;
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

    public BooleanFilter getActif() {
        return actif;
    }

    public Optional<BooleanFilter> optionalActif() {
        return Optional.ofNullable(actif);
    }

    public BooleanFilter actif() {
        if (actif == null) {
            setActif(new BooleanFilter());
        }
        return actif;
    }

    public void setActif(BooleanFilter actif) {
        this.actif = actif;
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
        final CoursCriteria that = (CoursCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(intitule, that.intitule) &&
            Objects.equals(libelleLong, that.libelleLong) &&
            Objects.equals(libelleCourt, that.libelleCourt) &&
            Objects.equals(ordreAffichage, that.ordreAffichage) &&
            Objects.equals(nbPeriodes, that.nbPeriodes) &&
            Objects.equals(coefficient, that.coefficient) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(evaluationId, that.evaluationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            intitule,
            libelleLong,
            libelleCourt,
            ordreAffichage,
            nbPeriodes,
            coefficient,
            dateDebut,
            dateFin,
            commentaire,
            actif,
            evaluationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoursCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIntitule().map(f -> "intitule=" + f + ", ").orElse("") +
            optionalLibelleLong().map(f -> "libelleLong=" + f + ", ").orElse("") +
            optionalLibelleCourt().map(f -> "libelleCourt=" + f + ", ").orElse("") +
            optionalOrdreAffichage().map(f -> "ordreAffichage=" + f + ", ").orElse("") +
            optionalNbPeriodes().map(f -> "nbPeriodes=" + f + ", ").orElse("") +
            optionalCoefficient().map(f -> "coefficient=" + f + ", ").orElse("") +
            optionalDateDebut().map(f -> "dateDebut=" + f + ", ").orElse("") +
            optionalDateFin().map(f -> "dateFin=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalEvaluationId().map(f -> "evaluationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
