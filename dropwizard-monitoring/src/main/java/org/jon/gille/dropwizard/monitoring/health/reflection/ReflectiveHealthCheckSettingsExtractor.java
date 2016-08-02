package org.jon.gille.dropwizard.monitoring.health.reflection;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.HealthCheckSettingsExtractor;
import org.jon.gille.dropwizard.monitoring.health.annotation.HealthCheckProperties;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jon.gille.dropwizard.monitoring.health.translation.HealthCheckPropertiesTranslator;

import java.util.Optional;

public class ReflectiveHealthCheckSettingsExtractor implements HealthCheckSettingsExtractor {

    @Override
    public HealthCheckSettings extractSettings(HealthCheck healthCheck) {
        Optional<HealthCheckProperties> properties = getAnnotationProperties(healthCheck);
        return properties.map(HealthCheckPropertiesTranslator::settingsFromAnnotation)
                .orElse(HealthCheckSettings.DEFAULT_SETTINGS);
    }

    private static Optional<HealthCheckProperties> getAnnotationProperties(HealthCheck healthCheck) {
        Class<? extends HealthCheck> cls = healthCheck.getClass();
        HealthCheckProperties annotation = cls.getAnnotation(HealthCheckProperties.class);
        return Optional.ofNullable(annotation);
    }
}
