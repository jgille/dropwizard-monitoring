package org.jgille.mumon.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;

public interface HealthCheckDecorator {

    HealthCheck decorate(HealthCheck healthCheck);
}
