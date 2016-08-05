package org.jon.gille.dropwizard.monitoring.health.translation.api;

import org.jon.gille.dropwizard.monitoring.api.health.HealthCheckResultDto;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;

import java.util.Optional;

public final class HealthCheckResultTranslator {

    private HealthCheckResultTranslator() {
    }

    public static HealthCheckResultDto mapToDto(HealthCheckResult healthCheckResult) {
        return new HealthCheckResultDto(
                healthCheckResult.name(),
                healthCheckResult.status().name(),
                healthCheckResult.description().orElse(null),
                healthCheckResult.message().orElse(null),
                mapError(healthCheckResult.error()),
                healthCheckResult.type().orElse(null),
                healthCheckResult.dependentOn().orElse(null),
                healthCheckResult.link().orElse(null));
    }

    private static String mapError(Optional<Throwable> error) {
        return error.map(HealthCheckResultTranslator::mapError).orElse(null);
    }

    private static String mapError(Throwable throwable) {
        return String.format("%s: %s", throwable.getClass().getName(), throwable.getMessage());
    }

}
