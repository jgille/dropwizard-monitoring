package org.jgille.mumon.dropwizard.monitoring.health.translation;

import org.jgille.mumon.dropwizard.monitoring.health.annotation.Settings;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckSettings;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class HealthCheckSettingsTranslator {

    public static HealthCheckSettings settingsFromAnnotation(Settings settings) {
        return HealthCheckSettings
                .withLevel(settings.level())
                .withType(trimToNull(settings.type()))
                .withDescription(trimToNull(settings.description()))
                .withDependentOn(trimToNull(settings.dependentOn()))
                .withLink(trimToNull(settings.link()))
                .build();
    }

}
