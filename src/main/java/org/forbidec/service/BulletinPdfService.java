package org.forbidec.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.forbidec.domain.BaremeMention;
import org.forbidec.repository.BaremeMentionRepository;
import org.forbidec.service.dto.bulletin.BulletinDTO;
import org.forbidec.service.dto.bulletin.BulletinLigneDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Génère le bulletin d'un étudiant en PDF, en reproduisant la mise en page
 * validée (bloc .bulletin-print de la page d'impression Angular) : même
 * contenu, même légende des mentions, même présentation, mais rendue
 * côté serveur (HTML -> PDF via openhtmltopdf) plutôt que via window.print().
 */
@Service
@Transactional(readOnly = true)
public class BulletinPdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);

    private final BulletinService bulletinService;
    private final BaremeMentionRepository baremeMentionRepository;

    // Logos des partenaires : IBG à droite pour tous les centres, FES à
    // gauche uniquement pour Dakar (partenariat propre à ce centre). À
    // rendre paramétrable par centre en base le jour où d'autres centres
    // ont eux aussi un partenaire local à afficher.
    private static final String CODE_CENTRE_AVEC_FES = "CDDakar";

    private final String logoGaucheBase64 = chargerLogoBase64("logos/fes-logo.jpg");
    private final String logoDroitBase64 = chargerLogoBase64("logos/ibg-logo.png");

    public BulletinPdfService(BulletinService bulletinService, BaremeMentionRepository baremeMentionRepository) {
        this.bulletinService = bulletinService;
        this.baremeMentionRepository = baremeMentionRepository;
    }

    private static String chargerLogoBase64(String classpathLocation) {
        try (InputStream in = new ClassPathResource(classpathLocation).getInputStream()) {
            return Base64.getEncoder().encodeToString(in.readAllBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Logo introuvable : " + classpathLocation, e);
        }
    }

    public byte[] genererPdf(Long etudiantId) {
        BulletinDTO bulletin = bulletinService.getBulletin(etudiantId);
        String legende = construireLegende();
        String html = construireHtml(bulletin, legende);

        ByteArrayOutputStream sortie = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(sortie);
        try {
            builder.run();
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de la génération du PDF du bulletin", e);
        }
        return sortie.toByteArray();
    }

    private String construireLegende() {
        List<BaremeMention> baremes = baremeMentionRepository
            .findAll()
            .stream()
            .filter(BaremeMention::getActif)
            .sorted(Comparator.comparing(BaremeMention::getOrdreAffichage))
            .toList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < baremes.size(); i++) {
            if (i > 0) {
                sb.append(" ; ");
            }
            BaremeMention b = baremes.get(i);
            sb.append(b.getLibelleCourt()).append(" = ").append(b.getLibelleLong()).append(": ").append(borneLabel(b));
        }
        return sb.toString();
    }

    private static String borneLabel(BaremeMention bareme) {
        if (bareme.getBorneMin() == null) {
            return "moins de " + bareme.getBorneMax();
        }
        String open = Boolean.TRUE.equals(bareme.getMinInclus()) ? "[" : "]";
        String close = Boolean.TRUE.equals(bareme.getMaxInclus()) ? "]" : "[";
        return open + bareme.getBorneMin() + "-" + bareme.getBorneMax() + close;
    }

    private String construireHtml(BulletinDTO b, String legende) {
        StringBuilder lignes = new StringBuilder();
        for (BulletinLigneDTO ligne : b.getLignes()) {
            lignes
                .append("<tr><td>")
                .append(esc(ligne.getCycleAnnee()))
                .append("</td><td>")
                .append(esc(ligne.getCoursIntitule()))
                .append("</td><td>")
                .append(esc(ligne.getMentionLongue()))
                .append("</td></tr>");
        }

        String entete = esc(b.getCentreEnteteDocument()).replace("\n", "<br/>");
        String dateEdition = b.getDateEdition() != null ? DATE_FORMATTER.format(b.getDateEdition()) : "";
        String moyenneGenerale = formatMoyenne(b.getMoyenneGenerale());
        boolean avecLogoFes = CODE_CENTRE_AVEC_FES.equals(b.getCentreCode());
        String logoGauche = avecLogoFes
            ? "<td class=\"logo\"><div class=\"logo-box\"><img src=\"data:image/jpeg;base64," + logoGaucheBase64 + "\"/></div></td>"
            : "<td class=\"logo\"></td>";

        return (
            "<html><head><meta charset=\"UTF-8\"/><style>" +
            "body{font-family:Arial,sans-serif;color:#000;font-size:11pt;}" +
            "table.masthead{width:100%;margin-bottom:1.5rem;}" +
            "table.masthead td{vertical-align:middle;}" +
            "table.masthead td.logo{width:15%;}" +
            "table.masthead td.logo .logo-box{width:64px;height:64px;}" +
            "table.masthead td.logo img{max-width:100%;max-height:100%;width:auto;height:auto;}" +
            "table.masthead td.logo-droit{text-align:right;}" +
            "table.masthead td.logo-droit .logo-box{margin-left:auto;}" +
            ".entete{text-align:center;font-style:italic;font-weight:bold;margin:0;}" +
            ".identite{text-align:center;font-weight:bold;margin-bottom:2rem;}" +
            ".titre{text-align:center;font-size:1.1rem;margin-bottom:1.5rem;}" +
            "table.lignes{width:100%;border-collapse:collapse;margin-bottom:1.5rem;}" +
            "table.lignes th{text-align:left;border-bottom:1px solid #000;padding:0.25rem 0.5rem;}" +
            "table.lignes td{padding:0.15rem 0.5rem;}" +
            ".synthese{margin-bottom:2rem;}" +
            ".synthese-titre{font-weight:bold;}" +
            ".synthese-ligne{margin:0.5rem 0 0 2rem;}" +
            ".synthese-ligne .label{display:inline-block;width:12rem;font-weight:bold;}" +
            ".box{border:1px solid #000;padding:0.1rem 1rem;min-width:3rem;text-align:center;display:inline-block;}" +
            "table.signature{width:100%;margin-bottom:3rem;}" +
            "table.signature td.droite{text-align:right;}" +
            ".legende{font-size:0.7rem;border-top:1px solid #000;padding-top:0.5rem;}" +
            ".pied{font-size:0.7rem;}" +
            "</style></head><body>" +
            "<table class=\"masthead\"><tr>" +
            logoGauche +
            "<td class=\"entete\">" +
            entete +
            "</td>" +
            "<td class=\"logo logo-droit\"><div class=\"logo-box\"><img src=\"data:image/png;base64," +
            logoDroitBase64 +
            "\"/></div></td>" +
            "</tr></table>" +
            "<p class=\"identite\">" +
            esc(b.getNom()) +
            ", " +
            esc(b.getPrenom()) +
            "</p>" +
            "<h2 class=\"titre\">" +
            esc(b.getCentreCode()) +
            " - Feuille récapitulative des mentions</h2>" +
            "<table class=\"lignes\"><thead><tr><th>Cycle</th><th>Cours</th><th>Mention</th></tr></thead><tbody>" +
            lignes +
            "</tbody></table>" +
            "<div class=\"synthese\">" +
            "<span class=\"synthese-titre\">COURS</span>" +
            "<div class=\"synthese-ligne\"><span class=\"label\">Moyenne Générale</span><span class=\"box\">" +
            moyenneGenerale +
            "</span></div>" +
            "<div class=\"synthese-ligne\"><span class=\"label\">Mention</span><span class=\"box\">" +
            esc(b.getMentionGeneraleCourte()) +
            "</span></div>" +
            "</div>" +
            "<table class=\"signature\"><tr><td>" +
            esc(b.getCentreVille()) +
            ", le " +
            dateEdition +
            "</td><td class=\"droite\">" +
            esc(b.getCentreSignataire()) +
            "</td></tr></table>" +
            "<p class=\"legende\">" +
            esc(legende) +
            "</p>" +
            "<p class=\"pied\">" +
            esc(b.getCentreCode()) +
            " - " +
            esc(b.getDerniereAnnee()) +
            " - " +
            esc(b.getNom()) +
            ", " +
            esc(b.getPrenom()) +
            "</p>" +
            "</body></html>"
        );
    }

    private static String formatMoyenne(BigDecimal moyenne) {
        return moyenne == null ? "" : moyenne.toPlainString();
    }

    private static String esc(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
