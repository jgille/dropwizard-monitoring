package org.jon.gille.dropwizard.monitoring.sample.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.jon.gille.dropwizard.monitoring.config.DropwizardMonitoringConfiguration;
import org.jon.gille.dropwizard.monitoring.config.HealthConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SampleConfiguration extends Configuration implements DropwizardMonitoringConfiguration {

    @Valid
    @NotNull
    private HealthConfiguration health = new HealthConfiguration();

    @Override
    @JsonProperty
    public HealthConfiguration getHealth() {
        return health;
    }

    @JsonProperty
    public void setHealth(HealthConfiguration health) {
        this.health = health;
    }
}
