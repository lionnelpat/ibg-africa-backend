package org.forbidec.service.dto.enseignant;

import java.io.Serializable;
import java.util.List;
import org.forbidec.service.dto.bulletin.MatiereDispenseeDTO;

/**
 * Un cycle et les matières qu'un enseignant y a dispensées.
 */
public class CycleEnseignementDTO implements Serializable {

    private Long cycleId;
    private Integer cycleAnnee;
    private String cycleLibelle;
    private List<MatiereDispenseeDTO> matieres;

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getCycleAnnee() {
        return cycleAnnee;
    }

    public void setCycleAnnee(Integer cycleAnnee) {
        this.cycleAnnee = cycleAnnee;
    }

    public String getCycleLibelle() {
        return cycleLibelle;
    }

    public void setCycleLibelle(String cycleLibelle) {
        this.cycleLibelle = cycleLibelle;
    }

    public List<MatiereDispenseeDTO> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<MatiereDispenseeDTO> matieres) {
        this.matieres = matieres;
    }
}
