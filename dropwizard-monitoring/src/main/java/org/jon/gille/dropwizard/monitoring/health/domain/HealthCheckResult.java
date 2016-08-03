package org.jon.gille.dropwizard.monitoring.health.domain;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.ValueObject;

import java.util.Optional;

public class HealthCheckResult extends ValueObject {

    private final String name;
    private final HealthCheckSettings settings;
    private final HealthCheck.Result result;

    public HealthCheckResult(String name, HealthCheckSettings settings, HealthCheck.Result result) {
        this.name = name;
        this.settings = settings;
        this.result = result;
    }

    public String name() {
        return name;
    }

    public Status status() {
        return settings.level().mapToStatus(isHealthy());
    }

    public Type type() {
        return settings.type();
    }

    public Optional<String> description() {
        return settings.description();
    }

    public Optional<String> message() {
        return Optional.ofNullable(result.getMessage());
    }

    public Optional<Throwable> error() {
        return Optional.ofNullable(result.getError());
    }

    public Optional<String> dependentOn() {
        return settings.dependentOn();
    }

    public Optional<String> link() {
        return settings.link();
    }

    public boolean isHealthy() {
        return result.isHealthy();
    }

    public boolean isUnhealthy() {
        return !isHealthy();
    }
}
