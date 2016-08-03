package org.jon.gille.dropwizard.monitoring.health.reporting;

import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckService;
import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledServiceHealthReporter {

    private final ServiceMetadata serviceMetadata;
    private final HealthCheckService healthCheckService;
    private final ServiceHealthReporter reporter;

    private final Logger logger;

    public ScheduledServiceHealthReporter(ServiceMetadata serviceMetadata,
                                          HealthCheckService healthCheckService,
                                          ScheduledExecutorService scheduler,
                                          Duration frequency,
                                          ServiceHealthReporter reporter) {
        this.logger = LoggerFactory.getLogger(String.format("%s*scheduled", reporter.getClass().getName()));
        this.serviceMetadata = serviceMetadata;
        this.healthCheckService = healthCheckService;
        this.reporter = reporter;
        long delay = frequency.toMillis();
        scheduler.scheduleAtFixedRate(this::reportSafely, delay, delay, TimeUnit.MILLISECONDS);
    }

    private void reportSafely() {
        try {
            List<HealthCheckResult> executedChecks = healthCheckService.runHealthChecks();
            ServiceHealth serviceHealth = new ServiceHealth(serviceMetadata, executedChecks, Instant.now());
            reporter.report(serviceHealth);
        } catch (Exception e) {
            logger.warn("Failed to report service health", e);
        }
    }

}
