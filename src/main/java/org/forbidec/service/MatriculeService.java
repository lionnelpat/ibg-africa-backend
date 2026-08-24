package org.forbidec.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.forbidec.domain.Etudiant;
import org.forbidec.repository.EtudiantRepository;
import org.forbidec.service.dto.matricule.MatriculeGenerationResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Génère les matricules manquants au format {@code AAAA-NNN} (année
 * d'entrée, puis séquence sur 3 chiffres remise à zéro par année).
 * Ré-exécutable sans risque : ne touche jamais un matricule déjà posé,
 * et repart de la séquence la plus haute déjà utilisée pour chaque année.
 */
@Service
@Transactional
public class MatriculeService {

    private static final Logger LOG = LoggerFactory.getLogger(MatriculeService.class);
    private static final Pattern MATRICULE_PATTERN = Pattern.compile("^(\\d{4})-(\\d{3})$");

    private final EtudiantRepository etudiantRepository;

    public MatriculeService(EtudiantRepository etudiantRepository) {
        this.etudiantRepository = etudiantRepository;
    }

    public MatriculeGenerationResultDTO genererMatriculesManquants() {
        LOG.debug("Request to generate missing matricules");

        List<Etudiant> tous = etudiantRepository.findAll();

        Map<Integer, Integer> dernierSeqParAnnee = new HashMap<>();
        for (Etudiant e : tous) {
            if (e.getMatricule() != null) {
                Matcher m = MATRICULE_PATTERN.matcher(e.getMatricule());
                if (m.matches()) {
                    int annee = Integer.parseInt(m.group(1));
                    int seq = Integer.parseInt(m.group(2));
                    dernierSeqParAnnee.merge(annee, seq, Math::max);
                }
            }
        }

        List<Etudiant> sansMatricule = tous.stream().filter(e -> e.getMatricule() == null || e.getMatricule().isBlank()).toList();

        List<Etudiant> aTraiter = sansMatricule
            .stream()
            .filter(e -> e.getAnneeEntree() != null)
            .sorted(
                Comparator.comparing(Etudiant::getAnneeEntree)
                    .thenComparing(Etudiant::getNom, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Etudiant::getPrenom, String.CASE_INSENSITIVE_ORDER)
            )
            .toList();

        for (Etudiant e : aTraiter) {
            int annee = e.getAnneeEntree();
            int seq = dernierSeqParAnnee.merge(annee, 1, Integer::sum);
            e.setMatricule(annee + "-" + String.format("%03d", seq));
        }
        etudiantRepository.saveAll(aTraiter);

        List<String> ignores = new ArrayList<>();
        for (Etudiant e : sansMatricule) {
            if (e.getAnneeEntree() == null) {
                ignores.add(e.getNom() + " " + e.getPrenom() + " (id=" + e.getId() + ")");
            }
        }

        MatriculeGenerationResultDTO result = new MatriculeGenerationResultDTO();
        result.setGenere(aTraiter.size());
        result.setIgnoresSansAnneeEntree(ignores);
        return result;
    }
}
