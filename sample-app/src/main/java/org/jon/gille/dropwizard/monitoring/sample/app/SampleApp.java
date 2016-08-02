package org.jon.gille.dropwizard.monitoring.sample.app;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jon.gille.dropwizard.monitoring.bundle.DropwizardMonitoringBundle;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jon.gille.dropwizard.monitoring.health.domain.Level;
import org.jon.gille.dropwizard.monitoring.health.domain.Type;
import org.jon.gille.dropwizard.monitoring.sample.app.health.AnnotatedHealthCheck;
import org.jon.gille.dropwizard.monitoring.sample.app.health.UnhealthyHealthCheck;

public class SampleApp extends Application<Configuration> {

    private final DropwizardMonitoringBundle<Configuration> monitoringBundle =
            new DropwizardMonitoringBundle<>();

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        bootstrap.addBundle(monitoringBundle);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        monitoringBundle.registerHealthCheck("annotated", new AnnotatedHealthCheck());
        monitoringBundle.registerHealthCheck("broken", new UnhealthyHealthCheck(),
                HealthCheckSettings.withLevel(Level.CRITICAL)
                        .withType(Type.SELF)
                        .withDescription("I will always fail")
                        .build());
    }

    public static void main(String[] args) throws Exception {
        new SampleApp().run(args);
    }
}
