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

describe('TypeTache e2e test', () => {
  const typeTachePageUrl = '/type-tache';
  const typeTachePageUrlPattern = new RegExp('/type-tache(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeTacheSample = { code: 'aux environs de', intitule: 'diététiste', entreDansMoyenne: false, actif: true };

  let typeTache;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/type-taches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/type-taches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/type-taches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeTache) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/type-taches/${typeTache.id}`,
      }).then(() => {
        typeTache = undefined;
      });
    }
  });

  it('TypeTaches menu should load TypeTaches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('type-tache');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeTache').should('exist');
    cy.url().should('match', typeTachePageUrlPattern);
  });

  describe('TypeTache page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeTachePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeTache page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/type-tache/new$'));
        cy.getEntityCreateUpdateHeading('TypeTache');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', typeTachePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/type-taches',
          body: typeTacheSample,
        }).then(({ body }) => {
          typeTache = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/type-taches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [typeTache],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeTachePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeTache page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeTache');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', typeTachePageUrlPattern);
      });

      it('edit button click should load edit TypeTache page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeTache');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', typeTachePageUrlPattern);
      });

      it('edit button click should load edit TypeTache page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeTache');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', typeTachePageUrlPattern);
      });

      it('last delete button click should delete instance of TypeTache', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeTache').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', typeTachePageUrlPattern);

        typeTache = undefined;
      });
    });
  });

  describe('new TypeTache page', () => {
    beforeEach(() => {
      cy.visit(`${typeTachePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeTache');
    });

    it('should create an instance of TypeTache', () => {
      cy.get(`[data-cy="code"]`).type('aussitôt que');
      cy.get(`[data-cy="code"]`).should('have.value', 'aussitôt que');

      cy.get(`[data-cy="intitule"]`).type('moderne');
      cy.get(`[data-cy="intitule"]`).should('have.value', 'moderne');

      cy.get(`[data-cy="libelleLong"]`).type('couvrir psitt par suite de');
      cy.get(`[data-cy="libelleLong"]`).should('have.value', 'couvrir psitt par suite de');

      cy.get(`[data-cy="libelleCourt"]`).type('ressentir');
      cy.get(`[data-cy="libelleCourt"]`).should('have.value', 'ressentir');

      cy.get(`[data-cy="entreDansMoyenne"]`).should('not.be.checked');
      cy.get(`[data-cy="entreDansMoyenne"]`).click();
      cy.get(`[data-cy="entreDansMoyenne"]`).should('be.checked');

      cy.get(`[data-cy="commentaire"]`).type('mince prout');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'mince prout');

      cy.get(`[data-cy="actif"]`).should('not.be.checked');
      cy.get(`[data-cy="actif"]`).click();
      cy.get(`[data-cy="actif"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        typeTache = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', typeTachePageUrlPattern);
    });
  });
});
