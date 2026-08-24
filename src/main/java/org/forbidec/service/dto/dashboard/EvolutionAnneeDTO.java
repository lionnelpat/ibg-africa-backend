package org.forbidec.service.dto.dashboard;

import java.io.Serializable;

public class EvolutionAnneeDTO implements Serializable {

    private Integer annee;
    private Long nombre;

    public EvolutionAnneeDTO() {}

    public EvolutionAnneeDTO(Integer annee, Long nombre) {
        this.annee = annee;
        this.nombre = nombre;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Long getNombre() {
        return nombre;
    }

    public void setNombre(Long nombre) {
        this.nombre = nombre;
    }
}
