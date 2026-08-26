# Déploiement sur Dokploy

Stack : MySQL, Adminer, Keycloak, backend (Spring Boot), frontend (Angular/nginx).
Le frontend et le backend sont dans deux dépôts séparés ; ce fichier
`docker-compose.yml` (dans le dépôt backend) construit les deux via leur
URL Git — pas besoin de checkout local du frontend.

## 1. Domaines (Cloudflare)
w
Pointez ces quatre sous-domaines vers l'IP du serveur Dokploy (DNS A/AAAA,
proxy Cloudflare activé ou non selon votre préférence) :

| Sous-domaine | Service | Port interne |
|---|---|---|
| `ibgafrica.forbidec.org` | frontend | 80 |
| `api-ibgafrica.forbidec.org` | backend | 8080 |
| `keycloak-ibgafrica.forbidec.org` | keycloak | 8080 |
| `adminer-ibgafrica.forbidec.org` | adminer | 8080 |

## 2. Créer le projet dans Dokploy

1. Nouveau projet → **Docker Compose**.
2. Source : dépôt Git `https://github.com/lionnelpat/ibg-africa-backend.git`, branche `main`, fichier `docker-compose.yml`.
3. Onglet **Environment** : renseignez les 3 variables (générez vos propres valeurs, ou utilisez celles fournies séparément lors de la mise en place) :
   - `MYSQL_ROOT_PASSWORD`
   - `KEYCLOAK_ADMIN_PASSWORD`
   - `CLIENT_SECRET_WEB_APP` — doit être **identique** au champ `secret` du client `web_app` dans `src/main/docker/realm-config-prod/jhipster-realm.json`. Si vous changez l'un, changez l'autre et redéployez.
4. Déployez. Ordre de démarrage géré par les `depends_on` + healthchecks : mysql → keycloak → backend ; le frontend démarre dès que le backend existe (son nginx retente la résolution DNS de `backend` tout seul).

## 3. Attacher les domaines

Dans Dokploy, pour **chaque service** du compose (frontend, backend,
keycloak, adminer), onglet **Domains** → ajoutez le sous-domaine
correspondant au tableau ci-dessus, avec le port interne indiqué, et
activez le certificat Let's Encrypt. Dokploy gère lui-même le
reverse-proxy (Traefik) — ce fichier compose ne contient volontairement
aucun label Traefik.

> Si votre version de Dokploy ne supporte pas les `build.context` pointant
> vers une URL Git pour le service `frontend`, l'alternative est de créer
> le frontend comme projet **Application** séparé (dépôt
> `ibg-africa-frontend`, Dockerfile à la racine) plutôt que comme service
> de ce compose — dans ce cas retirez le service `frontend` d'ici.

## 4. Après le premier déploiement

- **Keycloak** : connectez-vous sur `https://keycloak-ibgafrica.forbidec.org/admin` avec `admin` / le mot de passe de `KEYCLOAK_ADMIN_PASSWORD`.
- **Compte admin applicatif** (`admin` dans le realm `jhipster`, utilisé pour se connecter à FORBIDEC) : le mot de passe importé est le mot de passe de démo JHipster (`admin`), mais le realm force un changement de mot de passe à la première connexion (`UPDATE_PASSWORD`). Connectez-vous une première fois sur `https://ibgafrica.forbidec.org` avec `admin` / `admin` pour définir le mot de passe définitif.
- Le compte `user` de démo est désactivé dans le realm de production.
- **Adminer** : serveur `mysql`, utilisateur `root`, mot de passe = `MYSQL_ROOT_PASSWORD`.

## 5. Pousser une mise à jour

```bash
cd backend && git push origin main
cd ../frontend && git push origin main
```

Puis relancez un déploiement du projet compose dans Dokploy (il reconstruit
les deux images depuis les dépôts Git).

## 6. Environnement de staging (branche `develop`)

Le même `docker-compose.yml` sert aux deux environnements. Tout ce qui doit
différer entre prod et staging est piloté par des variables d'environnement
(avec une valeur par défaut qui reproduit exactement le comportement de
prod), pour que ce fichier reste unique et que prod ne soit jamais affecté
par un changement fait pour staging.

**Pourquoi c'est nécessaire** : les deux stacks tournent sur le **même
serveur**. Sans ces variables, staging tenterait de publier les mêmes ports
hôte que prod (`9080`, `8080`, `8081`, `8082`) → `port is already allocated`
au démarrage de Keycloak (ou du service suivant). Et même une fois les
ports résolus, Keycloak et le backend annonceraient encore les URLs de
**production** (`KC_HOSTNAME`, issuer OIDC, redirect URIs du realm), donc la
connexion échouerait silencieusement sur le domaine de staging.

1. Nouveau projet Dokploy → **Docker Compose**, dépôt
   `ibg-africa-backend`, branche **`develop`**, fichier `docker-compose.yml`.
2. Onglet **Environment** : les 3 variables habituelles, avec des valeurs
   **différentes de la prod** (mots de passe et secret propres à staging —
   ne réutilisez pas ceux de prod), plus les variables ci-dessous :

   | Variable | Valeur recommandée | Rôle |
   |---|---|---|
   | `MYSQL_ROOT_PASSWORD` | (propre à staging) | — |
   | `KEYCLOAK_ADMIN_PASSWORD` | (propre à staging) | — |
   | `CLIENT_SECRET_WEB_APP` | voir `secret` du client `web_app` dans `src/main/docker/realm-config-staging/jhipster-realm.json` | doit correspondre au realm importé |
   | `REALM_CONFIG_DIR` | `realm-config-staging` | Keycloak importe le realm avec les redirect URIs `stg-*` au lieu de ceux de prod |
   | `KC_HOSTNAME_URL` | `https://stg-openid-ibgafrica.forbidec.org` | URL publique annoncée par Keycloak |
   | `OIDC_ISSUER_URI` | `https://stg-openid-ibgafrica.forbidec.org/realms/jhipster` | issuer OIDC utilisé par le backend Spring Security |
   | `KEYCLOAK_HOST_PORT` | `9081` | évite le conflit avec le `9080` de prod |
   | `BACKEND_HOST_PORT` | `8091` | évite le conflit avec le `8080` de prod |
   | `ADMINER_HOST_PORT` | `8092` | évite le conflit avec le `8081` de prod |
   | `FRONTEND_HOST_PORT` | `8093` | évite le conflit avec le `8082` de prod |

   (Les ports proposés sont arbitraires — n'importe quel port libre sur le
   serveur convient, du moment qu'il ne rentre pas en collision avec un port
   déjà utilisé par un autre projet Dokploy.)

3. Attachez les domaines `stg-*` (cf. la liste fournie) sur les services
   correspondants, comme à l'étape 3, en utilisant `stg-openid-ibgafrica.forbidec.org`
   pour le service `keycloak`.
4. Déployez. `MYSQL_DATABASE`, réseau et volumes sont propres à ce projet
   Dokploy (Dokploy isole chaque projet compose), donc aucune donnée n'est
   partagée avec la prod.

Pour une mise à jour de staging : `git push origin develop`, puis relancez
le déploiement du projet staging dans Dokploy.
