package org.jgille.mumon.dropwizard.monitoring.config;

public interface MuMonConfiguration {

    default HealthConfiguration getHealth() {
        return new HealthConfiguration();
    }

}
