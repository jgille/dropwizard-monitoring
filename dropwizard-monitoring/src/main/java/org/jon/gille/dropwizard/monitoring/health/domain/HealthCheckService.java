package org.jon.gille.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;

import java.util.concurrent.ExecutorService;

public interface HealthCheckService {

    void registerHealthCheck(String name, HealthCheck healthCheck);

    void registerHealthCheck(String name, HealthCheck healthCheck, HealthCheckSettings settings);

    void configureHealthCheck(String name, HealthCheckSettings settings);

    HealthCheckResult runHealthCheck(String name);

    ServiceHealth runHealthChecks();

    ServiceHealth runHealthChecksConcurrently(ExecutorService executor);
}
