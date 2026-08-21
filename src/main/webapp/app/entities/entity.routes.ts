import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'pays',
    data: { pageTitle: 'forbidecBackendApp.pays.home.title' },
    loadChildren: () => import('./pays/pays.routes'),
  },
  {
    path: 'centre-formation',
    data: { pageTitle: 'forbidecBackendApp.centreFormation.home.title' },
    loadChildren: () => import('./centre-formation/centre-formation.routes'),
  },
  {
    path: 'matiere',
    data: { pageTitle: 'forbidecBackendApp.matiere.home.title' },
    loadChildren: () => import('./matiere/matiere.routes'),
  },
  {
    path: 'sous-matiere',
    data: { pageTitle: 'forbidecBackendApp.sousMatiere.home.title' },
    loadChildren: () => import('./sous-matiere/sous-matiere.routes'),
  },
  {
    path: 'cours',
    data: { pageTitle: 'forbidecBackendApp.cours.home.title' },
    loadChildren: () => import('./cours/cours.routes'),
  },
  {
    path: 'type-tache',
    data: { pageTitle: 'forbidecBackendApp.typeTache.home.title' },
    loadChildren: () => import('./type-tache/type-tache.routes'),
  },
  {
    path: 'bareme-mention',
    data: { pageTitle: 'forbidecBackendApp.baremeMention.home.title' },
    loadChildren: () => import('./bareme-mention/bareme-mention.routes'),
  },
  {
    path: 'enseignant',
    data: { pageTitle: 'forbidecBackendApp.enseignant.home.title' },
    loadChildren: () => import('./enseignant/enseignant.routes'),
  },
  {
    path: 'etudiant',
    data: { pageTitle: 'forbidecBackendApp.etudiant.home.title' },
    loadChildren: () => import('./etudiant/etudiant.routes'),
  },
  {
    path: 'cycle',
    data: { pageTitle: 'forbidecBackendApp.cycle.home.title' },
    loadChildren: () => import('./cycle/cycle.routes'),
  },
  {
    path: 'inscription-cycle',
    data: { pageTitle: 'forbidecBackendApp.inscriptionCycle.home.title' },
    loadChildren: () => import('./inscription-cycle/inscription-cycle.routes'),
  },
  {
    path: 'evenement-etudiant',
    data: { pageTitle: 'forbidecBackendApp.evenementEtudiant.home.title' },
    loadChildren: () => import('./evenement-etudiant/evenement-etudiant.routes'),
  },
  {
    path: 'evaluation-prevue',
    data: { pageTitle: 'forbidecBackendApp.evaluationPrevue.home.title' },
    loadChildren: () => import('./evaluation-prevue/evaluation-prevue.routes'),
  },
  {
    path: 'evaluation-realisee',
    data: { pageTitle: 'forbidecBackendApp.evaluationRealisee.home.title' },
    loadChildren: () => import('./evaluation-realisee/evaluation-realisee.routes'),
  },
  {
    path: 'historique-note',
    data: { pageTitle: 'forbidecBackendApp.historiqueNote.home.title' },
    loadChildren: () => import('./historique-note/historique-note.routes'),
  },
  {
    path: 'parametre',
    data: { pageTitle: 'forbidecBackendApp.parametre.home.title' },
    loadChildren: () => import('./parametre/parametre.routes'),
  },
  {
    path: 'habilitation-cycle',
    data: { pageTitle: 'forbidecBackendApp.habilitationCycle.home.title' },
    loadChildren: () => import('./habilitation-cycle/habilitation-cycle.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
