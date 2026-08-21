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

describe('SousMatiere e2e test', () => {
  const sousMatierePageUrl = '/sous-matiere';
  const sousMatierePageUrlPattern = new RegExp('/sous-matiere(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const sousMatiereSample = { intitule: 'en faveur de aux environs de', actif: true };

  let sousMatiere;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sous-matieres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sous-matieres').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sous-matieres/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (sousMatiere) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sous-matieres/${sousMatiere.id}`,
      }).then(() => {
        sousMatiere = undefined;
      });
    }
  });

  it('SousMatieres menu should load SousMatieres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sous-matiere');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SousMatiere').should('exist');
    cy.url().should('match', sousMatierePageUrlPattern);
  });

  describe('SousMatiere page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sousMatierePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SousMatiere page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sous-matiere/new$'));
        cy.getEntityCreateUpdateHeading('SousMatiere');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sousMatierePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sous-matieres',
          body: sousMatiereSample,
        }).then(({ body }) => {
          sousMatiere = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sous-matieres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sous-matieres?page=0&size=20>; rel="last",<http://localhost/api/sous-matieres?page=0&size=20>; rel="first"',
              },
              body: [sousMatiere],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sousMatierePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SousMatiere page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('sousMatiere');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sousMatierePageUrlPattern);
      });

      it('edit button click should load edit SousMatiere page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SousMatiere');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sousMatierePageUrlPattern);
      });

      it('edit button click should load edit SousMatiere page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SousMatiere');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sousMatierePageUrlPattern);
      });

      it('last delete button click should delete instance of SousMatiere', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('sousMatiere').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sousMatierePageUrlPattern);

        sousMatiere = undefined;
      });
    });
  });

  describe('new SousMatiere page', () => {
    beforeEach(() => {
      cy.visit(`${sousMatierePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SousMatiere');
    });

    it('should create an instance of SousMatiere', () => {
      cy.get(`[data-cy="intitule"]`).type('séculaire de façon à');
      cy.get(`[data-cy="intitule"]`).should('have.value', 'séculaire de façon à');

      cy.get(`[data-cy="libelleLong"]`).type('ensuite');
      cy.get(`[data-cy="libelleLong"]`).should('have.value', 'ensuite');

      cy.get(`[data-cy="libelleCourt"]`).type('terriblement');
      cy.get(`[data-cy="libelleCourt"]`).should('have.value', 'terriblement');

      cy.get(`[data-cy="commentaire"]`).type('loger ouille blablabla');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'loger ouille blablabla');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        sousMatiere = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', sousMatierePageUrlPattern);
    });
  });
});
