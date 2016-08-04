package org.jon.gille.dropwizard.monitoring.health.reporting;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.util.Duration;

import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface HealthReporterFactory extends Discoverable {

    default Optional<Duration> getFrequency() {
        return Optional.empty();
    }

    ServiceHealthReporter build();

    String getName();
}
