package org.jgille.mumon.dropwizard.monitoring.health.domain;

import java.util.Comparator;

public class HealthCheckResultComparator implements Comparator<HealthCheckResult> {
    @Override
    public int compare(HealthCheckResult r1, HealthCheckResult r2) {
        if (r1.status() == r2.status()) {
            return r1.name().compareTo(r2.name());
        }
        return compareByStatus(r1, r2);
    }

    private int compareByStatus(HealthCheckResult r1, HealthCheckResult r2) {
        return r2.status().severity() - r1.status().severity();
    }
}
