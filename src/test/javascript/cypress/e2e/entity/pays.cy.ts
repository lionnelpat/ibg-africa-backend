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

describe('Pays e2e test', () => {
  const paysPageUrl = '/pays';
  const paysPageUrlPattern = new RegExp('/pays(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paysSample = { codeIso: 'ho', nom: 'contenter', langue: 'si mi', actif: true };

  let pays;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pays+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pays').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pays/*').as('deleteEntityRequest');
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

  it('Pays menu should load Pays page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pays');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Pays').should('exist');
    cy.url().should('match', paysPageUrlPattern);
  });

  describe('Pays page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paysPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Pays page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pays/new$'));
        cy.getEntityCreateUpdateHeading('Pays');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paysPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pays',
          body: paysSample,
        }).then(({ body }) => {
          pays = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pays+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [pays],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paysPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Pays page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('pays');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paysPageUrlPattern);
      });

      it('edit button click should load edit Pays page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pays');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paysPageUrlPattern);
      });

      it('edit button click should load edit Pays page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Pays');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paysPageUrlPattern);
      });

      it('last delete button click should delete instance of Pays', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('pays').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', paysPageUrlPattern);

        pays = undefined;
      });
    });
  });

  describe('new Pays page', () => {
    beforeEach(() => {
      cy.visit(`${paysPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Pays');
    });

    it('should create an instance of Pays', () => {
      cy.get(`[data-cy="codeIso"]`).type('do');
      cy.get(`[data-cy="codeIso"]`).should('have.value', 'do');

      cy.get(`[data-cy="nom"]`).type('derrière');
      cy.get(`[data-cy="nom"]`).should('have.value', 'derrière');

      cy.get(`[data-cy="langue"]`).type('spéci');
      cy.get(`[data-cy="langue"]`).should('have.value', 'spéci');

      cy.get(`[data-cy="fuseau"]`).type('sauf lunatique');
      cy.get(`[data-cy="fuseau"]`).should('have.value', 'sauf lunatique');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        pays = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', paysPageUrlPattern);
    });
  });
});
