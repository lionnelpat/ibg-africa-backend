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

describe('EvaluationPrevue e2e test', () => {
  const evaluationPrevuePageUrl = '/evaluation-prevue';
  const evaluationPrevuePageUrlPattern = new RegExp('/evaluation-prevue(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const evaluationPrevueSample = {"intitule":"près","libelleImpression":"rapide","coefficient":12109.93,"compteDansMoyenne":false,"noteMaximale":16266.64};

  let evaluationPrevue;
  // let cycle;
  // let enseignant;
  // let matiere;
  // let sousMatiere;
  // let cours;
  // let typeTache;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cycles',
      body: {"annee":2186,"libelle":"commencer hirsute corps enseignant","dateDebut":"2026-08-20","dateFin":"2026-08-20","cloture":true,"commentaire":"triathlète à défaut de"},
    }).then(({ body }) => {
      cycle = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/enseignants',
      body: {"nom":"de façon à ce que patientèle oups","prenom":"antagoniste commis","libelleLong":"super","libelleCourt":"pendant que","email":"Anstrudie_Dumas83@yahoo.fr","telephone":"+33 429368293","keycloakUserId":"plutôt fonctionnaire après que","commentaire":"au dépens de adversaire","actif":false},
    }).then(({ body }) => {
      enseignant = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/matieres',
      body: {"intitule":"entre-temps","libelleLong":"incognito","libelleCourt":"malade","commentaire":"à partir de coac coac","actif":true},
    }).then(({ body }) => {
      matiere = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/sous-matieres',
      body: {"intitule":"triathlète sans que voler","libelleLong":"pendre mairie","libelleCourt":"hypocrite","commentaire":"plouf tendre","actif":false},
    }).then(({ body }) => {
      sousMatiere = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cours',
      body: {"intitule":"novice parce que placide","libelleLong":"ouin","libelleCourt":"sitôt que aujourd'hui si","ordreAffichage":16291,"nbPeriodes":20077,"coefficient":9492.13,"dateDebut":"2026-08-20","dateFin":"2026-08-20","commentaire":"hôte rédaction boulanger","actif":true},
    }).then(({ body }) => {
      cours = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/type-taches',
      body: {"code":"propre très gratis","intitule":"parmi ronron d'entre","libelleLong":"dans la mesure où","libelleCourt":"électorat","entreDansMoyenne":false,"commentaire":"après","actif":true},
    }).then(({ body }) => {
      typeTache = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/evaluation-prevues+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/evaluation-prevues').as('postEntityRequest');
    cy.intercept('DELETE', '/api/evaluation-prevues/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cycles', {
      statusCode: 200,
      body: [cycle],
    });

    cy.intercept('GET', '/api/enseignants', {
      statusCode: 200,
      body: [enseignant],
    });

    cy.intercept('GET', '/api/matieres', {
      statusCode: 200,
      body: [matiere],
    });

    cy.intercept('GET', '/api/sous-matieres', {
      statusCode: 200,
      body: [sousMatiere],
    });

    cy.intercept('GET', '/api/cours', {
      statusCode: 200,
      body: [cours],
    });

    cy.intercept('GET', '/api/type-taches', {
      statusCode: 200,
      body: [typeTache],
    });

    cy.intercept('GET', '/api/evaluation-realisees', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (evaluationPrevue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/evaluation-prevues/${evaluationPrevue.id}`,
      }).then(() => {
        evaluationPrevue = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (cycle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cycles/${cycle.id}`,
      }).then(() => {
        cycle = undefined;
      });
    }
    if (enseignant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/enseignants/${enseignant.id}`,
      }).then(() => {
        enseignant = undefined;
      });
    }
    if (matiere) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/matieres/${matiere.id}`,
      }).then(() => {
        matiere = undefined;
      });
    }
    if (sousMatiere) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sous-matieres/${sousMatiere.id}`,
      }).then(() => {
        sousMatiere = undefined;
      });
    }
    if (cours) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cours/${cours.id}`,
      }).then(() => {
        cours = undefined;
      });
    }
    if (typeTache) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/type-taches/${typeTache.id}`,
      }).then(() => {
        typeTache = undefined;
      });
    }
  });
   */

  it('EvaluationPrevues menu should load EvaluationPrevues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('evaluation-prevue');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EvaluationPrevue').should('exist');
    cy.url().should('match', evaluationPrevuePageUrlPattern);
  });

  describe('EvaluationPrevue page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(evaluationPrevuePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EvaluationPrevue page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/evaluation-prevue/new$'));
        cy.getEntityCreateUpdateHeading('EvaluationPrevue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationPrevuePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/evaluation-prevues',
          body: {
            ...evaluationPrevueSample,
            cycle: cycle,
            enseignant: enseignant,
            matiere: matiere,
            sousMatiere: sousMatiere,
            cours: cours,
            typeTache: typeTache,
          },
        }).then(({ body }) => {
          evaluationPrevue = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/evaluation-prevues+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/evaluation-prevues?page=0&size=20>; rel="last",<http://localhost/api/evaluation-prevues?page=0&size=20>; rel="first"',
              },
              body: [evaluationPrevue],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(evaluationPrevuePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(evaluationPrevuePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details EvaluationPrevue page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('evaluationPrevue');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationPrevuePageUrlPattern);
      });

      it('edit button click should load edit EvaluationPrevue page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EvaluationPrevue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationPrevuePageUrlPattern);
      });

      it('edit button click should load edit EvaluationPrevue page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EvaluationPrevue');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationPrevuePageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of EvaluationPrevue', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('evaluationPrevue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', evaluationPrevuePageUrlPattern);

        evaluationPrevue = undefined;
      });
    });
  });

  describe('new EvaluationPrevue page', () => {
    beforeEach(() => {
      cy.visit(`${evaluationPrevuePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EvaluationPrevue');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of EvaluationPrevue', () => {
      cy.get(`[data-cy="intitule"]`).type('patientèle');
      cy.get(`[data-cy="intitule"]`).should('have.value', 'patientèle');

      cy.get(`[data-cy="libelleImpression"]`).type('en outre de antique');
      cy.get(`[data-cy="libelleImpression"]`).should('have.value', 'en outre de antique');

      cy.get(`[data-cy="coefficient"]`).type('9207.77');
      cy.get(`[data-cy="coefficient"]`).should('have.value', '9207.77');

      cy.get(`[data-cy="compteDansMoyenne"]`).should('not.be.checked');
      cy.get(`[data-cy="compteDansMoyenne"]`).click();
      cy.get(`[data-cy="compteDansMoyenne"]`).should('be.checked');

      cy.get(`[data-cy="noteMaximale"]`).type('2503.81');
      cy.get(`[data-cy="noteMaximale"]`).should('have.value', '2503.81');

      cy.get(`[data-cy="dateDebut"]`).type('2026-08-20');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="dateFin"]`).type('2026-08-20');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-08-20');

      cy.get(`[data-cy="commentaire"]`).type('de peur que');
      cy.get(`[data-cy="commentaire"]`).should('have.value', 'de peur que');

      cy.get(`[data-cy="cycle"]`).select(1);
      cy.get(`[data-cy="enseignant"]`).select(1);
      cy.get(`[data-cy="matiere"]`).select(1);
      cy.get(`[data-cy="sousMatiere"]`).select(1);
      cy.get(`[data-cy="cours"]`).select(1);
      cy.get(`[data-cy="typeTache"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        evaluationPrevue = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', evaluationPrevuePageUrlPattern);
    });
  });
});
