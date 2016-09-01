package org.jgille.mumon.dropwizard.monitoring.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import io.dropwizard.util.Duration;
import org.jgille.mumon.dropwizard.monitoring.health.reporting.HealthReporterFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class HealthConfiguration {

    @Valid
    @NotNull
    private ImmutableList<HealthReporterFactory> reporters = ImmutableList.of();

    @NotNull
    private Duration defaultReportingFrequency = Duration.minutes(1);

    @JsonProperty
    public ImmutableList<HealthReporterFactory> getReporters() {
        return reporters;
    }

    @JsonProperty
    public void setReporters(ImmutableList<HealthReporterFactory> reporters) {
        this.reporters = reporters;
    }

    @JsonProperty
    public Duration getDefaultReportingFrequency() {
        return defaultReportingFrequency;
    }
}
