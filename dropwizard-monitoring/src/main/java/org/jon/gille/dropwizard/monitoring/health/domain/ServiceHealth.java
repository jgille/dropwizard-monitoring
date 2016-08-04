package org.jon.gille.dropwizard.monitoring.health.domain;

import org.jon.gille.dropwizard.monitoring.ValueObject;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class ServiceHealth extends ValueObject {

    private final UUID id;
    private final ServiceMetadata metadata;
    private final List<HealthCheckResult> executedChecks;
    private final Instant timestamp;

    public ServiceHealth(UUID id, ServiceMetadata metadata, List<HealthCheckResult> executedChecks, Instant timestamp) {
        this.id = id;
        this.metadata = metadata;
        this.executedChecks = new ArrayList<>(executedChecks);
        this.timestamp = timestamp;
    }

    public UUID id() {
        return id;
    }

    public ServiceMetadata serviceMetadata() {
        return metadata;
    }

    public List<HealthCheckResult> executedChecks() {
        return Collections.unmodifiableList(executedChecks);
    }

    public List<HealthCheckResult> healthyChecks() {
        return executedChecks(HealthCheckResult::isHealthy);
    }

    public List<HealthCheckResult> unhealthyChecks() {
        return executedChecks(HealthCheckResult::isUnhealthy);
    }

    public List<HealthCheckResult> executedChecks(Predicate<HealthCheckResult> filter) {
        return executedChecks.stream().filter(filter).collect(toList());
    }

    public Instant timestamp() {
        return timestamp;
    }
}
