package org.jgille.mumon.dropwizard.monitoring.config;

public interface DropwizardMonitoringConfiguration {

    default HealthConfiguration getHealth() {
        return new HealthConfiguration();
    }
}
