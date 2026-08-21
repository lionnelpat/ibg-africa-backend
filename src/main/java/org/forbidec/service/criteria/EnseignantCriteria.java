package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.Enseignant} entity. This class is used
 * in {@link org.forbidec.web.rest.EnseignantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enseignants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnseignantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter libelleLong;

    private StringFilter libelleCourt;

    private StringFilter email;

    private StringFilter telephone;

    private StringFilter keycloakUserId;

    private StringFilter commentaire;

    private BooleanFilter actif;

    private LongFilter evaluationId;

    private Boolean distinct;

    public EnseignantCriteria() {}

    public EnseignantCriteria(EnseignantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.libelleLong = other.optionalLibelleLong().map(StringFilter::copy).orElse(null);
        this.libelleCourt = other.optionalLibelleCourt().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.keycloakUserId = other.optionalKeycloakUserId().map(StringFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.evaluationId = other.optionalEvaluationId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EnseignantCriteria copy() {
        return new EnseignantCriteria(this);
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

    public StringFilter getNom() {
        return nom;
    }

    public Optional<StringFilter> optionalNom() {
        return Optional.ofNullable(nom);
    }

    public StringFilter nom() {
        if (nom == null) {
            setNom(new StringFilter());
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public Optional<StringFilter> optionalPrenom() {
        return Optional.ofNullable(prenom);
    }

    public StringFilter prenom() {
        if (prenom == null) {
            setPrenom(new StringFilter());
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
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

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public Optional<StringFilter> optionalTelephone() {
        return Optional.ofNullable(telephone);
    }

    public StringFilter telephone() {
        if (telephone == null) {
            setTelephone(new StringFilter());
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public StringFilter getKeycloakUserId() {
        return keycloakUserId;
    }

    public Optional<StringFilter> optionalKeycloakUserId() {
        return Optional.ofNullable(keycloakUserId);
    }

    public StringFilter keycloakUserId() {
        if (keycloakUserId == null) {
            setKeycloakUserId(new StringFilter());
        }
        return keycloakUserId;
    }

    public void setKeycloakUserId(StringFilter keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
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
        final EnseignantCriteria that = (EnseignantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(libelleLong, that.libelleLong) &&
            Objects.equals(libelleCourt, that.libelleCourt) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(keycloakUserId, that.keycloakUserId) &&
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
            nom,
            prenom,
            libelleLong,
            libelleCourt,
            email,
            telephone,
            keycloakUserId,
            commentaire,
            actif,
            evaluationId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnseignantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalLibelleLong().map(f -> "libelleLong=" + f + ", ").orElse("") +
            optionalLibelleCourt().map(f -> "libelleCourt=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalKeycloakUserId().map(f -> "keycloakUserId=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalEvaluationId().map(f -> "evaluationId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
