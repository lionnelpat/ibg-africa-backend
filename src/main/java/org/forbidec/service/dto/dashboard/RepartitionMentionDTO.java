package org.forbidec.service.dto.dashboard;

import java.io.Serializable;

public class RepartitionMentionDTO implements Serializable {

    private String mentionCourte;
    private String mentionLongue;
    private Long nombre;

    public RepartitionMentionDTO() {}

    public RepartitionMentionDTO(String mentionCourte, String mentionLongue, Long nombre) {
        this.mentionCourte = mentionCourte;
        this.mentionLongue = mentionLongue;
        this.nombre = nombre;
    }

    public String getMentionCourte() {
        return mentionCourte;
    }

    public void setMentionCourte(String mentionCourte) {
        this.mentionCourte = mentionCourte;
    }

    public String getMentionLongue() {
        return mentionLongue;
    }

    public void setMentionLongue(String mentionLongue) {
        this.mentionLongue = mentionLongue;
    }

    public Long getNombre() {
        return nombre;
    }

    public void setNombre(Long nombre) {
        this.nombre = nombre;
    }
}
