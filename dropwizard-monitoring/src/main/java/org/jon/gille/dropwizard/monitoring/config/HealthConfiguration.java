package org.jon.gille.dropwizard.monitoring.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import io.dropwizard.util.Duration;
import org.jon.gille.dropwizard.monitoring.health.reporting.HealthReporterFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class HealthConfiguration {

    @Valid
    @NotNull
    private ImmutableList<HealthReporterFactory> reporters = ImmutableList.of();

    private Duration defaultReportingFrequency = Duration.minutes(1);

    @JsonProperty
    public ImmutableList<HealthReporterFactory> getReporters() {
        return reporters;
    }

    @JsonProperty
    public void setReporters(ImmutableList<HealthReporterFactory> reporters) {
        this.reporters = reporters;
    }

    public Duration getDefaultReportingFrequency() {
        return defaultReportingFrequency;
    }
}
