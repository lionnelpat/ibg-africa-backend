package org.forbidec.service.dto.bulletin;

import java.io.Serializable;

/**
 * Une matière (cours) dispensée sur un cycle, avec l'enseignant qui la donne.
 */
public class MatiereDispenseeDTO implements Serializable {

    private Long coursId;
    private String coursIntitule;
    private String matiereIntitule;
    private String sousMatiereIntitule;
    private Long enseignantId;
    private String enseignantNom;
    private String enseignantPrenom;

    public Long getCoursId() {
        return coursId;
    }

    public void setCoursId(Long coursId) {
        this.coursId = coursId;
    }

    public String getCoursIntitule() {
        return coursIntitule;
    }

    public void setCoursIntitule(String coursIntitule) {
        this.coursIntitule = coursIntitule;
    }

    public String getMatiereIntitule() {
        return matiereIntitule;
    }

    public void setMatiereIntitule(String matiereIntitule) {
        this.matiereIntitule = matiereIntitule;
    }

    public String getSousMatiereIntitule() {
        return sousMatiereIntitule;
    }

    public void setSousMatiereIntitule(String sousMatiereIntitule) {
        this.sousMatiereIntitule = sousMatiereIntitule;
    }

    public Long getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(Long enseignantId) {
        this.enseignantId = enseignantId;
    }

    public String getEnseignantNom() {
        return enseignantNom;
    }

    public void setEnseignantNom(String enseignantNom) {
        this.enseignantNom = enseignantNom;
    }

    public String getEnseignantPrenom() {
        return enseignantPrenom;
    }

    public void setEnseignantPrenom(String enseignantPrenom) {
        this.enseignantPrenom = enseignantPrenom;
    }
}
