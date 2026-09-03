package org.forbidec.service.dto.pays;

/** Un pays sélectionnable par l'utilisateur courant sur l'écran de choix de pays. */
public class PaysActifDTO {

    private Long id;
    private String codeIso;
    private String nom;

    public PaysActifDTO() {}

    public PaysActifDTO(Long id, String codeIso, String nom) {
        this.id = id;
        this.codeIso = codeIso;
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeIso() {
        return codeIso;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
