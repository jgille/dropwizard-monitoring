package org.jon.gille.dropwizard.monitoring.health.domain;

import org.jon.gille.dropwizard.monitoring.ValueObject;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceHealth extends ValueObject {

    private final ServiceMetadata metadata;
    private final List<HealthCheckResult> executedChecks;
    private final Instant timestamp;

    public ServiceHealth(ServiceMetadata metadata, List<HealthCheckResult> executedChecks, Instant timestamp) {
        this.metadata = metadata;
        this.executedChecks = new ArrayList<>(executedChecks);
        this.timestamp = timestamp;
    }

    public ServiceMetadata serviceMetadata() {
        return metadata;
    }

    public List<HealthCheckResult> executedChecks() {
        return Collections.unmodifiableList(executedChecks);
    }

    public Instant timestamp() {
        return timestamp;
    }
}
