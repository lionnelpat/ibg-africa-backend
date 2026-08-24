package org.forbidec.service.mention;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.forbidec.domain.BaremeMention;
import org.springframework.stereotype.Component;

/**
 * Résout une note vers sa mention, à partir d'une liste de {@link BaremeMention}
 * déjà triée par ordreAffichage. Reproduit fn_mention_longue / fn_mention_courte
 * du script de migration (bornes basses/hautes incluses ou non selon le barème).
 */
@Component
public class MentionResolver {

    /**
     * Barèmes applicables : ceux du centre s'il en a un, sinon les barèmes
     * globaux (sans centre rattaché). {@code centreId} null = globaux d'office.
     */
    public List<BaremeMention> selectApplicable(List<BaremeMention> all, Long centreId) {
        List<BaremeMention> actifsTries = all
            .stream()
            .filter(BaremeMention::getActif)
            .sorted(Comparator.comparing(BaremeMention::getOrdreAffichage))
            .toList();

        if (centreId != null) {
            List<BaremeMention> specifiques = actifsTries
                .stream()
                .filter(b -> b.getCentre() != null && centreId.equals(b.getCentre().getId()))
                .toList();
            if (!specifiques.isEmpty()) {
                return specifiques;
            }
        }
        return actifsTries.stream().filter(b -> b.getCentre() == null).toList();
    }

    public Mention resolve(List<BaremeMention> baremes, BigDecimal note) {
        if (note == null) {
            return Mention.AUCUNE;
        }
        for (BaremeMention bareme : baremes) {
            boolean minOk =
                bareme.getBorneMin() == null ||
                (Boolean.TRUE.equals(bareme.getMinInclus()) ? note.compareTo(bareme.getBorneMin()) >= 0 : note.compareTo(bareme.getBorneMin()) > 0);
            boolean maxOk =
                bareme.getBorneMax() == null ||
                (Boolean.TRUE.equals(bareme.getMaxInclus()) ? note.compareTo(bareme.getBorneMax()) <= 0 : note.compareTo(bareme.getBorneMax()) < 0);
            if (minOk && maxOk) {
                return new Mention(bareme.getLibelleLong(), bareme.getLibelleCourt());
            }
        }
        return Mention.AUCUNE;
    }
}
