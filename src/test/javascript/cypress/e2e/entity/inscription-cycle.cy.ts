import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('InscriptionCycle e2e test', () => {
  const inscriptionCyclePageUrl = '/inscription-cycle';
  const inscriptionCyclePageUrlPattern = new RegExp('/inscription-cycle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const inscriptionCycleSample = {"cycleTermine":false};

  let inscriptionCycle;
  // let cycle;
  // let etudiant;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cycles',
      body: {"annee":2135,"libelle":"toc","dateDebut":"2026-08-20","dateFin":"2026-08-20","cloture":true,"commentaire":"hormis à l'entour de rédaction"},
    }).then(({ body }) => {
      cycle = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/etudiants',
      body: {"matricule":"tic-tac triathlète","nom":"multiple athlète groin groin","prenom":"de peur que allonger","particularite":"accepter à la merci grrr","dateNaissance":"2026-08-20","email":"Eva.Prevost53@yahoo.fr","telephone":"0193241980","anneeEntree":2117,"cursusAcheve":true,"anneeFinale":1980,"keycloakUserId":"rectorat retarder prout","commentaire":"électorat même","actif":true},
    }).then(({ body }) => {
      etudiant = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/inscription-cycles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inscription-cycles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inscription-cycles/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cycles', {
      statusCode: 200,
      body: [cycle],
    });

    cy.intercept('GET', '/api/etudiants', {
      statusCode: 200,
      body: [etudiant],
    });

  });
   */

  afterEach(() => {
    if (inscriptionCycle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inscription-cycles/${inscriptionCycle.id}`,
      }).then(() => {
        inscriptionCycle = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (cycle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cycles/${cycle.id}`,
      }).then(() => {
        cycle = undefined;
      });
    }
    if (etudiant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/etudiants/${etudiant.id}`,
      }).then(() => {
        etudiant = undefined;
      });
    }
  });
   */

  it('InscriptionCycles menu should load InscriptionCycles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inscription-cycle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InscriptionCycle').should('exist');
    cy.url().should('match', inscriptionCyclePageUrlPattern);
  });

  describe('InscriptionCycle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inscriptionCyclePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InscriptionCycle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inscription-cycle/new$'));
        cy.getEntityCreateUpdateHeading('InscriptionCycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionCyclePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inscription-cycles',
          body: {
            ...inscriptionCycleSample,
            cycle: cycle,
            etudiant: etudiant,
          },
        }).then(({ body }) => {
          inscriptionCycle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inscription-cycles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inscription-cycles?page=0&size=20>; rel="last",<http://localhost/api/inscription-cycles?page=0&size=20>; rel="first"',
              },
              body: [inscriptionCycle],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(inscriptionCyclePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(inscriptionCyclePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details InscriptionCycle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inscriptionCycle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionCyclePageUrlPattern);
      });

      it('edit button click should load edit InscriptionCycle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionCycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionCyclePageUrlPattern);
      });

      it('edit button click should load edit InscriptionCycle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionCycle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionCyclePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of InscriptionCycle', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inscriptionCycle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionCyclePageUrlPattern);

        inscriptionCycle = undefined;
      });
    });
  });

  describe('new InscriptionCycle page', () => {
    beforeEach(() => {
      cy.visit(`${inscriptionCyclePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InscriptionCycle');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of InscriptionCycle', () => {
      cy.get(`[data-cy="dateInscription"]`).type('2026-08-20');
      cy.get(`[data-cy="dateInscription"]`).blur();
      cy.get(`[data-cy="dateInscription"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="cycleTermine"]`).should('not.be.checked');
      cy.get(`[data-cy="cycleTermine"]`).click();
      cy.get(`[data-cy="cycleTermine"]`).should('be.checked');

      cy.get(`[data-cy="groupe"]`).type('que');
      cy.get(`[data-cy="groupe"]`).should('have.value', 'que');

      cy.get(`[data-cy="commentaire1"]`).type('miaou');
      cy.get(`[data-cy="commentaire1"]`).should('have.value', 'miaou');

      cy.get(`[data-cy="commentaire2"]`).type('quand');
      cy.get(`[data-cy="commentaire2"]`).should('have.value', 'quand');

      cy.get(`[data-cy="commentaire3"]`).type('consacrer à la faveur de meuh');
      cy.get(`[data-cy="commentaire3"]`).should('have.value', 'consacrer à la faveur de meuh');

      cy.get(`[data-cy="commentaire5"]`).type('orange');
      cy.get(`[data-cy="commentaire5"]`).should('have.value', 'orange');

      cy.get(`[data-cy="cycle"]`).select(1);
      cy.get(`[data-cy="etudiant"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inscriptionCycle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', inscriptionCyclePageUrlPattern);
    });
  });
});
