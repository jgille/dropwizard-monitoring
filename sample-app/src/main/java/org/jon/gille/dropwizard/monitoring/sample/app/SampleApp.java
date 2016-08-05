package org.jon.gille.dropwizard.monitoring.sample.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jon.gille.dropwizard.monitoring.bundle.DropwizardMonitoringBundle;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jon.gille.dropwizard.monitoring.health.domain.Level;
import org.jon.gille.dropwizard.monitoring.sample.app.health.AnnotatedHealthCheck;
import org.jon.gille.dropwizard.monitoring.sample.app.health.CachingHealthCheck;
import org.jon.gille.dropwizard.monitoring.sample.app.health.UnhealthyHealthCheck;

public class SampleApp extends Application<SampleConfiguration> {

    private final DropwizardMonitoringBundle<SampleConfiguration> monitoringBundle =
            new DropwizardMonitoringBundle<>();

    @Override
    public void initialize(Bootstrap<SampleConfiguration> bootstrap) {
        bootstrap.addBundle(monitoringBundle);
    }

    @Override
    public void run(SampleConfiguration configuration, Environment environment) throws Exception {
        monitoringBundle.registerHealthCheck("annotated", new AnnotatedHealthCheck());
        monitoringBundle.registerHealthCheck("cached", new CachingHealthCheck());
        monitoringBundle.registerHealthCheck("broken", new UnhealthyHealthCheck(),
                HealthCheckSettings.withLevel(Level.CRITICAL)
                        .withType("SELF")
                        .withDescription("I will always fail")
                        .build());
    }

    @Override
    public String getName() {
        return "Sample App";
    }

    public static void main(String[] args) throws Exception {
        new SampleApp().run(args);
    }
}
