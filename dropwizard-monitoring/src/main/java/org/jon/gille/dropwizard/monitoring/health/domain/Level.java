package org.jon.gille.dropwizard.monitoring.health.domain;

public enum Level {

    WARNING(Status.WARNING), CRITICAL(Status.CRITICAL);

    private final Status unhealthyStatus;

    Level(Status unhealthyStatus) {
        this.unhealthyStatus = unhealthyStatus;
    }

    public Status mapToStatus(boolean isHealthy) {
        return isHealthy ? Status.HEALTHY : unhealthyStatus;
    }
}
