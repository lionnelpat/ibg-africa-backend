package org.forbidec.service.dto.cycle;

import java.io.Serializable;

/**
 * Nombre d'étudiants inscrits pour un cycle donné.
 */
public class CycleInscriptionCountDTO implements Serializable {

    private Long cycleId;
    private Long total;

    public CycleInscriptionCountDTO() {}

    public CycleInscriptionCountDTO(Long cycleId, Long total) {
        this.cycleId = cycleId;
        this.total = total;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public void setCycleId(Long cycleId) {
        this.cycleId = cycleId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
