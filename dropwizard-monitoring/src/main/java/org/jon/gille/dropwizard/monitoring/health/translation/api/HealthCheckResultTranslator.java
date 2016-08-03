package org.jon.gille.dropwizard.monitoring.health.translation.api;

import org.jon.gille.dropwizard.monitoring.api.health.HealthCheckResultDto;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;

public final class HealthCheckResultTranslator {

    private HealthCheckResultTranslator() {
    }

    public static HealthCheckResultDto mapToDto(HealthCheckResult healthCheckResult) {
        return new HealthCheckResultDto(
                healthCheckResult.name(),
                healthCheckResult.status().name(),
                healthCheckResult.description().orElse(null),
                healthCheckResult.message().orElse(null),
                healthCheckResult.type().name(),
                healthCheckResult.dependentOn().orElse(null),
                healthCheckResult.link().orElse(null));
    }

}
