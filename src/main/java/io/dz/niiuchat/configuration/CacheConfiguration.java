package io.dz.niiuchat.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfiguration {

    public static final String USERS_BY_GROUP = "USERS_BY_GROUP";

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        var usersByGroup = buildOneHourCache(USERS_BY_GROUP, ticker);

        var manager = new SimpleCacheManager();
        manager.setCaches(Collections.singletonList(usersByGroup));

        return manager;
    }

    private CaffeineCache buildOneHourCache(String name, Ticker ticker) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(10000)
                .ticker(ticker)
                .build());
    }

}
