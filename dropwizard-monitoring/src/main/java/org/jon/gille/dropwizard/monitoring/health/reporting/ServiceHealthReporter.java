package org.jon.gille.dropwizard.monitoring.health.reporting;

import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;

public interface ServiceHealthReporter {

    void report(ServiceHealth serviceHealth);
}
