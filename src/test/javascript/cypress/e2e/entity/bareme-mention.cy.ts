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

describe('BaremeMention e2e test', () => {
  const baremeMentionPageUrl = '/bareme-mention';
  const baremeMentionPageUrlPattern = new RegExp('/bareme-mention(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const baremeMentionSample = {
    libelleLong: 'adversaire splendide',
    libelleCourt: 'à défaut de',
    minInclus: true,
    maxInclus: false,
    ordreAffichage: 27482,
    actif: false,
  };

  let baremeMention;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bareme-mentions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bareme-mentions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bareme-mentions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (baremeMention) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bareme-mentions/${baremeMention.id}`,
      }).then(() => {
        baremeMention = undefined;
      });
    }
  });

  it('BaremeMentions menu should load BaremeMentions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bareme-mention');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BaremeMention').should('exist');
    cy.url().should('match', baremeMentionPageUrlPattern);
  });

  describe('BaremeMention page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(baremeMentionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BaremeMention page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bareme-mention/new$'));
        cy.getEntityCreateUpdateHeading('BaremeMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', baremeMentionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bareme-mentions',
          body: baremeMentionSample,
        }).then(({ body }) => {
          baremeMention = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bareme-mentions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [baremeMention],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(baremeMentionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BaremeMention page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('baremeMention');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', baremeMentionPageUrlPattern);
      });

      it('edit button click should load edit BaremeMention page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BaremeMention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', baremeMentionPageUrlPattern);
      });

      it('edit button click should load edit BaremeMention page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BaremeMention');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', baremeMentionPageUrlPattern);
      });

      it('last delete button click should delete instance of BaremeMention', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('baremeMention').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', baremeMentionPageUrlPattern);

        baremeMention = undefined;
      });
    });
  });

  describe('new BaremeMention page', () => {
    beforeEach(() => {
      cy.visit(`${baremeMentionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BaremeMention');
    });

    it('should create an instance of BaremeMention', () => {
      cy.get(`[data-cy="libelleLong"]`).type('jeter');
      cy.get(`[data-cy="libelleLong"]`).should('have.value', 'jeter');

      cy.get(`[data-cy="libelleCourt"]`).type('maigre');
      cy.get(`[data-cy="libelleCourt"]`).should('have.value', 'maigre');

      cy.get(`[data-cy="borneMin"]`).type('19727.02');
      cy.get(`[data-cy="borneMin"]`).should('have.value', '19727.02');

      cy.get(`[data-cy="minInclus"]`).should('not.be.checked');
      cy.get(`[data-cy="minInclus"]`).click();
      cy.get(`[data-cy="minInclus"]`).should('be.checked');

      cy.get(`[data-cy="borneMax"]`).type('29212.16');
      cy.get(`[data-cy="borneMax"]`).should('have.value', '29212.16');

      cy.get(`[data-cy="maxInclus"]`).should('not.be.checked');
      cy.get(`[data-cy="maxInclus"]`).click();
      cy.get(`[data-cy="maxInclus"]`).should('be.checked');

      cy.get(`[data-cy="ordreAffichage"]`).type('24099');
      cy.get(`[data-cy="ordreAffichage"]`).should('have.value', '24099');

      cy.get(`[data-cy="commentaire"]`).type('pourvu que avaler diplomate');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'pourvu que avaler diplomate');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        baremeMention = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', baremeMentionPageUrlPattern);
    });
  });
});
