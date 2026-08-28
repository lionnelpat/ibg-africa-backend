package org.forbidec.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.forbidec.domain.BaremeMention;
import org.forbidec.domain.Cycle;
import org.forbidec.repository.BaremeMentionRepository;
import org.forbidec.repository.CycleRepository;
import org.forbidec.repository.EnseignantRepository;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.repository.dashboard.AnneeCountProjection;
import org.forbidec.repository.dashboard.CycleInscriptionCountProjection;
import org.forbidec.repository.dashboard.DashboardQueryRepository;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.dashboard.DashboardDTO;
import org.forbidec.service.dto.dashboard.EvolutionAnneeDTO;
import org.forbidec.service.dto.dashboard.RepartitionMentionDTO;
import org.forbidec.service.dto.dashboard.SessionRecenteDTO;
import org.forbidec.service.mention.Mention;
import org.forbidec.service.mention.MentionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reporting global : effectifs, réussite, évolution des inscriptions,
 * répartition des mentions (barème global), 5 dernières sessions.
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardService.class);
    private static final int NB_DERNIERES_SESSIONS = 5;

    private final EtudiantRepository etudiantRepository;
    private final CycleRepository cycleRepository;
    private final EnseignantRepository enseignantRepository;
    private final BaremeMentionRepository baremeMentionRepository;
    private final DashboardQueryRepository dashboardQueryRepository;
    private final MentionResolver mentionResolver;
    private final PaysContextService paysContextService;

    public DashboardService(
        EtudiantRepository etudiantRepository,
        CycleRepository cycleRepository,
        EnseignantRepository enseignantRepository,
        BaremeMentionRepository baremeMentionRepository,
        DashboardQueryRepository dashboardQueryRepository,
        MentionResolver mentionResolver,
        PaysContextService paysContextService
    ) {
        this.etudiantRepository = etudiantRepository;
        this.cycleRepository = cycleRepository;
        this.enseignantRepository = enseignantRepository;
        this.baremeMentionRepository = baremeMentionRepository;
        this.dashboardQueryRepository = dashboardQueryRepository;
        this.mentionResolver = mentionResolver;
        this.paysContextService = paysContextService;
    }

    public DashboardDTO getDashboard() {
        LOG.debug("Request to build the global Dashboard");
        paysContextService.enableFilterForCurrentRequest();

        DashboardDTO dto = new DashboardDTO();

        long totalEtudiants = etudiantRepository.count();
        long totalFinissants = dashboardQueryRepository.countFinissants();
        dto.setTotalEtudiants(totalEtudiants);
        dto.setTotalEtudiantsActifs(dashboardQueryRepository.countEtudiantsActifs());
        dto.setTotalCycles(cycleRepository.count());
        dto.setTotalEnseignants(enseignantRepository.count());
        dto.setTotalFinissants(totalFinissants);
        dto.setTauxReussite(totalEtudiants == 0 ? 0 : Math.round(totalFinissants * 1000.0 / totalEtudiants) / 10.0);

        dto.setEvolutionInscriptions(
            dashboardQueryRepository
                .countInscriptionsByCycleAnnee()
                .stream()
                .map((AnneeCountProjection p) -> new EvolutionAnneeDTO(p.getAnnee(), p.getNombre()))
                .toList()
        );

        dto.setRepartitionMentions(buildRepartitionMentions());
        dto.setDernieresSessions(buildDernieresSessions());

        return dto;
    }

    private List<RepartitionMentionDTO> buildRepartitionMentions() {
        List<BaremeMention> baremes = mentionResolver.selectApplicable(baremeMentionRepository.findAll(), null);
        List<BigDecimal> notes = dashboardQueryRepository.findNotesComptantDansMoyenne();

        Map<String, RepartitionMentionDTO> parMention = new LinkedHashMap<>();
        for (BaremeMention bareme : baremes) {
            parMention.put(bareme.getLibelleCourt(), new RepartitionMentionDTO(bareme.getLibelleCourt(), bareme.getLibelleLong(), 0L));
        }
        for (BigDecimal note : notes) {
            Mention mention = mentionResolver.resolve(baremes, note);
            if (mention.courte() == null) {
                continue;
            }
            RepartitionMentionDTO courant = parMention.get(mention.courte());
            if (courant != null) {
                courant.setNombre(courant.getNombre() + 1);
            }
        }
        return parMention.values().stream().filter(r -> r.getNombre() > 0).toList();
    }

    private List<SessionRecenteDTO> buildDernieresSessions() {
        Map<Long, Long> inscriptionsParCycle = dashboardQueryRepository
            .countInscriptionsByCycle()
            .stream()
            .collect(java.util.stream.Collectors.toMap(CycleInscriptionCountProjection::getCycleId, CycleInscriptionCountProjection::getNombre));

        return cycleRepository
            .findAllWithEagerRelationships()
            .stream()
            .sorted(Comparator.comparing(Cycle::getAnnee).reversed())
            .limit(NB_DERNIERES_SESSIONS)
            .map(cycle -> {
                SessionRecenteDTO session = new SessionRecenteDTO();
                session.setId(cycle.getId());
                session.setAnnee(cycle.getAnnee());
                session.setLibelle(cycle.getLibelle());
                session.setCloture(cycle.getCloture());
                if (cycle.getCentre() != null) {
                    session.setCentreCode(cycle.getCentre().getCode());
                    session.setCentreNom(cycle.getCentre().getNom());
                }
                session.setNbEtudiants(inscriptionsParCycle.getOrDefault(cycle.getId(), 0L));
                return session;
            })
            .toList();
    }
}
