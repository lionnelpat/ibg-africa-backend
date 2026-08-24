package org.forbidec.service.dto.saisie;

import java.io.Serializable;
import java.util.List;

/** Résultat d'un enregistrement de notes (saisie manuelle ou import Excel). */
public class SaisieResultDTO implements Serializable {

    private int enregistrees;
    private List<String> erreurs;

    public int getEnregistrees() {
        return enregistrees;
    }

    public void setEnregistrees(int enregistrees) {
        this.enregistrees = enregistrees;
    }

    public List<String> getErreurs() {
        return erreurs;
    }

    public void setErreurs(List<String> erreurs) {
        this.erreurs = erreurs;
    }
}
