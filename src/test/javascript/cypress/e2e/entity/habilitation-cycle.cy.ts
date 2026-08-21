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

describe('HabilitationCycle e2e test', () => {
  const habilitationCyclePageUrl = '/habilitation-cycle';
  const habilitationCyclePageUrlPattern = new RegExp('/habilitation-cycle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const habilitationCycleSample = { keycloakUserId: 'cadre pisser', roleFonctionnel: 'ADMIN' };

  let habilitationCycle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/habilitation-cycles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/habilitation-cycles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/habilitation-cycles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (habilitationCycle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/habilitation-cycles/${habilitationCycle.id}`,
      }).then(() => {
        habilitationCycle = undefined;
      });
    }
  });

  it('HabilitationCycles menu should load HabilitationCycles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('habilitation-cycle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HabilitationCycle').should('exist');
    cy.url().should('match', habilitationCyclePageUrlPattern);
  });

  describe('HabilitationCycle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(habilitationCyclePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HabilitationCycle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/habilitation-cycle/new$'));
        cy.getEntityCreateUpdateHeading('HabilitationCycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationCyclePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/habilitation-cycles',
          body: habilitationCycleSample,
        }).then(({ body }) => {
          habilitationCycle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/habilitation-cycles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/habilitation-cycles?page=0&size=20>; rel="last",<http://localhost/api/habilitation-cycles?page=0&size=20>; rel="first"',
              },
              body: [habilitationCycle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(habilitationCyclePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HabilitationCycle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('habilitationCycle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationCyclePageUrlPattern);
      });

      it('edit button click should load edit HabilitationCycle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HabilitationCycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationCyclePageUrlPattern);
      });

      it('edit button click should load edit HabilitationCycle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HabilitationCycle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationCyclePageUrlPattern);
      });

      it('last delete button click should delete instance of HabilitationCycle', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('habilitationCycle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', habilitationCyclePageUrlPattern);

        habilitationCycle = undefined;
      });
    });
  });

  describe('new HabilitationCycle page', () => {
    beforeEach(() => {
      cy.visit(`${habilitationCyclePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HabilitationCycle');
    });

    it('should create an instance of HabilitationCycle', () => {
      cy.get(`[data-cy="keycloakUserId"]`).type('ci dense');
      cy.get(`[data-cy="keycloakUserId"]`).should('have.value', 'ci dense');

      cy.get(`[data-cy="roleFonctionnel"]`).select('ETUDIANT');

      cy.get(`[data-cy="dateDebut"]`).type('2026-08-20');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="dateFin"]`).type('2026-08-20');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-08-20');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        habilitationCycle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', habilitationCyclePageUrlPattern);
    });
  });
});
