package org.forbidec.web.rest;

import java.util.List;
import org.forbidec.domain.Pays;
import org.forbidec.repository.PaysRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.pays.ContextePaysDTO;
import org.forbidec.service.dto.pays.PaysActifDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Alimente l'écran de choix de pays affiché après connexion : la liste des
 * pays que l'utilisateur peut sélectionner (ses groupes {@code /pays/<ISO>},
 * ou tous les pays actifs s'il est admin global), et si l'écran peut être
 * sauté (un seul choix possible pour un utilisateur non-admin).
 */
@RestController
@RequestMapping("/api")
public class PaysActifResource {

    private final PaysContextService paysContextService;
    private final PaysRepository paysRepository;

    public PaysActifResource(PaysContextService paysContextService, PaysRepository paysRepository) {
        this.paysContextService = paysContextService;
        this.paysRepository = paysRepository;
    }

    @GetMapping("/pays-actifs")
    public ContextePaysDTO getPaysActifs() {
        boolean admin = paysContextService.isGlobalAdmin();
        List<String> allowedCodes = paysContextService.getAllowedPaysCodes();

        List<Pays> tousLesPays = paysRepository.findAll();
        List<PaysActifDTO> selectionnables;
        if (admin) {
            selectionnables = tousLesPays
                .stream()
                .filter(Pays::getActif)
                .map(p -> new PaysActifDTO(p.getId(), p.getCodeIso(), p.getNom()))
                .toList();
        } else {
            selectionnables = tousLesPays
                .stream()
                .filter(p -> allowedCodes.contains(p.getCodeIso()))
                .map(p -> new PaysActifDTO(p.getId(), p.getCodeIso(), p.getNom()))
                .toList();
        }

        boolean sautEcran = !admin && selectionnables.size() == 1;
        Long paysUnique = sautEcran ? selectionnables.get(0).getId() : null;

        return new ContextePaysDTO(admin, selectionnables, sautEcran, paysUnique);
    }
}
