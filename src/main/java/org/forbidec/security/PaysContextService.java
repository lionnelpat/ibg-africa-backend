package org.forbidec.security;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.forbidec.domain.CentreFormation;
import org.forbidec.domain.Cycle;
import org.forbidec.domain.Etudiant;
import org.forbidec.domain.EvaluationPrevue;
import org.forbidec.domain.Pays;
import org.forbidec.repository.PaysRepository;
import org.hibernate.Session;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Cloisonnement par pays : lit le claim JWT {@code pays_groups} (peuplé par
 * un vrai Group Membership Mapper Keycloak sur les groupes {@code /pays/<ISO>},
 * distinct du claim {@code groups} déjà utilisé par JHipster pour les rôles),
 * en déduit le(s) pays autorisés pour l'utilisateur courant, et active le
 * filtre Hibernate {@code paysFilter} (voir {@code package-info.java} du
 * package domain) sur la session active.
 *
 * <p>À appeler en tout début de chaque méthode {@code @Transactional} qui
 * expose une liste/recherche potentiellement multi-pays (ex. les
 * {@code *QueryService.findByCriteria}) — l'appel doit se faire une fois la
 * transaction ouverte pour agir sur la bonne {@link Session} Hibernate
 * (OSIV désactivé dans ce projet : impossible de l'activer plus tôt, par
 * exemple dans un {@code HandlerInterceptor}, sans risquer une session
 * différente de celle réellement utilisée par la requête).
 */
@Service
public class PaysContextService {

    public static final String HEADER_PAYS_ACTIF = "X-Pays-Actif";
    public static final String VALEUR_TOUS = "TOUS";
    private static final String GROUP_PREFIX = "/pays/";

    private final PaysRepository paysRepository;
    private final EntityManager entityManager;

    public PaysContextService(PaysRepository paysRepository, EntityManager entityManager) {
        this.paysRepository = paysRepository;
        this.entityManager = entityManager;
    }

    /** Codes ISO (ex. "SN") des pays dont l'utilisateur courant est membre, via le claim pays_groups. */
    public List<String> getAllowedPaysCodes() {
        Map<String, Object> claims = getJwtClaims();
        Object raw = claims.get("pays_groups");
        if (!(raw instanceof List<?> list)) {
            return List.of();
        }
        return list
            .stream()
            .filter(String.class::isInstance)
            .map(String.class::cast)
            .filter(path -> path.startsWith(GROUP_PREFIX))
            .map(path -> path.substring(GROUP_PREFIX.length()))
            .toList();
    }

    /**
     * Selon le point d'entrée, l'authentification courante est soit un
     * {@link JwtAuthenticationToken} (appel porteur d'un Bearer token, ex.
     * via l'API testée directement), soit un {@code OAuth2AuthenticationToken}
     * dont le principal est un {@link DefaultOidcUser} (cas normal : session
     * de navigateur issue du flux {@code oauth2Login} de l'app) — les deux
     * doivent être lus pour ne pas silencieusement voir 0 pays.
     */
    private Map<String, Object> getJwtClaims() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaims();
        }
        if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return oidcUser.getClaims();
        }
        return Map.of();
    }

    public boolean isGlobalAdmin() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ADMIN);
    }

    /**
     * Active (ou n'active pas) le filtre paysFilter sur la session Hibernate
     * courante, à partir du header {@value #HEADER_PAYS_ACTIF} de la requête
     * en cours et des groupes pays de l'utilisateur. Idempotent : sans effet
     * si le filtre est déjà activé pour cette session.
     *
     * <p>Règles :
     * <ul>
     *   <li>admin global + header absent ou {@value #VALEUR_TOUS} : aucun filtre, tout est visible ;</li>
     *   <li>admin global + header = code pays : filtré sur ce seul pays (peu importe ses groupes) ;</li>
     *   <li>utilisateur non-admin : filtré sur le pays du header, qui DOIT faire partie de ses groupes
     *       (sinon filtré sur un pays inexistant : liste vide plutôt qu'une fuite de données) ;</li>
     *   <li>utilisateur non-admin, header absent, un seul pays dans ses groupes : ce pays par défaut.</li>
     * </ul>
     */
    public void enableFilterForCurrentRequest() {
        Session session = entityManager.unwrap(Session.class);
        if (session.getEnabledFilter("paysFilter") != null) {
            return;
        }

        String header = currentRequestHeader();
        boolean admin = isGlobalAdmin();
        List<String> allowedCodes = getAllowedPaysCodes();

        if (admin && (header == null || header.isBlank() || VALEUR_TOUS.equalsIgnoreCase(header))) {
            return; // pas de filtre : vue globale
        }

        String codeActif = header;
        if (codeActif == null || codeActif.isBlank()) {
            codeActif = allowedCodes.size() == 1 ? allowedCodes.get(0) : null;
        }

        List<Long> paysIds;
        if (codeActif == null || (!admin && !allowedCodes.contains(codeActif))) {
            paysIds = List.of(-1L); // aucun pays valide résolu : ne rien montrer plutôt que fuiter
        } else {
            String codeRecherche = codeActif;
            paysIds = paysRepository
                .findAll()
                .stream()
                .filter(p -> codeRecherche.equalsIgnoreCase(p.getCodeIso()))
                .map(Pays::getId)
                .toList();
            if (paysIds.isEmpty()) {
                paysIds = List.of(-1L);
            }
        }

        session.enableFilter("paysFilter").setParameterList("paysIds", paysIds);
    }

    /**
     * Contrôle d'accès pour les lectures par identifiant direct (bulletin PDF,
     * détail de cycle, saisie de notes...) : le filtre Hibernate {@code paysFilter}
     * ne protège que les requêtes JPQL/Criteria (findByCriteria, etc.), jamais un
     * {@code findById} — un utilisateur pourrait donc, en changeant l'id dans
     * l'URL, atteindre une fiche d'un autre pays que le sien. À appeler juste
     * après avoir chargé l'entité cible, avant de lui faire quoi que ce soit.
     * Lève {@link AccessDeniedException} (→ 403) plutôt que de risquer une fuite.
     */
    public void verifierAccesEtudiant(Etudiant etudiant) {
        verifierAccesPays(etudiant == null ? null : etudiant.getPays());
    }

    public void verifierAccesCycle(Cycle cycle) {
        verifierAccesPays(cycle == null ? null : paysDuCentre(cycle.getCentre()));
    }

    public void verifierAccesEvaluationPrevue(EvaluationPrevue evaluationPrevue) {
        verifierAccesCycle(evaluationPrevue == null ? null : evaluationPrevue.getCycle());
    }

    private Pays paysDuCentre(CentreFormation centre) {
        return centre == null ? null : centre.getPays();
    }

    private void verifierAccesPays(Pays pays) {
        if (isGlobalAdmin()) {
            return;
        }
        boolean autorise =
            pays != null && getAllowedPaysCodes().stream().anyMatch(code -> code.equalsIgnoreCase(pays.getCodeIso()));
        if (!autorise) {
            throw new AccessDeniedException("Accès refusé : cette ressource n'appartient pas à un pays autorisé pour cet utilisateur");
        }
    }

    private String currentRequestHeader() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
            HttpServletRequest request = attrs.getRequest();
            return request.getHeader(HEADER_PAYS_ACTIF);
        }
        return null;
    }
}
