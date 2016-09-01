package org.jgille.mumon.dropwizard.monitoring.health.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;
import io.dropwizard.validation.MinDuration;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public abstract class BaseHealthReporterFactory implements HealthReporterFactory {

    @NotNull
    private String name;

    @Valid
    @MinDuration(0)
    @UnwrapValidatedValue
    private Optional<Duration> frequency = Optional.empty();

    protected BaseHealthReporterFactory(String name) {
        this.name = name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public Optional<Duration> getFrequency() {
        return frequency;
    }

    @JsonProperty
    public void setFrequency(Optional<Duration> frequency) {
        this.frequency = frequency;
    }

}
