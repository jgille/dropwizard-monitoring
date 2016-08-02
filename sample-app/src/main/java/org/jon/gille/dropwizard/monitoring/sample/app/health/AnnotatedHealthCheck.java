package org.jon.gille.dropwizard.monitoring.sample.app.health;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.annotation.HealthCheckProperties;
import org.jon.gille.dropwizard.monitoring.health.domain.Level;
import org.jon.gille.dropwizard.monitoring.health.domain.Type;

@HealthCheckProperties(
        level = Level.WARNING,
        type = Type.EXTERNAL_DEPENDENCY,
        dependentOn = "google"
)
public class AnnotatedHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy("Search succeeded");
    }
}
