package org.forbidec.service.dto.matricule;

import java.io.Serializable;
import java.util.List;

/** Résultat d'une génération de matricules manquants. */
public class MatriculeGenerationResultDTO implements Serializable {

    private int genere;
    private List<String> ignoresSansAnneeEntree;

    public int getGenere() {
        return genere;
    }

    public void setGenere(int genere) {
        this.genere = genere;
    }

    public List<String> getIgnoresSansAnneeEntree() {
        return ignoresSansAnneeEntree;
    }

    public void setIgnoresSansAnneeEntree(List<String> ignoresSansAnneeEntree) {
        this.ignoresSansAnneeEntree = ignoresSansAnneeEntree;
    }
}
