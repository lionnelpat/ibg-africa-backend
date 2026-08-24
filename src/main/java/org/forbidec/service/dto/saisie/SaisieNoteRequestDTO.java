package org.forbidec.service.dto.saisie;

import java.io.Serializable;
import java.math.BigDecimal;
import org.forbidec.domain.enumeration.StatutNote;

/** Une ligne envoyée par le formulaire de saisie (manuelle ou import Excel). */
public class SaisieNoteRequestDTO implements Serializable {

    private Long etudiantId;
    private BigDecimal note;
    private StatutNote statut;

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public BigDecimal getNote() {
        return note;
    }

    public void setNote(BigDecimal note) {
        this.note = note;
    }

    public StatutNote getStatut() {
        return statut;
    }

    public void setStatut(StatutNote statut) {
        this.statut = statut;
    }
}
