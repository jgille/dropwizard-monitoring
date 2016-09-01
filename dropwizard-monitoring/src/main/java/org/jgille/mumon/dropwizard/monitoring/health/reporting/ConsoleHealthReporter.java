package org.jgille.mumon.dropwizard.monitoring.health.reporting;

import org.jgille.mumon.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.ServiceMetadata;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckResult;

import java.io.PrintStream;
import java.util.List;

class ConsoleHealthReporter implements ServiceHealthReporter {

    private final PrintStream out;

    ConsoleHealthReporter() {
        this(System.out);
    }

    ConsoleHealthReporter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void report(ServiceMetadata serviceMetadata, ServiceHealth serviceHealth) {

        List<HealthCheckResult> healthyChecks = serviceHealth.healthyChecks();
        List<HealthCheckResult> unhealthyChecks = serviceHealth.unhealthyChecks();

        out.println();
        out.printf("--- %s Service Health ---", serviceMetadata.serviceName().name());
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
