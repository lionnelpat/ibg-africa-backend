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

describe('EvenementEtudiant e2e test', () => {
  const evenementEtudiantPageUrl = '/evenement-etudiant';
  const evenementEtudiantPageUrlPattern = new RegExp('/evenement-etudiant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const evenementEtudiantSample = { intitule: 'pacifique' };

  let evenementEtudiant;
  let etudiant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/etudiants',
      body: {
        matricule: 'direction',
        nom: 'main-d’œuvre par rapport à quoique',
        prenom: 'déployer',
        particularite: 'sombrer encore très',
        dateNaissance: '2026-08-20',
        email: 'Flavie.Pons67@gmail.com',
        telephone: '+33 478031466',
        anneeEntree: 2083,
        cursusAcheve: false,
        anneeFinale: 2096,
        keycloakUserId: 'débile payer',
        commentaire: "exprès à l'exception de pour que",
        actif: false,
      },
    }).then(({ body }) => {
      etudiant = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/evenement-etudiants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/evenement-etudiants').as('postEntityRequest');
    cy.intercept('DELETE', '/api/evenement-etudiants/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/etudiants', {
      statusCode: 200,
      body: [etudiant],
    });
  });

  afterEach(() => {
    if (evenementEtudiant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/evenement-etudiants/${evenementEtudiant.id}`,
      }).then(() => {
        evenementEtudiant = undefined;
      });
    }
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

  it('EvenementEtudiants menu should load EvenementEtudiants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('evenement-etudiant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EvenementEtudiant').should('exist');
    cy.url().should('match', evenementEtudiantPageUrlPattern);
  });

  describe('EvenementEtudiant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(evenementEtudiantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EvenementEtudiant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/evenement-etudiant/new$'));
        cy.getEntityCreateUpdateHeading('EvenementEtudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementEtudiantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/evenement-etudiants',
          body: {
            ...evenementEtudiantSample,
            etudiant,
          },
        }).then(({ body }) => {
          evenementEtudiant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/evenement-etudiants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/evenement-etudiants?page=0&size=20>; rel="last",<http://localhost/api/evenement-etudiants?page=0&size=20>; rel="first"',
              },
              body: [evenementEtudiant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(evenementEtudiantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EvenementEtudiant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('evenementEtudiant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementEtudiantPageUrlPattern);
      });

      it('edit button click should load edit EvenementEtudiant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EvenementEtudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementEtudiantPageUrlPattern);
      });

      it('edit button click should load edit EvenementEtudiant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EvenementEtudiant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementEtudiantPageUrlPattern);
      });

      it('last delete button click should delete instance of EvenementEtudiant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('evenementEtudiant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evenementEtudiantPageUrlPattern);

        evenementEtudiant = undefined;
      });
    });
  });

  describe('new EvenementEtudiant page', () => {
    beforeEach(() => {
      cy.visit(`${evenementEtudiantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EvenementEtudiant');
    });

    it('should create an instance of EvenementEtudiant', () => {
      cy.get(`[data-cy="dateEvenement"]`).type('2026-08-20');
      cy.get(`[data-cy="dateEvenement"]`).blur();
      cy.get(`[data-cy="dateEvenement"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="intitule"]`).type('suivant spécialiste');
      cy.get(`[data-cy="intitule"]`).should('have.value', 'suivant spécialiste');

      cy.get(`[data-cy="commentaire"]`).type('bang');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'bang');

      cy.get(`[data-cy="etudiant"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        evenementEtudiant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', evenementEtudiantPageUrlPattern);
    });
  });
});
