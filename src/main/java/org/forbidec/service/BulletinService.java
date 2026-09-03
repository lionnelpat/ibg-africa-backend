package org.forbidec.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.forbidec.domain.BaremeMention;
import org.forbidec.domain.CentreFormation;
import org.forbidec.domain.Cycle;
import org.forbidec.domain.Etudiant;
import org.forbidec.repository.BaremeMentionRepository;
import org.forbidec.repository.CycleRepository;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.repository.bulletin.BulletinQueryRepository;
import org.forbidec.repository.bulletin.EvaluationLigneProjection;
import org.forbidec.security.PaysContextService;
import org.forbidec.service.dto.bulletin.BulletinDTO;
import org.forbidec.service.dto.bulletin.BulletinLigneDTO;
import org.forbidec.service.mention.Mention;
import org.forbidec.service.mention.MentionResolver;
import org.forbidec.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Calcule le bulletin cumulé d'un étudiant : une ligne par (cycle, cours)
 * comptant dans la moyenne, la mention associée, et la synthèse générale.
 * Reproduit en Java la logique validée en SQL lors de la reprise des
 * données (v_bulletin_ligne / v_bulletin_synthese / fn_mention_*), pour ne
 * pas dépendre d'objets non gérés par Liquibase.
 */
@Service
@Transactional(readOnly = true)
public class BulletinService {

    private static final Logger LOG = LoggerFactory.getLogger(BulletinService.class);

    private final BulletinQueryRepository bulletinQueryRepository;
    private final EtudiantRepository etudiantRepository;
    private final CycleRepository cycleRepository;
    private final BaremeMentionRepository baremeMentionRepository;
    private final MentionResolver mentionResolver;
    private final PaysContextService paysContextService;

    public BulletinService(
        BulletinQueryRepository bulletinQueryRepository,
        EtudiantRepository etudiantRepository,
        CycleRepository cycleRepository,
        BaremeMentionRepository baremeMentionRepository,
        MentionResolver mentionResolver,
        PaysContextService paysContextService
    ) {
        this.bulletinQueryRepository = bulletinQueryRepository;
        this.etudiantRepository = etudiantRepository;
        this.cycleRepository = cycleRepository;
        this.baremeMentionRepository = baremeMentionRepository;
        this.mentionResolver = mentionResolver;
        this.paysContextService = paysContextService;
    }

    public BulletinDTO getBulletin(Long etudiantId) {
        LOG.debug("Request to build Bulletin for Etudiant : {}", etudiantId);

        Etudiant etudiant = etudiantRepository
            .findById(etudiantId)
            .orElseThrow(() -> new BadRequestAlertException("Étudiant introuvable", "etudiant", "idnotfound"));
        paysContextService.verifierAccesEtudiant(etudiant);

        List<EvaluationLigneProjection> rows = bulletinQueryRepository.findLignesForEtudiant(etudiantId);

        record GroupKey(Long cycleId, Integer cycleAnnee, Long coursId, String coursIntitule, Integer ordreAffichage) {}
        record Accumulator(BigDecimal sommeCoefNote, BigDecimal sommeCoef) {}

        Map<GroupKey, Accumulator> groupes = new LinkedHashMap<>();
        for (EvaluationLigneProjection row : rows) {
            GroupKey key = new GroupKey(row.getCycleId(), row.getCycleAnnee(), row.getCoursId(), row.getCoursIntitule(), row.getCoursOrdreAffichage());
            Accumulator acc = groupes.get(key);
            BigDecimal coef = row.getCoefficient();
            BigDecimal contribution = coef.multiply(row.getNote());
            groupes.put(
                key,
                acc == null
                    ? new Accumulator(contribution, coef)
                    : new Accumulator(acc.sommeCoefNote().add(contribution), acc.sommeCoef().add(coef))
            );
        }

        Long dernierCycleId = groupes
            .keySet()
            .stream()
            .max(Comparator.comparing(GroupKey::cycleAnnee))
            .map(GroupKey::cycleId)
            .orElse(null);

        Long centreId = dernierCycleId == null
            ? null
            : cycleRepository.findById(dernierCycleId).map(Cycle::getCentre).map(CentreFormation::getId).orElse(null);
        List<BaremeMention> baremes = mentionResolver.selectApplicable(baremeMentionRepository.findAll(), centreId);

        record LigneCalculee(GroupKey key, BigDecimal moyenneCours) {}

        List<LigneCalculee> calculees = groupes
            .entrySet()
            .stream()
            .map(entry -> new LigneCalculee(entry.getKey(), entry.getValue().sommeCoefNote().divide(entry.getValue().sommeCoef(), 2, RoundingMode.HALF_UP)))
            .sorted(Comparator.<LigneCalculee, Integer>comparing(l -> l.key().cycleAnnee()).thenComparing(l -> l.key().ordreAffichage()))
            .toList();

        BigDecimal sommeMoyennes = calculees.stream().map(LigneCalculee::moyenneCours).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BulletinLigneDTO> lignes = new ArrayList<>();
        for (LigneCalculee calculee : calculees) {
            BulletinLigneDTO ligne = new BulletinLigneDTO();
            ligne.setCycleAnnee(calculee.key().cycleAnnee());
            ligne.setCoursIntitule(calculee.key().coursIntitule());
            ligne.setMoyenneCours(calculee.moyenneCours());
            Mention mention = mentionResolver.resolve(baremes, calculee.moyenneCours());
            ligne.setMentionLongue(mention.longue());
            ligne.setMentionCourte(mention.courte());
            lignes.add(ligne);
        }

        BulletinDTO dto = new BulletinDTO();
        dto.setEtudiantId(etudiant.getId());
        dto.setMatricule(etudiant.getMatricule());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setLignes(lignes);
        dto.setDateEdition(LocalDate.now());

        Optional<Integer> premiereAnnee = groupes.keySet().stream().map(GroupKey::cycleAnnee).min(Integer::compareTo);
        Optional<Integer> derniereAnnee = groupes.keySet().stream().map(GroupKey::cycleAnnee).max(Integer::compareTo);
        dto.setPremiereAnnee(premiereAnnee.orElse(null));
        dto.setDerniereAnnee(derniereAnnee.orElse(null));

        if (!groupes.isEmpty()) {
            BigDecimal moyenneGenerale = sommeMoyennes.divide(BigDecimal.valueOf(groupes.size()), 2, RoundingMode.HALF_UP);
            dto.setMoyenneGenerale(moyenneGenerale);
            Mention mentionGenerale = mentionResolver.resolve(baremes, moyenneGenerale);
            dto.setMentionGeneraleLongue(mentionGenerale.longue());
            dto.setMentionGeneraleCourte(mentionGenerale.courte());
        }

        if (dernierCycleId != null) {
            cycleRepository
                .findById(dernierCycleId)
                .map(Cycle::getCentre)
                .ifPresent(centre -> applyCentre(dto, centre));
        }

        return dto;
    }

    private void applyCentre(BulletinDTO dto, CentreFormation centre) {
        dto.setCentreCode(centre.getCode());
        dto.setCentreNom(centre.getNom());
        dto.setCentreVille(centre.getVille());
        dto.setCentreSignataire(centre.getSignataire());
        dto.setCentreEnteteDocument(centre.getEnteteDocument());
    }
}
