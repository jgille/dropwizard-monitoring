package org.jgille.mumon.dropwizard.monitoring.health.domain;

import org.jgille.mumon.dropwizard.monitoring.ValueObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class ServiceHealth extends ValueObject {

    private final UUID id;
    private final Status status;
    private final List<HealthCheckResult> executedChecks;
    private final Instant timestamp;

    public ServiceHealth(UUID id, Status status, List<HealthCheckResult> executedChecks, Instant timestamp) {
        this.id = id;
        this.status = status;
        this.executedChecks = new ArrayList<>(executedChecks);
        this.timestamp = timestamp;
    }

    public UUID id() {
        return id;
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

    public Status status() {
        return status;
    }

    public Instant timestamp() {
        return timestamp;
    }
}
