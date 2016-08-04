package org.jon.gille.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;

public interface HealthCheckDecorator {

    HealthCheck decorate(HealthCheck healthCheck);
}
