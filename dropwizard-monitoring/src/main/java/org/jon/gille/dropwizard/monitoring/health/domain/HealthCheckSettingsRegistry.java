package org.jon.gille.dropwizard.monitoring.health.domain;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HealthCheckSettingsRegistry {

    private final ConcurrentHashMap<String, HealthCheckSettings> registeredSettings;

    public HealthCheckSettingsRegistry() {
        this.registeredSettings = new ConcurrentHashMap<>();
    }

    public Optional<HealthCheckSettings> settings(String healthCheckName) {
        HealthCheckSettings healthCheckSettings = registeredSettings.get(healthCheckName);
        return Optional.ofNullable(healthCheckSettings);
    }

    public HealthCheckSettings settings(String healthCheckName, HealthCheckSettings defaultSettings) {
        return registeredSettings.getOrDefault(healthCheckName, defaultSettings);
    }

    public void register(String healthCheckName, HealthCheckSettings healthCheckSettings) {
        registeredSettings.put(healthCheckName, healthCheckSettings);
    }
}
