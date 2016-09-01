package org.jgille.mumon.dropwizard.monitoring.health.reporting;

import org.jgille.mumon.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.ServiceMetadata;

public interface ServiceHealthReporter {

    void report(ServiceMetadata serviceMetadata, ServiceHealth serviceHealth);
}
