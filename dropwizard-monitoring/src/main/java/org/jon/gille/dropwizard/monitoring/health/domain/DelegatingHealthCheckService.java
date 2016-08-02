package org.jon.gille.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.jon.gille.dropwizard.monitoring.health.HealthCheckSettingsExtractor;
import org.jon.gille.dropwizard.monitoring.health.reflection.ReflectiveHealthCheckSettingsExtractor;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ExecutorService;

import static java.util.stream.Collectors.toList;

public class DelegatingHealthCheckService implements HealthCheckService {

    private final HealthCheckRegistry healthCheckRegistry;
    private final HealthCheckSettingsRegistry settingsRegistry;
    private final HealthCheckSettingsExtractor settingsExtractor;

    public DelegatingHealthCheckService(HealthCheckRegistry healthCheckRegistry) {
        this(healthCheckRegistry, new HealthCheckSettingsRegistry(), new ReflectiveHealthCheckSettingsExtractor());
    }

    public DelegatingHealthCheckService(HealthCheckRegistry healthCheckRegistry,
                                        HealthCheckSettingsRegistry settingsRegistry,
                                        HealthCheckSettingsExtractor settingsExtractor) {
        this.healthCheckRegistry = healthCheckRegistry;
        this.settingsRegistry = settingsRegistry;
        this.settingsExtractor = settingsExtractor;
    }

    @Override
    public void registerHealthCheck(String name, HealthCheck healthCheck) {
        registerHealthCheck(name, healthCheck, settingsExtractor.extractSettings(healthCheck));
    }

    @Override
    public void registerHealthCheck(String name, HealthCheck healthCheck, HealthCheckSettings settings) {
        healthCheckRegistry.register(name, healthCheck);
        settingsRegistry.register(name, settings);
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
    public List<HealthCheckResult> runHealthChecks() {
        SortedMap<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        return mapToHealthCheckResults(results);
    }

    @Override
    public List<HealthCheckResult> runHealthChecksConcurrently(ExecutorService executor) {
        SortedMap<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks(executor);
        return mapToHealthCheckResults(results);
    }

    private HealthCheckResult mapToHealthCheckResult(String name, HealthCheck.Result result) {
        HealthCheckSettings settings = settings(name);
        return new HealthCheckResult(name, settings, result);
    }

    private List<HealthCheckResult> mapToHealthCheckResults(SortedMap<String, HealthCheck.Result> results) {
        return results.entrySet().stream()
                .map(this::mapToHealthCheckResult)
                .collect(toList());
    }

    private HealthCheckResult mapToHealthCheckResult(Map.Entry<String, HealthCheck.Result> entry) {
        return mapToHealthCheckResult(entry.getKey(), entry.getValue());
    }

    private HealthCheckSettings settings(String name) {
        return settingsRegistry.settings(name, HealthCheckSettings.DEFAULT_SETTINGS);
    }

}
