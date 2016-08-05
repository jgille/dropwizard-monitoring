package org.jon.gille.dropwizard.monitoring.sample.app.health;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.annotation.Settings;
import org.jon.gille.dropwizard.monitoring.health.domain.Level;

@Settings(
        level = Level.WARNING,
        type = "EXTERNAL_DEPENDENCY",
        dependentOn = "google"
)
public class AnnotatedHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return Result.healthy("Search succeeded");
    }
}
