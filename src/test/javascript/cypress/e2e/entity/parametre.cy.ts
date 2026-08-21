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

describe('Parametre e2e test', () => {
  const parametrePageUrl = '/parametre';
  const parametrePageUrlPattern = new RegExp('/parametre(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const parametreSample = { cle: 'trop tout à fait membre du personnel', typeValeur: 'BOOLEEN', modifiableUi: false };

  let parametre;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/parametres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/parametres').as('postEntityRequest');
    cy.intercept('DELETE', '/api/parametres/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (parametre) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parametres/${parametre.id}`,
      }).then(() => {
        parametre = undefined;
      });
    }
  });

  it('Parametres menu should load Parametres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('parametre');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Parametre').should('exist');
    cy.url().should('match', parametrePageUrlPattern);
  });

  describe('Parametre page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(parametrePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Parametre page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/parametre/new$'));
        cy.getEntityCreateUpdateHeading('Parametre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parametrePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/parametres',
          body: parametreSample,
        }).then(({ body }) => {
          parametre = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/parametres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [parametre],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(parametrePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Parametre page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('parametre');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parametrePageUrlPattern);
      });

      it('edit button click should load edit Parametre page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Parametre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parametrePageUrlPattern);
      });

      it('edit button click should load edit Parametre page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Parametre');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parametrePageUrlPattern);
      });

      it('last delete button click should delete instance of Parametre', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('parametre').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', parametrePageUrlPattern);

        parametre = undefined;
      });
    });
  });

  describe('new Parametre page', () => {
    beforeEach(() => {
      cy.visit(`${parametrePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Parametre');
    });

    it('should create an instance of Parametre', () => {
      cy.get(`[data-cy="cle"]`).type('hi');
      cy.get(`[data-cy="cle"]`).should('have.value', 'hi');

      cy.get(`[data-cy="libelle"]`).type('mieux dérober');
      cy.get(`[data-cy="libelle"]`).should('have.value', 'mieux dérober');

      cy.get(`[data-cy="valeur"]`).type('vlan vouh');
      cy.get(`[data-cy="valeur"]`).should('have.value', 'vlan vouh');

      cy.get(`[data-cy="typeValeur"]`).select('NOMBRE');

      cy.get(`[data-cy="modifiableUi"]`).should('not.be.checked');
      cy.get(`[data-cy="modifiableUi"]`).click();
      cy.get(`[data-cy="modifiableUi"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        parametre = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', parametrePageUrlPattern);
    });
  });
});
