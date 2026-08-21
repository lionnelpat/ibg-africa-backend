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

describe('Cours e2e test', () => {
  const coursPageUrl = '/cours';
  const coursPageUrlPattern = new RegExp('/cours(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const coursSample = { intitule: 'cadre', ordreAffichage: 23117, coefficient: 24801.5, actif: true };

  let cours;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cours+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cours').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cours/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cours) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cours/${cours.id}`,
      }).then(() => {
        cours = undefined;
      });
    }
  });

  it('Cours menu should load Cours page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cours');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cours').should('exist');
    cy.url().should('match', coursPageUrlPattern);
  });

  describe('Cours page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(coursPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cours page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/cours/new$'));
        cy.getEntityCreateUpdateHeading('Cours');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coursPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cours',
          body: coursSample,
        }).then(({ body }) => {
          cours = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cours+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cours?page=0&size=20>; rel="last",<http://localhost/api/cours?page=0&size=20>; rel="first"',
              },
              body: [cours],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(coursPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cours page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cours');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coursPageUrlPattern);
      });

      it('edit button click should load edit Cours page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cours');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coursPageUrlPattern);
      });

      it('edit button click should load edit Cours page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cours');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coursPageUrlPattern);
      });

      it('last delete button click should delete instance of Cours', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cours').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coursPageUrlPattern);

        cours = undefined;
      });
    });
  });

  describe('new Cours page', () => {
    beforeEach(() => {
      cy.visit(`${coursPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cours');
    });

    it('should create an instance of Cours', () => {
      cy.get(`[data-cy="intitule"]`).type('bang après que');
      cy.get(`[data-cy="intitule"]`).should('have.value', 'bang après que');

      cy.get(`[data-cy="libelleLong"]`).type('brusque rédaction');
      cy.get(`[data-cy="libelleLong"]`).should('have.value', 'brusque rédaction');

      cy.get(`[data-cy="libelleCourt"]`).type('sous toutefois doucement');
      cy.get(`[data-cy="libelleCourt"]`).should('have.value', 'sous toutefois doucement');

      cy.get(`[data-cy="ordreAffichage"]`).type('32493');
      cy.get(`[data-cy="ordreAffichage"]`).should('have.value', '32493');

      cy.get(`[data-cy="nbPeriodes"]`).type('16966');
      cy.get(`[data-cy="nbPeriodes"]`).should('have.value', '16966');

      cy.get(`[data-cy="coefficient"]`).type('11585.97');
      cy.get(`[data-cy="coefficient"]`).should('have.value', '11585.97');

      cy.get(`[data-cy="dateDebut"]`).type('2026-08-20');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="dateFin"]`).type('2026-08-20');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="commentaire"]`).type('boum efficace épuiser');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'boum efficace épuiser');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cours = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', coursPageUrlPattern);
    });
  });
});
