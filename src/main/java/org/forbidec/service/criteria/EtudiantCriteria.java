package org.forbidec.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.forbidec.domain.Etudiant} entity. This class is used
 * in {@link org.forbidec.web.rest.EtudiantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /etudiants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EtudiantCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricule;

    private StringFilter nom;

    private StringFilter prenom;

    private StringFilter particularite;

    private LocalDateFilter dateNaissance;

    private StringFilter email;

    private StringFilter telephone;

    private IntegerFilter anneeEntree;

    private BooleanFilter cursusAcheve;

    private IntegerFilter anneeFinale;

    private StringFilter keycloakUserId;

    private StringFilter commentaire;

    private BooleanFilter actif;

    private LongFilter paysId;

    private LongFilter inscriptionId;

    private LongFilter evenementId;

    private LongFilter noteId;

    private Boolean distinct;

    public EtudiantCriteria() {}

    public EtudiantCriteria(EtudiantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.matricule = other.optionalMatricule().map(StringFilter::copy).orElse(null);
        this.nom = other.optionalNom().map(StringFilter::copy).orElse(null);
        this.prenom = other.optionalPrenom().map(StringFilter::copy).orElse(null);
        this.particularite = other.optionalParticularite().map(StringFilter::copy).orElse(null);
        this.dateNaissance = other.optionalDateNaissance().map(LocalDateFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telephone = other.optionalTelephone().map(StringFilter::copy).orElse(null);
        this.anneeEntree = other.optionalAnneeEntree().map(IntegerFilter::copy).orElse(null);
        this.cursusAcheve = other.optionalCursusAcheve().map(BooleanFilter::copy).orElse(null);
        this.anneeFinale = other.optionalAnneeFinale().map(IntegerFilter::copy).orElse(null);
        this.keycloakUserId = other.optionalKeycloakUserId().map(StringFilter::copy).orElse(null);
        this.commentaire = other.optionalCommentaire().map(StringFilter::copy).orElse(null);
        this.actif = other.optionalActif().map(BooleanFilter::copy).orElse(null);
        this.paysId = other.optionalPaysId().map(LongFilter::copy).orElse(null);
        this.inscriptionId = other.optionalInscriptionId().map(LongFilter::copy).orElse(null);
        this.evenementId = other.optionalEvenementId().map(LongFilter::copy).orElse(null);
        this.noteId = other.optionalNoteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EtudiantCriteria copy() {
        return new EtudiantCriteria(this);
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

    public StringFilter getMatricule() {
        return matricule;
    }

    public Optional<StringFilter> optionalMatricule() {
        return Optional.ofNullable(matricule);
    }

    public StringFilter matricule() {
        if (matricule == null) {
            setMatricule(new StringFilter());
        }
        return matricule;
    }

    public void setMatricule(StringFilter matricule) {
        this.matricule = matricule;
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

    public StringFilter getParticularite() {
        return particularite;
    }

    public Optional<StringFilter> optionalParticularite() {
        return Optional.ofNullable(particularite);
    }

    public StringFilter particularite() {
        if (particularite == null) {
            setParticularite(new StringFilter());
        }
        return particularite;
    }

    public void setParticularite(StringFilter particularite) {
        this.particularite = particularite;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public Optional<LocalDateFilter> optionalDateNaissance() {
        return Optional.ofNullable(dateNaissance);
    }

    public LocalDateFilter dateNaissance() {
        if (dateNaissance == null) {
            setDateNaissance(new LocalDateFilter());
        }
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
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

    public IntegerFilter getAnneeEntree() {
        return anneeEntree;
    }

    public Optional<IntegerFilter> optionalAnneeEntree() {
        return Optional.ofNullable(anneeEntree);
    }

    public IntegerFilter anneeEntree() {
        if (anneeEntree == null) {
            setAnneeEntree(new IntegerFilter());
        }
        return anneeEntree;
    }

    public void setAnneeEntree(IntegerFilter anneeEntree) {
        this.anneeEntree = anneeEntree;
    }

    public BooleanFilter getCursusAcheve() {
        return cursusAcheve;
    }

    public Optional<BooleanFilter> optionalCursusAcheve() {
        return Optional.ofNullable(cursusAcheve);
    }

    public BooleanFilter cursusAcheve() {
        if (cursusAcheve == null) {
            setCursusAcheve(new BooleanFilter());
        }
        return cursusAcheve;
    }

    public void setCursusAcheve(BooleanFilter cursusAcheve) {
        this.cursusAcheve = cursusAcheve;
    }

    public IntegerFilter getAnneeFinale() {
        return anneeFinale;
    }

    public Optional<IntegerFilter> optionalAnneeFinale() {
        return Optional.ofNullable(anneeFinale);
    }

    public IntegerFilter anneeFinale() {
        if (anneeFinale == null) {
            setAnneeFinale(new IntegerFilter());
        }
        return anneeFinale;
    }

    public void setAnneeFinale(IntegerFilter anneeFinale) {
        this.anneeFinale = anneeFinale;
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

    public LongFilter getPaysId() {
        return paysId;
    }

    public Optional<LongFilter> optionalPaysId() {
        return Optional.ofNullable(paysId);
    }

    public LongFilter paysId() {
        if (paysId == null) {
            setPaysId(new LongFilter());
        }
        return paysId;
    }

    public void setPaysId(LongFilter paysId) {
        this.paysId = paysId;
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

    public LongFilter getEvenementId() {
        return evenementId;
    }

    public Optional<LongFilter> optionalEvenementId() {
        return Optional.ofNullable(evenementId);
    }

    public LongFilter evenementId() {
        if (evenementId == null) {
            setEvenementId(new LongFilter());
        }
        return evenementId;
    }

    public void setEvenementId(LongFilter evenementId) {
        this.evenementId = evenementId;
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
        final EtudiantCriteria that = (EtudiantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matricule, that.matricule) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(particularite, that.particularite) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(anneeEntree, that.anneeEntree) &&
            Objects.equals(cursusAcheve, that.cursusAcheve) &&
            Objects.equals(anneeFinale, that.anneeFinale) &&
            Objects.equals(keycloakUserId, that.keycloakUserId) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(paysId, that.paysId) &&
            Objects.equals(inscriptionId, that.inscriptionId) &&
            Objects.equals(evenementId, that.evenementId) &&
            Objects.equals(noteId, that.noteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            matricule,
            nom,
            prenom,
            particularite,
            dateNaissance,
            email,
            telephone,
            anneeEntree,
            cursusAcheve,
            anneeFinale,
            keycloakUserId,
            commentaire,
            actif,
            paysId,
            inscriptionId,
            evenementId,
            noteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EtudiantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMatricule().map(f -> "matricule=" + f + ", ").orElse("") +
            optionalNom().map(f -> "nom=" + f + ", ").orElse("") +
            optionalPrenom().map(f -> "prenom=" + f + ", ").orElse("") +
            optionalParticularite().map(f -> "particularite=" + f + ", ").orElse("") +
            optionalDateNaissance().map(f -> "dateNaissance=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelephone().map(f -> "telephone=" + f + ", ").orElse("") +
            optionalAnneeEntree().map(f -> "anneeEntree=" + f + ", ").orElse("") +
            optionalCursusAcheve().map(f -> "cursusAcheve=" + f + ", ").orElse("") +
            optionalAnneeFinale().map(f -> "anneeFinale=" + f + ", ").orElse("") +
            optionalKeycloakUserId().map(f -> "keycloakUserId=" + f + ", ").orElse("") +
            optionalCommentaire().map(f -> "commentaire=" + f + ", ").orElse("") +
            optionalActif().map(f -> "actif=" + f + ", ").orElse("") +
            optionalPaysId().map(f -> "paysId=" + f + ", ").orElse("") +
            optionalInscriptionId().map(f -> "inscriptionId=" + f + ", ").orElse("") +
            optionalEvenementId().map(f -> "evenementId=" + f + ", ").orElse("") +
            optionalNoteId().map(f -> "noteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
