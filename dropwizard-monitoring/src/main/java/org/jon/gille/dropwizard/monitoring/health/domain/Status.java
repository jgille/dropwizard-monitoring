package org.jon.gille.dropwizard.monitoring.health.domain;

public enum Status {

    HEALTHY(0), WARNING(1), CRITICAL(2);

    private final int severity;

    Status(int severity) {
        this.severity = severity;
    }

    public int severity() {
        return severity;
    }
}
