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

describe('Enseignant e2e test', () => {
  const enseignantPageUrl = '/enseignant';
  const enseignantPageUrlPattern = new RegExp('/enseignant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enseignantSample = { nom: 'loin de affranchir près de', prenom: 'conquérir à peine simple', actif: true };

  let enseignant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/enseignants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/enseignants').as('postEntityRequest');
    cy.intercept('DELETE', '/api/enseignants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (enseignant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/enseignants/${enseignant.id}`,
      }).then(() => {
        enseignant = undefined;
      });
    }
  });

  it('Enseignants menu should load Enseignants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('enseignant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Enseignant').should('exist');
    cy.url().should('match', enseignantPageUrlPattern);
  });

  describe('Enseignant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enseignantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Enseignant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/enseignant/new$'));
        cy.getEntityCreateUpdateHeading('Enseignant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/enseignants',
          body: enseignantSample,
        }).then(({ body }) => {
          enseignant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/enseignants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/enseignants?page=0&size=20>; rel="last",<http://localhost/api/enseignants?page=0&size=20>; rel="first"',
              },
              body: [enseignant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enseignantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Enseignant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('enseignant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });

      it('edit button click should load edit Enseignant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enseignant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });

      it('edit button click should load edit Enseignant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enseignant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });

      it('last delete button click should delete instance of Enseignant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('enseignant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);

        enseignant = undefined;
      });
    });
  });

  describe('new Enseignant page', () => {
    beforeEach(() => {
      cy.visit(`${enseignantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Enseignant');
    });

    it('should create an instance of Enseignant', () => {
      cy.get(`[data-cy="nom"]`).type('bien que');
      cy.get(`[data-cy="nom"]`).should('have.value', 'bien que');

      cy.get(`[data-cy="prenom"]`).type('avant-hier');
      cy.get(`[data-cy="prenom"]`).should('have.value', 'avant-hier');

      cy.get(`[data-cy="libelleLong"]`).type('débile vu que');
      cy.get(`[data-cy="libelleLong"]`).should('have.value', 'débile vu que');

      cy.get(`[data-cy="libelleCourt"]`).type('assez');
      cy.get(`[data-cy="libelleCourt"]`).should('have.value', 'assez');

      cy.get(`[data-cy="email"]`).type('Gerard80@hotmail.fr');
      cy.get(`[data-cy="email"]`).should('have.value', 'Gerard80@hotmail.fr');

      cy.get(`[data-cy="telephone"]`).type('+33 542865412');
      cy.get(`[data-cy="telephone"]`).should('have.value', '+33 542865412');

      cy.get(`[data-cy="keycloakUserId"]`).type('grimper');
      cy.get(`[data-cy="keycloakUserId"]`).should('have.value', 'grimper');

      cy.get(`[data-cy="commentaire"]`).type('relire fonctionnaire ouin');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'relire fonctionnaire ouin');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        enseignant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', enseignantPageUrlPattern);
    });
  });
});
