package org.jon.gille.dropwizard.monitoring.health.reporting;

import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;

public interface ServiceHealthReporter {

    void report(ServiceMetadata serviceMetadata, ServiceHealth serviceHealth);
}
