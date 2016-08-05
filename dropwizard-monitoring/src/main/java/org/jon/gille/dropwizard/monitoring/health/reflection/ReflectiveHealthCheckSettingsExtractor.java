package org.jon.gille.dropwizard.monitoring.health.reflection;

import com.codahale.metrics.health.HealthCheck;
import org.jon.gille.dropwizard.monitoring.health.HealthCheckSettingsExtractor;
import org.jon.gille.dropwizard.monitoring.health.annotation.Settings;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jon.gille.dropwizard.monitoring.health.translation.HealthCheckSettingsTranslator;

import java.util.Optional;

public class ReflectiveHealthCheckSettingsExtractor implements HealthCheckSettingsExtractor {

    @Override
    public HealthCheckSettings extractSettings(HealthCheck healthCheck) {
        Optional<Settings> settings = getAnnotationSettings(healthCheck);
        return settings.map(HealthCheckSettingsTranslator::settingsFromAnnotation)
                .orElse(HealthCheckSettings.DEFAULT_SETTINGS);
    }

    private static Optional<Settings> getAnnotationSettings(HealthCheck healthCheck) {
        Class<? extends HealthCheck> cls = healthCheck.getClass();
        Settings annotation = cls.getAnnotation(Settings.class);
        return Optional.ofNullable(annotation);
    }
}
