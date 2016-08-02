package org.jon.gille.dropwizard.monitoring.health;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;

/**
 * Created by jon on 8/2/16.
 */
public interface HealthCheckSettingsExtractor {
    HealthCheckSettings extractSettings(HealthCheck healthCheck);
}
