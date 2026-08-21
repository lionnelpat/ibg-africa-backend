package org.forbidec.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.forbidec.domain.Pays.class.getName());
            createCache(cm, org.forbidec.domain.Pays.class.getName() + ".centres");
            createCache(cm, org.forbidec.domain.Pays.class.getName() + ".etudiants");
            createCache(cm, org.forbidec.domain.CentreFormation.class.getName());
            createCache(cm, org.forbidec.domain.CentreFormation.class.getName() + ".baremes");
            createCache(cm, org.forbidec.domain.CentreFormation.class.getName() + ".parametres");
            createCache(cm, org.forbidec.domain.CentreFormation.class.getName() + ".cycles");
            createCache(cm, org.forbidec.domain.CentreFormation.class.getName() + ".habilitations");
            createCache(cm, org.forbidec.domain.Matiere.class.getName());
            createCache(cm, org.forbidec.domain.Matiere.class.getName() + ".evaluations");
            createCache(cm, org.forbidec.domain.SousMatiere.class.getName());
            createCache(cm, org.forbidec.domain.SousMatiere.class.getName() + ".evaluations");
            createCache(cm, org.forbidec.domain.Cours.class.getName());
            createCache(cm, org.forbidec.domain.Cours.class.getName() + ".evaluations");
            createCache(cm, org.forbidec.domain.TypeTache.class.getName());
            createCache(cm, org.forbidec.domain.TypeTache.class.getName() + ".evaluations");
            createCache(cm, org.forbidec.domain.BaremeMention.class.getName());
            createCache(cm, org.forbidec.domain.Enseignant.class.getName());
            createCache(cm, org.forbidec.domain.Enseignant.class.getName() + ".evaluations");
            createCache(cm, org.forbidec.domain.Etudiant.class.getName());
            createCache(cm, org.forbidec.domain.Etudiant.class.getName() + ".inscriptions");
            createCache(cm, org.forbidec.domain.Etudiant.class.getName() + ".evenements");
            createCache(cm, org.forbidec.domain.Etudiant.class.getName() + ".notes");
            createCache(cm, org.forbidec.domain.Cycle.class.getName());
            createCache(cm, org.forbidec.domain.Cycle.class.getName() + ".inscriptions");
            createCache(cm, org.forbidec.domain.Cycle.class.getName() + ".evaluations");
            createCache(cm, org.forbidec.domain.Cycle.class.getName() + ".habilitations");
            createCache(cm, org.forbidec.domain.InscriptionCycle.class.getName());
            createCache(cm, org.forbidec.domain.EvenementEtudiant.class.getName());
            createCache(cm, org.forbidec.domain.EvaluationPrevue.class.getName());
            createCache(cm, org.forbidec.domain.EvaluationPrevue.class.getName() + ".notes");
            createCache(cm, org.forbidec.domain.EvaluationRealisee.class.getName());
            createCache(cm, org.forbidec.domain.EvaluationRealisee.class.getName() + ".historiques");
            createCache(cm, org.forbidec.domain.HistoriqueNote.class.getName());
            createCache(cm, org.forbidec.domain.Parametre.class.getName());
            createCache(cm, org.forbidec.domain.HabilitationCycle.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
