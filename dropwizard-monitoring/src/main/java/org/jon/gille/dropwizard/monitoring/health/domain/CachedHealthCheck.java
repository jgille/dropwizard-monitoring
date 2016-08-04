package org.jon.gille.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CachedHealthCheck extends HealthCheck {
    private final HealthCheck delegate;
    private final LoadingCache<HealthCheck, Result> cache;

    public CachedHealthCheck(HealthCheck delegate, Duration ttl) {
        this.delegate = delegate;
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(ttl.toMillis(), TimeUnit.MILLISECONDS)
                .build(new CacheLoader<HealthCheck, Result>() {
                    @Override
                    public Result load(@Nonnull HealthCheck healthCheck) throws Exception {
                        return healthCheck.execute();
                    }
                });
    }

    @Override
    protected Result check() throws Exception {
        return cache.get(delegate);
    }
}
