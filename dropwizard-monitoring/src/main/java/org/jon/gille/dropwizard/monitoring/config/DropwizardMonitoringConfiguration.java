package org.jon.gille.dropwizard.monitoring.config;

public interface DropwizardMonitoringConfiguration {

    default HealthConfiguration getHealth() {
        return new HealthConfiguration();
    }
}
