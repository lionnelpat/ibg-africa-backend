package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.InscriptionCycle} entity. This class is used
 * in {@link org.forbidec.web.rest.InscriptionCycleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /inscription-cycles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InscriptionCycleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateInscription;

    private BooleanFilter cycleTermine;

    private StringFilter groupe;

    private StringFilter commentaire1;

    private StringFilter commentaire2;

    private StringFilter commentaire3;

    private StringFilter commentaire5;

    private LongFilter cycleId;

    private LongFilter etudiantId;

    private Boolean distinct;

    public InscriptionCycleCriteria() {}

    public InscriptionCycleCriteria(InscriptionCycleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dateInscription = other.optionalDateInscription().map(LocalDateFilter::copy).orElse(null);
        this.cycleTermine = other.optionalCycleTermine().map(BooleanFilter::copy).orElse(null);
        this.groupe = other.optionalGroupe().map(StringFilter::copy).orElse(null);
        this.commentaire1 = other.optionalCommentaire1().map(StringFilter::copy).orElse(null);
        this.commentaire2 = other.optionalCommentaire2().map(StringFilter::copy).orElse(null);
        this.commentaire3 = other.optionalCommentaire3().map(StringFilter::copy).orElse(null);
        this.commentaire5 = other.optionalCommentaire5().map(StringFilter::copy).orElse(null);
        this.cycleId = other.optionalCycleId().map(LongFilter::copy).orElse(null);
        this.etudiantId = other.optionalEtudiantId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InscriptionCycleCriteria copy() {
        return new InscriptionCycleCriteria(this);
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

    public LocalDateFilter getDateInscription() {
        return dateInscription;
    }

    public Optional<LocalDateFilter> optionalDateInscription() {
        return Optional.ofNullable(dateInscription);
    }

    public LocalDateFilter dateInscription() {
        if (dateInscription == null) {
            setDateInscription(new LocalDateFilter());
        }
        return dateInscription;
    }

    public void setDateInscription(LocalDateFilter dateInscription) {
        this.dateInscription = dateInscription;
    }

    public BooleanFilter getCycleTermine() {
        return cycleTermine;
    }

    public Optional<BooleanFilter> optionalCycleTermine() {
        return Optional.ofNullable(cycleTermine);
    }

    public BooleanFilter cycleTermine() {
        if (cycleTermine == null) {
            setCycleTermine(new BooleanFilter());
        }
        return cycleTermine;
    }

    public void setCycleTermine(BooleanFilter cycleTermine) {
        this.cycleTermine = cycleTermine;
    }

    public StringFilter getGroupe() {
        return groupe;
    }

    public Optional<StringFilter> optionalGroupe() {
        return Optional.ofNullable(groupe);
    }

    public StringFilter groupe() {
        if (groupe == null) {
            setGroupe(new StringFilter());
        }
        return groupe;
    }

    public void setGroupe(StringFilter groupe) {
        this.groupe = groupe;
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

    public StringFilter getCommentaire5() {
        return commentaire5;
    }

    public Optional<StringFilter> optionalCommentaire5() {
        return Optional.ofNullable(commentaire5);
    }

    public StringFilter commentaire5() {
        if (commentaire5 == null) {
            setCommentaire5(new StringFilter());
        }
        return commentaire5;
    }

    public void setCommentaire5(StringFilter commentaire5) {
        this.commentaire5 = commentaire5;
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
        final InscriptionCycleCriteria that = (InscriptionCycleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateInscription, that.dateInscription) &&
            Objects.equals(cycleTermine, that.cycleTermine) &&
            Objects.equals(groupe, that.groupe) &&
            Objects.equals(commentaire1, that.commentaire1) &&
            Objects.equals(commentaire2, that.commentaire2) &&
            Objects.equals(commentaire3, that.commentaire3) &&
            Objects.equals(commentaire5, that.commentaire5) &&
            Objects.equals(cycleId, that.cycleId) &&
            Objects.equals(etudiantId, that.etudiantId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateInscription,
            cycleTermine,
            groupe,
            commentaire1,
            commentaire2,
            commentaire3,
            commentaire5,
            cycleId,
            etudiantId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InscriptionCycleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDateInscription().map(f -> "dateInscription=" + f + ", ").orElse("") +
            optionalCycleTermine().map(f -> "cycleTermine=" + f + ", ").orElse("") +
            optionalGroupe().map(f -> "groupe=" + f + ", ").orElse("") +
            optionalCommentaire1().map(f -> "commentaire1=" + f + ", ").orElse("") +
            optionalCommentaire2().map(f -> "commentaire2=" + f + ", ").orElse("") +
            optionalCommentaire3().map(f -> "commentaire3=" + f + ", ").orElse("") +
            optionalCommentaire5().map(f -> "commentaire5=" + f + ", ").orElse("") +
            optionalCycleId().map(f -> "cycleId=" + f + ", ").orElse("") +
            optionalEtudiantId().map(f -> "etudiantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
