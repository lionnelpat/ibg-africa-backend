package org.forbidec.service.dto.dashboard;

import java.io.Serializable;
import java.util.List;

/**
 * Reporting global : effectifs, réussite, évolution des inscriptions,
 * répartition des mentions, dernières sessions.
 */
public class DashboardDTO implements Serializable {

    private long totalEtudiants;
    private long totalEtudiantsActifs;
    private long totalCycles;
    private long totalEnseignants;
    private long totalFinissants;
    private double tauxReussite;

    private List<EvolutionAnneeDTO> evolutionInscriptions;
    private List<RepartitionMentionDTO> repartitionMentions;
    private List<SessionRecenteDTO> dernieresSessions;

    public long getTotalEtudiants() {
        return totalEtudiants;
    }

    public void setTotalEtudiants(long totalEtudiants) {
        this.totalEtudiants = totalEtudiants;
    }

    public long getTotalEtudiantsActifs() {
        return totalEtudiantsActifs;
    }

    public void setTotalEtudiantsActifs(long totalEtudiantsActifs) {
        this.totalEtudiantsActifs = totalEtudiantsActifs;
    }

    public long getTotalCycles() {
        return totalCycles;
    }

    public void setTotalCycles(long totalCycles) {
        this.totalCycles = totalCycles;
    }

    public long getTotalEnseignants() {
        return totalEnseignants;
    }

    public void setTotalEnseignants(long totalEnseignants) {
        this.totalEnseignants = totalEnseignants;
    }

    public long getTotalFinissants() {
        return totalFinissants;
    }

    public void setTotalFinissants(long totalFinissants) {
        this.totalFinissants = totalFinissants;
    }

    public double getTauxReussite() {
        return tauxReussite;
    }

    public void setTauxReussite(double tauxReussite) {
        this.tauxReussite = tauxReussite;
    }

    public List<EvolutionAnneeDTO> getEvolutionInscriptions() {
        return evolutionInscriptions;
    }

    public void setEvolutionInscriptions(List<EvolutionAnneeDTO> evolutionInscriptions) {
        this.evolutionInscriptions = evolutionInscriptions;
    }

    public List<RepartitionMentionDTO> getRepartitionMentions() {
        return repartitionMentions;
    }

    public void setRepartitionMentions(List<RepartitionMentionDTO> repartitionMentions) {
        this.repartitionMentions = repartitionMentions;
    }

    public List<SessionRecenteDTO> getDernieresSessions() {
        return dernieresSessions;
    }

    public void setDernieresSessions(List<SessionRecenteDTO> dernieresSessions) {
        this.dernieresSessions = dernieresSessions;
    }
}
