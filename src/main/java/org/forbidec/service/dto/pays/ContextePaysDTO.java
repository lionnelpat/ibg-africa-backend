package org.forbidec.service.dto.pays;

import java.util.List;

/**
 * Réponse de {@code GET /api/pays-actifs} : de quoi peupler l'écran de choix
 * de pays après connexion. {@code sautEcran} indique si le frontend peut
 * sauter directement l'écran (un seul pays possible, non-admin).
 */
public class ContextePaysDTO {

    private boolean admin;
    private List<PaysActifDTO> paysSelectionnables;
    private boolean sautEcran;
    private Long paysUnique;

    public ContextePaysDTO() {}

    public ContextePaysDTO(boolean admin, List<PaysActifDTO> paysSelectionnables, boolean sautEcran, Long paysUnique) {
        this.admin = admin;
        this.paysSelectionnables = paysSelectionnables;
        this.sautEcran = sautEcran;
        this.paysUnique = paysUnique;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<PaysActifDTO> getPaysSelectionnables() {
        return paysSelectionnables;
    }

    public void setPaysSelectionnables(List<PaysActifDTO> paysSelectionnables) {
        this.paysSelectionnables = paysSelectionnables;
    }

    public boolean isSautEcran() {
        return sautEcran;
    }

    public void setSautEcran(boolean sautEcran) {
        this.sautEcran = sautEcran;
    }

    public Long getPaysUnique() {
        return paysUnique;
    }

    public void setPaysUnique(Long paysUnique) {
        this.paysUnique = paysUnique;
    }
}
