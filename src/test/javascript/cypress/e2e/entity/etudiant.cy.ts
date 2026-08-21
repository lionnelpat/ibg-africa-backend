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

describe('Etudiant e2e test', () => {
  const etudiantPageUrl = '/etudiant';
  const etudiantPageUrlPattern = new RegExp('/etudiant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const etudiantSample = { nom: 'accumuler', prenom: 'turquoise priver', cursusAcheve: true, actif: true };

  let etudiant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/etudiants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/etudiants').as('postEntityRequest');
    cy.intercept('DELETE', '/api/etudiants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (etudiant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/etudiants/${etudiant.id}`,
      }).then(() => {
        etudiant = undefined;
      });
    }
  });

  it('Etudiants menu should load Etudiants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('etudiant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Etudiant').should('exist');
    cy.url().should('match', etudiantPageUrlPattern);
  });

  describe('Etudiant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(etudiantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Etudiant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/etudiant/new$'));
        cy.getEntityCreateUpdateHeading('Etudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/etudiants',
          body: etudiantSample,
        }).then(({ body }) => {
          etudiant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/etudiants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/etudiants?page=0&size=20>; rel="last",<http://localhost/api/etudiants?page=0&size=20>; rel="first"',
              },
              body: [etudiant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(etudiantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Etudiant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('etudiant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });

      it('edit button click should load edit Etudiant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Etudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });

      it('edit button click should load edit Etudiant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Etudiant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });

      it('last delete button click should delete instance of Etudiant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('etudiant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);

        etudiant = undefined;
      });
    });
  });

  describe('new Etudiant page', () => {
    beforeEach(() => {
      cy.visit(`${etudiantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Etudiant');
    });

    it('should create an instance of Etudiant', () => {
      cy.get(`[data-cy="matricule"]`).type('loufoque trancher');
      cy.get(`[data-cy="matricule"]`).should('have.value', 'loufoque trancher');

      cy.get(`[data-cy="nom"]`).type('antagoniste');
      cy.get(`[data-cy="nom"]`).should('have.value', 'antagoniste');

      cy.get(`[data-cy="prenom"]`).type('corps enseignant');
      cy.get(`[data-cy="prenom"]`).should('have.value', 'corps enseignant');

      cy.get(`[data-cy="particularite"]`).type('bè');
      cy.get(`[data-cy="particularite"]`).should('have.value', 'bè');

      cy.get(`[data-cy="dateNaissance"]`).type('2026-08-20');
      cy.get(`[data-cy="dateNaissance"]`).blur();
      cy.get(`[data-cy="dateNaissance"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="email"]`).type('Amalric95@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Amalric95@gmail.com');

      cy.get(`[data-cy="telephone"]`).type('0391513946');
      cy.get(`[data-cy="telephone"]`).should('have.value', '0391513946');

      cy.get(`[data-cy="anneeEntree"]`).type('1989');
      cy.get(`[data-cy="anneeEntree"]`).should('have.value', '1989');

      cy.get(`[data-cy="cursusAcheve"]`).should('not.be.checked');
      cy.get(`[data-cy="cursusAcheve"]`).click();
      cy.get(`[data-cy="cursusAcheve"]`).should('be.checked');

      cy.get(`[data-cy="anneeFinale"]`).type('2022');
      cy.get(`[data-cy="anneeFinale"]`).should('have.value', '2022');

      cy.get(`[data-cy="keycloakUserId"]`).type('trop de peur que police');
      cy.get(`[data-cy="keycloakUserId"]`).should('have.value', 'trop de peur que police');

      cy.get(`[data-cy="commentaire"]`).type('en dehors de');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'en dehors de');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        etudiant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', etudiantPageUrlPattern);
    });
  });
});
