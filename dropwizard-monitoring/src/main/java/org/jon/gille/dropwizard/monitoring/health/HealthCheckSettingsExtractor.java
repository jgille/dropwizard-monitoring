package org.jon.gille.dropwizard.monitoring.health;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;

public interface HealthCheckSettingsExtractor {
    HealthCheckSettings extractSettings(HealthCheck healthCheck);
}
