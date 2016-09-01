package org.jgille.mumon.dropwizard.monitoring.sample.app;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jgille.mumon.dropwizard.monitoring.bundle.MuMonBundle;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jgille.mumon.dropwizard.monitoring.health.domain.Level;
import org.jgille.mumon.dropwizard.monitoring.sample.app.health.AnnotatedHealthCheck;
import org.jgille.mumon.dropwizard.monitoring.sample.app.health.CachingHealthCheck;
import org.jgille.mumon.dropwizard.monitoring.sample.app.health.UnhealthyHealthCheck;

import java.util.Collections;

public class SampleApp extends Application<SampleConfiguration> {

    private final MuMonBundle<SampleConfiguration> monitoringBundle =
            new MuMonBundle.Builder<SampleConfiguration>()
                    .addServiceMetadataProvider(c -> Collections.singletonMap("key", "value"))
                    .addServiceMetadataProvider(c -> Collections.singletonMap("otherKey", "otherValue"))
                    .build();

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
