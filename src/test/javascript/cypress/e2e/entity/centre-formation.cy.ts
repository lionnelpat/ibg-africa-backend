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

describe('CentreFormation e2e test', () => {
  const centreFormationPageUrl = '/centre-formation';
  const centreFormationPageUrlPattern = new RegExp('/centre-formation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const centreFormationSample = {
    code: 'actionnaire chef de ',
    nom: 'pendant que',
    ville: 'capter membre de l’équipe',
    signataire: 'déployer prou',
    nbCyclesCursus: 17,
    noteMaximale: 23600.92,
    actif: true,
  };

  let centreFormation;
  let pays;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/pays',
      body: { codeIso: 'ba', nom: 'ici', langue: 'désha', fuseau: 'concernant ensuite de peur de', actif: true },
    }).then(({ body }) => {
      pays = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/centre-formations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/centre-formations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/centre-formations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/pays', {
      statusCode: 200,
      body: [pays],
    });

    cy.intercept('GET', '/api/bareme-mentions', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parametres', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/cycles', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/habilitation-cycles', {
      statusCode: 200,
      body: [],
    });
  });

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

  afterEach(() => {
    if (pays) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pays/${pays.id}`,
      }).then(() => {
        pays = undefined;
      });
    }
  });

  it('CentreFormations menu should load CentreFormations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('centre-formation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CentreFormation').should('exist');
    cy.url().should('match', centreFormationPageUrlPattern);
  });

  describe('CentreFormation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(centreFormationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CentreFormation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/centre-formation/new$'));
        cy.getEntityCreateUpdateHeading('CentreFormation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', centreFormationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/centre-formations',
          body: {
            ...centreFormationSample,
            pays,
          },
        }).then(({ body }) => {
          centreFormation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/centre-formations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [centreFormation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(centreFormationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CentreFormation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('centreFormation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', centreFormationPageUrlPattern);
      });

      it('edit button click should load edit CentreFormation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CentreFormation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', centreFormationPageUrlPattern);
      });

      it('edit button click should load edit CentreFormation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CentreFormation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', centreFormationPageUrlPattern);
      });

      it('last delete button click should delete instance of CentreFormation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('centreFormation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', centreFormationPageUrlPattern);

        centreFormation = undefined;
      });
    });
  });

  describe('new CentreFormation page', () => {
    beforeEach(() => {
      cy.visit(`${centreFormationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CentreFormation');
    });

    it('should create an instance of CentreFormation', () => {
      cy.get(`[data-cy="code"]`).type('depuis longtemps');
      cy.get(`[data-cy="code"]`).should('have.value', 'depuis longtemps');

      cy.get(`[data-cy="nom"]`).type('vraisemblablement');
      cy.get(`[data-cy="nom"]`).should('have.value', 'vraisemblablement');

      cy.get(`[data-cy="ville"]`).type('dès que');
      cy.get(`[data-cy="ville"]`).should('have.value', 'dès que');

      cy.get(`[data-cy="adresse"]`).type('adversaire');
      cy.get(`[data-cy="adresse"]`).should('have.value', 'adversaire');

      cy.get(`[data-cy="enteteDocument"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="enteteDocument"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="signataire"]`).type('sincère hôte diététiste');
      cy.get(`[data-cy="signataire"]`).should('have.value', 'sincère hôte diététiste');

      cy.get(`[data-cy="logoUrl"]`).type('quand');
      cy.get(`[data-cy="logoUrl"]`).should('have.value', 'quand');

      cy.get(`[data-cy="nbCyclesCursus"]`).type('10');
      cy.get(`[data-cy="nbCyclesCursus"]`).should('have.value', '10');

      cy.get(`[data-cy="noteMaximale"]`).type('25935.86');
      cy.get(`[data-cy="noteMaximale"]`).should('have.value', '25935.86');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(`[data-cy="pays"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        centreFormation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', centreFormationPageUrlPattern);
    });
  });
});
