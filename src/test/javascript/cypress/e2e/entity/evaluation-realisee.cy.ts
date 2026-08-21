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

describe('EvaluationRealisee e2e test', () => {
  const evaluationRealiseePageUrl = '/evaluation-realisee';
  const evaluationRealiseePageUrlPattern = new RegExp('/evaluation-realisee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const evaluationRealiseeSample = {"statut":"NON_SAISIE","compteDansMoyenne":true};

  let evaluationRealisee;
  // let evaluationPrevue;
  // let etudiant;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/evaluation-prevues',
      body: {"intitule":"du côté de","libelleImpression":"pacifique sous couleur de","coefficient":2532.74,"compteDansMoyenne":false,"noteMaximale":31779.46,"dateDebut":"2026-08-20","dateFin":"2026-08-20","commentaire":"établir charitable via"},
    }).then(({ body }) => {
      evaluationPrevue = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/etudiants',
      body: {"matricule":"incalculable moins","nom":"avant","prenom":"passablement porte-parole orange","particularite":"premièrement pin-pon assurément","dateNaissance":"2026-08-20","email":"Alliaume_Fabre24@hotmail.fr","telephone":"+33 769818139","anneeEntree":2149,"cursusAcheve":true,"anneeFinale":1962,"keycloakUserId":"quitte à de manière à ce que","commentaire":"dresser hésiter","actif":false},
    }).then(({ body }) => {
      etudiant = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/evaluation-realisees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/evaluation-realisees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/evaluation-realisees/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/evaluation-prevues', {
      statusCode: 200,
      body: [evaluationPrevue],
    });

    cy.intercept('GET', '/api/etudiants', {
      statusCode: 200,
      body: [etudiant],
    });

    cy.intercept('GET', '/api/historique-notes', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (evaluationRealisee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/evaluation-realisees/${evaluationRealisee.id}`,
      }).then(() => {
        evaluationRealisee = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (evaluationPrevue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/evaluation-prevues/${evaluationPrevue.id}`,
      }).then(() => {
        evaluationPrevue = undefined;
      });
    }
    if (etudiant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/etudiants/${etudiant.id}`,
      }).then(() => {
        etudiant = undefined;
      });
    }
  });
   */

  it('EvaluationRealisees menu should load EvaluationRealisees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('evaluation-realisee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EvaluationRealisee').should('exist');
    cy.url().should('match', evaluationRealiseePageUrlPattern);
  });

  describe('EvaluationRealisee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(evaluationRealiseePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EvaluationRealisee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/evaluation-realisee/new$'));
        cy.getEntityCreateUpdateHeading('EvaluationRealisee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationRealiseePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/evaluation-realisees',
          body: {
            ...evaluationRealiseeSample,
            evaluationPrevue: evaluationPrevue,
            etudiant: etudiant,
          },
        }).then(({ body }) => {
          evaluationRealisee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/evaluation-realisees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/evaluation-realisees?page=0&size=20>; rel="last",<http://localhost/api/evaluation-realisees?page=0&size=20>; rel="first"',
              },
              body: [evaluationRealisee],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(evaluationRealiseePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(evaluationRealiseePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details EvaluationRealisee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('evaluationRealisee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationRealiseePageUrlPattern);
      });

      it('edit button click should load edit EvaluationRealisee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EvaluationRealisee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationRealiseePageUrlPattern);
      });

      it('edit button click should load edit EvaluationRealisee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EvaluationRealisee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationRealiseePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of EvaluationRealisee', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('evaluationRealisee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationRealiseePageUrlPattern);

        evaluationRealisee = undefined;
      });
    });
  });

  describe('new EvaluationRealisee page', () => {
    beforeEach(() => {
      cy.visit(`${evaluationRealiseePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EvaluationRealisee');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of EvaluationRealisee', () => {
      cy.get(`[data-cy="note"]`).type('31037.2');
      cy.get(`[data-cy="note"]`).should('have.value', '31037.2');

      cy.get(`[data-cy="statut"]`).select('NON_SAISIE');

      cy.get(`[data-cy="compteDansMoyenne"]`).should('not.be.checked');
      cy.get(`[data-cy="compteDansMoyenne"]`).click();
      cy.get(`[data-cy="compteDansMoyenne"]`).should('be.checked');

      cy.get(`[data-cy="dateDebut"]`).type('2026-08-20');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="dateFin"]`).type('2026-08-20');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="commentaire1"]`).type("à l'encontre de badaboum spécialiste");
      cy.get(`[data-cy="commentaire1"]`).should('have.value', "à l'encontre de badaboum spécialiste");

      cy.get(`[data-cy="commentaire2"]`).type('céder rédaction par rapport à');
      cy.get(`[data-cy="commentaire2"]`).should('have.value', 'céder rédaction par rapport à');

      cy.get(`[data-cy="commentaire3"]`).type('ensemble où trouver');
      cy.get(`[data-cy="commentaire3"]`).should('have.value', 'ensemble où trouver');

      cy.get(`[data-cy="saisiePar"]`).type('loin de');
      cy.get(`[data-cy="saisiePar"]`).should('have.value', 'loin de');

      cy.get(`[data-cy="saisieLe"]`).type('2026-08-20T11:17');
      cy.get(`[data-cy="saisieLe"]`).blur();
      cy.get(`[data-cy="saisieLe"]`).should('have.value', '2026-08-20T11:17');

      cy.get(`[data-cy="valideePar"]`).type('serviable');
      cy.get(`[data-cy="valideePar"]`).should('have.value', 'serviable');

      cy.get(`[data-cy="valideeLe"]`).type('2026-08-20T17:24');
      cy.get(`[data-cy="valideeLe"]`).blur();
      cy.get(`[data-cy="valideeLe"]`).should('have.value', '2026-08-20T17:24');

      cy.get(`[data-cy="evaluationPrevue"]`).select(1);
      cy.get(`[data-cy="etudiant"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        evaluationRealisee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', evaluationRealiseePageUrlPattern);
    });
  });
});
