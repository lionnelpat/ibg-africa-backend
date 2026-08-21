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

describe('Cycle e2e test', () => {
  const cyclePageUrl = '/cycle';
  const cyclePageUrlPattern = new RegExp('/cycle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const cycleSample = {"annee":2077,"cloture":true};

  let cycle;
  // let centreFormation;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/centre-formations',
      body: {"code":"transmettre","nom":"innombrable drôlement entourer","ville":"membre du personnel","adresse":"bon mélanger dense","enteteDocument":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","signataire":"trop crac","logoUrl":"de crainte que","nbCyclesCursus":10,"noteMaximale":27427.77,"actif":false},
    }).then(({ body }) => {
      centreFormation = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cycles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cycles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cycles/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/centre-formations', {
      statusCode: 200,
      body: [centreFormation],
    });

    cy.intercept('GET', '/api/inscription-cycles', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/evaluation-prevues', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/habilitation-cycles', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (cycle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cycles/${cycle.id}`,
      }).then(() => {
        cycle = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (centreFormation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/centre-formations/${centreFormation.id}`,
      }).then(() => {
        centreFormation = undefined;
      });
    }
  });
   */

  it('Cycles menu should load Cycles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cycle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cycle').should('exist');
    cy.url().should('match', cyclePageUrlPattern);
  });

  describe('Cycle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cyclePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cycle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cycle/new$'));
        cy.getEntityCreateUpdateHeading('Cycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cycles',
          body: {
            ...cycleSample,
            centre: centreFormation,
          },
        }).then(({ body }) => {
          cycle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cycles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cycles?page=0&size=20>; rel="last",<http://localhost/api/cycles?page=0&size=20>; rel="first"',
              },
              body: [cycle],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cyclePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(cyclePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Cycle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cycle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });

      it('edit button click should load edit Cycle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });

      it('edit button click should load edit Cycle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cycle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Cycle', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cycle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);

        cycle = undefined;
      });
    });
  });

  describe('new Cycle page', () => {
    beforeEach(() => {
      cy.visit(`${cyclePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cycle');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Cycle', () => {
      cy.get(`[data-cy="annee"]`).type('1965');
      cy.get(`[data-cy="annee"]`).should('have.value', '1965');

      cy.get(`[data-cy="libelle"]`).type('drelin près embarquer');
      cy.get(`[data-cy="libelle"]`).should('have.value', 'drelin près embarquer');

      cy.get(`[data-cy="dateDebut"]`).type('2026-08-20');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="dateFin"]`).type('2026-08-20');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="cloture"]`).should('not.be.checked');
      cy.get(`[data-cy="cloture"]`).click();
      cy.get(`[data-cy="cloture"]`).should('be.checked');

      cy.get(`[data-cy="commentaire"]`).type('loufoque oh vétuste');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'loufoque oh vétuste');

      cy.get(`[data-cy="centre"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cycle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', cyclePageUrlPattern);
    });
  });
});
