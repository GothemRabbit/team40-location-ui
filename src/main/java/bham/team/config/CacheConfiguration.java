package bham.team.config;

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
            createCache(cm, bham.team.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, bham.team.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, bham.team.domain.User.class.getName());
            createCache(cm, bham.team.domain.Authority.class.getName());
            createCache(cm, bham.team.domain.User.class.getName() + ".authorities");
            createCache(cm, bham.team.domain.UserDetails.class.getName());
            createCache(cm, bham.team.domain.Authentication.class.getName());
            createCache(cm, bham.team.domain.Item.class.getName());
            createCache(cm, bham.team.domain.Message.class.getName());
            createCache(cm, bham.team.domain.Conversation.class.getName());
            createCache(cm, bham.team.domain.ProductStatus.class.getName());
            createCache(cm, bham.team.domain.UserInteraction.class.getName());
            createCache(cm, bham.team.domain.Reservation.class.getName());
            createCache(cm, bham.team.domain.Review.class.getName());
            createCache(cm, bham.team.domain.Notification.class.getName());
            createCache(cm, bham.team.domain.UserSearchHistory.class.getName());
            createCache(cm, bham.team.domain.UserRecommendation.class.getName());
            createCache(cm, bham.team.domain.UserDetails.class.getName() + ".itemsOnSales");
            createCache(cm, bham.team.domain.UserDetails.class.getName() + ".wishlists");
            createCache(cm, bham.team.domain.UserDetails.class.getName() + ".meetupLocations");
            createCache(cm, bham.team.domain.UserDetails.class.getName() + ".buyersReviews");
            createCache(cm, bham.team.domain.UserDetails.class.getName() + ".reviewsOfSellers");
            createCache(cm, bham.team.domain.UserDetails.class.getName() + ".chats");
            createCache(cm, bham.team.domain.Item.class.getName() + ".images");
            createCache(cm, bham.team.domain.Item.class.getName() + ".wishlists");
            createCache(cm, bham.team.domain.Conversation.class.getName() + ".participants");
            createCache(cm, bham.team.domain.Conversation.class.getName() + ".messages");
            createCache(cm, bham.team.domain.Location.class.getName());
            createCache(cm, bham.team.domain.Location.class.getName() + ".users");
            createCache(cm, bham.team.domain.Likes.class.getName());
            createCache(cm, bham.team.domain.Images.class.getName());
            createCache(cm, bham.team.domain.Wishlist.class.getName());
            createCache(cm, bham.team.domain.Wishlist.class.getName() + ".items");
            createCache(cm, bham.team.domain.Item.class.getName() + ".likes");
            createCache(cm, bham.team.domain.Conversation.class.getName() + ".profileDetails");
            createCache(cm, bham.team.domain.Location.class.getName() + ".productStatuses");
            createCache(cm, bham.team.domain.Location.class.getName() + ".profileDetails");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName());
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".items");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".wishlists");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".locations");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".likes");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".reviews");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".messages");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".productStatuses");
            createCache(cm, bham.team.domain.ProfileDetails.class.getName() + ".conversations");
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
