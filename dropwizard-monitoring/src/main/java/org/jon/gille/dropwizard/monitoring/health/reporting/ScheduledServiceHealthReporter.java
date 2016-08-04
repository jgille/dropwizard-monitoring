package org.jon.gille.dropwizard.monitoring.health.reporting;

import io.dropwizard.lifecycle.Managed;
import io.dropwizard.util.Duration;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckService;
import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledServiceHealthReporter implements Managed {

    private final ServiceMetadata serviceMetadata;
    private final HealthCheckService healthCheckService;
    private final ServiceHealthReporter reporter;

    private final Logger logger;
    private final ScheduledExecutorService scheduler;
    private final Duration frequency;

    public ScheduledServiceHealthReporter(ServiceMetadata serviceMetadata,
                                          HealthCheckService healthCheckService,
                                          ScheduledExecutorService scheduler,
                                          Duration frequency,
                                          ServiceHealthReporter reporter) {
        this.scheduler = scheduler;
        this.frequency = frequency;
        this.logger = LoggerFactory.getLogger(String.format("%s*scheduled", reporter.getClass().getName()));
        this.serviceMetadata = serviceMetadata;
        this.healthCheckService = healthCheckService;
        this.reporter = reporter;

    }

    private void reportSafely() {
        try {
            List<HealthCheckResult> executedChecks = healthCheckService.runHealthChecks();
            ServiceHealth serviceHealth =
                    new ServiceHealth(UUID.randomUUID(), serviceMetadata, executedChecks, Instant.now());
            reporter.report(serviceHealth);
        } catch (Exception e) {
            logger.warn("Failed to report service health", e);
        }
    }

    @Override
    public void start() throws Exception {
        long delay = frequency.toMilliseconds();
        scheduler.scheduleAtFixedRate(this::reportSafely, delay, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() throws Exception {

    }
}
