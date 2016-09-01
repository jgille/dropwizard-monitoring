package org.jgille.mumon.dropwizard.monitoring.health;

import com.codahale.metrics.health.HealthCheck;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckSettings;

public interface HealthCheckSettingsExtractor {
    HealthCheckSettings extractSettings(HealthCheck healthCheck);
}
