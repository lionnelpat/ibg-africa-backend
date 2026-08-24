package org.forbidec.service.dto.bulletin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Bulletin cumulé d'un étudiant : historique complet (toutes les lignes
 * cycle/cours/mention depuis son entrée) + synthèse (moyenne générale et
 * mention générale), avec le contexte d'impression (centre, ville, signataire).
 */
public class BulletinDTO implements Serializable {

    private Long etudiantId;
    private String matricule;
    private String nom;
    private String prenom;

    private String centreCode;
    private String centreNom;
    private String centreVille;
    private String centreSignataire;
    private String centreEnteteDocument;

    private Integer premiereAnnee;
    private Integer derniereAnnee;

    private List<BulletinLigneDTO> lignes;

    private BigDecimal moyenneGenerale;
    private String mentionGeneraleLongue;
    private String mentionGeneraleCourte;

    private LocalDate dateEdition;

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCentreCode() {
        return centreCode;
    }

    public void setCentreCode(String centreCode) {
        this.centreCode = centreCode;
    }

    public String getCentreNom() {
        return centreNom;
    }

    public void setCentreNom(String centreNom) {
        this.centreNom = centreNom;
    }

    public String getCentreVille() {
        return centreVille;
    }

    public void setCentreVille(String centreVille) {
        this.centreVille = centreVille;
    }

    public String getCentreSignataire() {
        return centreSignataire;
    }

    public void setCentreSignataire(String centreSignataire) {
        this.centreSignataire = centreSignataire;
    }

    public String getCentreEnteteDocument() {
        return centreEnteteDocument;
    }

    public void setCentreEnteteDocument(String centreEnteteDocument) {
        this.centreEnteteDocument = centreEnteteDocument;
    }

    public Integer getPremiereAnnee() {
        return premiereAnnee;
    }

    public void setPremiereAnnee(Integer premiereAnnee) {
        this.premiereAnnee = premiereAnnee;
    }

    public Integer getDerniereAnnee() {
        return derniereAnnee;
    }

    public void setDerniereAnnee(Integer derniereAnnee) {
        this.derniereAnnee = derniereAnnee;
    }

    public List<BulletinLigneDTO> getLignes() {
        return lignes;
    }

    public void setLignes(List<BulletinLigneDTO> lignes) {
        this.lignes = lignes;
    }

    public BigDecimal getMoyenneGenerale() {
        return moyenneGenerale;
    }

    public void setMoyenneGenerale(BigDecimal moyenneGenerale) {
        this.moyenneGenerale = moyenneGenerale;
    }

    public String getMentionGeneraleLongue() {
        return mentionGeneraleLongue;
    }

    public void setMentionGeneraleLongue(String mentionGeneraleLongue) {
        this.mentionGeneraleLongue = mentionGeneraleLongue;
    }

    public String getMentionGeneraleCourte() {
        return mentionGeneraleCourte;
    }

    public void setMentionGeneraleCourte(String mentionGeneraleCourte) {
        this.mentionGeneraleCourte = mentionGeneraleCourte;
    }

    public LocalDate getDateEdition() {
        return dateEdition;
    }

    public void setDateEdition(LocalDate dateEdition) {
        this.dateEdition = dateEdition;
    }
}
