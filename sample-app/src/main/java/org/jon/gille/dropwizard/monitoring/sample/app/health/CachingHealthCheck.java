package org.jon.gille.dropwizard.monitoring.sample.app.health;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.annotation.Cached;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Cached(ttl = 3, unit = TimeUnit.SECONDS)
public class CachingHealthCheck extends HealthCheck {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    protected Result check() throws Exception {
        return Result.healthy(counter.incrementAndGet() + "");
    }
}
