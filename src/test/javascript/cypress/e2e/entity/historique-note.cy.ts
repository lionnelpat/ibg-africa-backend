import { entityDetailsBackButtonSelector, entityDetailsButtonSelector, entityTableSelector } from '../../support/entity';

describe('HistoriqueNote e2e test', () => {
  const historiqueNotePageUrl = '/historique-note';
  const historiqueNotePageUrlPattern = new RegExp('/historique-note(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';

  let historiqueNote;
  // let evaluationRealisee;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/evaluation-realisees',
      body: {"note":24675.86,"statut":"VALIDEE","compteDansMoyenne":false,"dateDebut":"2026-08-20","dateFin":"2026-08-20","commentaire1":"selon","commentaire2":"grandement","commentaire3":"sauf à avouer","saisiePar":"énergique d'avec","saisieLe":"2026-08-20T12:30:00.553Z","valideePar":"à l'instar de","valideeLe":"2026-08-20T03:59:56.062Z"},
    }).then(({ body }) => {
      evaluationRealisee = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/historique-notes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/historique-notes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/historique-notes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/evaluation-realisees', {
      statusCode: 200,
      body: [evaluationRealisee],
    });

  });
   */

  afterEach(() => {
    if (historiqueNote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/historique-notes/${historiqueNote.id}`,
      }).then(() => {
        historiqueNote = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (evaluationRealisee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/evaluation-realisees/${evaluationRealisee.id}`,
      }).then(() => {
        evaluationRealisee = undefined;
      });
    }
  });
   */

  it('HistoriqueNotes menu should load HistoriqueNotes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historique-note');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HistoriqueNote').should('exist');
    cy.url().should('match', historiqueNotePageUrlPattern);
  });

  describe('HistoriqueNote page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(historiqueNotePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details HistoriqueNote page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historiqueNote');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', historiqueNotePageUrlPattern);
      });
    });
  });
});
