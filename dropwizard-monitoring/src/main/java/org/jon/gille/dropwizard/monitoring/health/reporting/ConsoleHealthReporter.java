package org.jon.gille.dropwizard.monitoring.health.reporting;

import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;

import java.io.PrintStream;
import java.util.List;

class ConsoleHealthReporter implements ServiceHealthReporter {

    private final PrintStream out;

    public ConsoleHealthReporter() {
        this(System.out);
    }

    ConsoleHealthReporter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void report(ServiceHealth serviceHealth) {
        List<HealthCheckResult> healthyChecks = serviceHealth.healthyChecks();
        List<HealthCheckResult> unhealthyChecks = serviceHealth.unhealthyChecks();

        out.printf("--- %s Service Health ---", serviceHealth.serviceMetadata().serviceName().name());
        out.println();
        out.printf("%d unhealthy checks, %d healthy checks", unhealthyChecks.size(), healthyChecks.size());
        out.println();

        if (!unhealthyChecks.isEmpty()) {
            out.println("------ Unhealthy checks ------");
            unhealthyChecks.forEach(c -> {
                out.printf("    * %s - %s", c.name(), c.status().name());
                out.println();
            });
        }
        out.println();

    }
}
