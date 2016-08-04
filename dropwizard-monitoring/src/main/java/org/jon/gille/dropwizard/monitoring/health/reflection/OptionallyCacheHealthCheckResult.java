package org.jon.gille.dropwizard.monitoring.health.reflection;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.annotation.Cached;
import org.jon.gille.dropwizard.monitoring.health.domain.CachedHealthCheck;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckDecorator;

import java.time.Duration;
import java.util.Optional;

public class OptionallyCacheHealthCheckResult implements HealthCheckDecorator {
    @Override
    public HealthCheck decorate(HealthCheck healthCheck) {
        Optional<Cached> cachingSettings = cachingSettings(healthCheck);
        if (cachingSettings.isPresent()) {
            Cached cached = cachingSettings.get();
            Duration ttl = Duration.ofMillis(cached.unit().toMillis(cached.ttl()));
            return new CachedHealthCheck(healthCheck, ttl);
        } else {
            return healthCheck;
        }
    }

    private static Optional<Cached> cachingSettings(HealthCheck healthCheck) {
        Class<? extends HealthCheck> cls = healthCheck.getClass();
        Cached annotation = cls.getAnnotation(Cached.class);
        return Optional.ofNullable(annotation);
    }
}
