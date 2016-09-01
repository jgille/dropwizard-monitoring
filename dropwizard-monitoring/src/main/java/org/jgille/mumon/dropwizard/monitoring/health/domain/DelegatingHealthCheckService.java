package org.jgille.mumon.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.jgille.mumon.dropwizard.monitoring.health.ChainedHealthCheckDecorator;
import org.jgille.mumon.dropwizard.monitoring.health.reflection.OptionallyCacheHealthCheckResult;
import org.jgille.mumon.dropwizard.monitoring.health.reflection.ReflectiveHealthCheckSettingsExtractor;
import org.jgille.mumon.dropwizard.monitoring.health.HealthCheckSettingsExtractor;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static java.util.stream.Collectors.toList;

public class DelegatingHealthCheckService implements HealthCheckService {

    private final HealthCheckRegistry healthCheckRegistry;
    private final HealthCheckSettingsRegistry settingsRegistry;
    private final HealthCheckSettingsExtractor settingsExtractor;
    private final HealthCheckDecorator healthCheckDecorator;

    private static final HealthCheckResultComparator HEALTH_CHECK_RESULT_COMPARATOR = new HealthCheckResultComparator();

    public DelegatingHealthCheckService(HealthCheckRegistry healthCheckRegistry) {
        this(healthCheckRegistry, new HealthCheckSettingsRegistry(), new ReflectiveHealthCheckSettingsExtractor(),
                ChainedHealthCheckDecorator.builder()
                        .add(new OptionallyCacheHealthCheckResult())
                        .build());
    }

    public DelegatingHealthCheckService(HealthCheckRegistry healthCheckRegistry,
                                        HealthCheckSettingsRegistry settingsRegistry,
                                        HealthCheckSettingsExtractor settingsExtractor,
                                        HealthCheckDecorator healthCheckDecorator) {
        this.healthCheckRegistry = healthCheckRegistry;
        this.settingsRegistry = settingsRegistry;
        this.settingsExtractor = settingsExtractor;
        this.healthCheckDecorator = healthCheckDecorator;
    }

    @Override
    public void registerHealthCheck(String name, HealthCheck healthCheck) {
        registerHealthCheckAndSettings(name, healthCheck, settingsExtractor.extractSettings(healthCheck));
    }

    @Override
    public void registerHealthCheck(String name, HealthCheck healthCheck, HealthCheckSettings override) {
        HealthCheckSettings annotatedSettings = settingsExtractor.extractSettings(healthCheck);
        HealthCheckSettings mergedSettings = annotatedSettings.overridenWith(override);
        registerHealthCheckAndSettings(name, healthCheck, mergedSettings);
    }

    @Override
    public void configureHealthCheck(String name, HealthCheckSettings settings) {
        settingsRegistry.register(name, settings);
    }

    @Override
    public HealthCheckResult runHealthCheck(String name) {
        HealthCheck.Result result = healthCheckRegistry.runHealthCheck(name);
        return mapToHealthCheckResult(name, result);
    }

    @Override
    public ServiceHealth runHealthChecks() {
        SortedMap<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        return mapToServiceHealth(results);
    }

    @Override
    public ServiceHealth runHealthChecksConcurrently(ExecutorService executor) {
        SortedMap<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks(executor);
        return mapToServiceHealth(results);
    }

    private void registerHealthCheckAndSettings(String name, HealthCheck healthCheck, HealthCheckSettings settings) {
        healthCheckRegistry.register(name, decorate(healthCheck));
        settingsRegistry.register(name, settings);
    }

    private HealthCheck decorate(HealthCheck healthCheck) {
        return healthCheckDecorator.decorate(healthCheck);
    }

    private HealthCheckResult mapToHealthCheckResult(String name, HealthCheck.Result result) {
        HealthCheckSettings settings = settingsRegistry.settings(name, HealthCheckSettings.DEFAULT_SETTINGS);
        return new HealthCheckResult(name, settings, result);
    }

    private ServiceHealth mapToServiceHealth(SortedMap<String, HealthCheck.Result> results) {
        List<HealthCheckResult> executedChecks = results.entrySet().stream()
                .map(this::mapToHealthCheckResult)
                .sorted(HEALTH_CHECK_RESULT_COMPARATOR)
                .collect(toList());
        Status status = aggregateHealth(executedChecks);
        return new ServiceHealth(UUID.randomUUID(), status, executedChecks, Instant.now());
    }

    private Status aggregateHealth(List<HealthCheckResult> executedChecks) {
        // TODO: Make it possible to customize this
        return executedChecks.stream().findFirst().map(HealthCheckResult::status).orElse(Status.HEALTHY);
    }

    private HealthCheckResult mapToHealthCheckResult(Map.Entry<String, HealthCheck.Result> entry) {
        return mapToHealthCheckResult(entry.getKey(), entry.getValue());
    }

}
