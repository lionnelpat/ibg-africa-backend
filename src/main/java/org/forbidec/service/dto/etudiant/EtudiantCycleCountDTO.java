package org.forbidec.service.dto.etudiant;

import java.io.Serializable;

/**
 * Nombre de cycles distincts suivis par un étudiant — sert à dériver son
 * année de parcours (1 cycle = 1re année, ... 5 cycles = 5e et dernière année).
 */
public class EtudiantCycleCountDTO implements Serializable {

    private Long etudiantId;
    private Long total;

    public EtudiantCycleCountDTO() {}

    public EtudiantCycleCountDTO(Long etudiantId, Long total) {
        this.etudiantId = etudiantId;
        this.total = total;
    }

    public Long getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(Long etudiantId) {
        this.etudiantId = etudiantId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
