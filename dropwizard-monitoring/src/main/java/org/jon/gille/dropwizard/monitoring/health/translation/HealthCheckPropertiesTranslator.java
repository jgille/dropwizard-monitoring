package org.jon.gille.dropwizard.monitoring.health.translation;

import org.jon.gille.dropwizard.monitoring.health.annotation.HealthCheckProperties;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class HealthCheckPropertiesTranslator {

    public static HealthCheckSettings settingsFromAnnotation(HealthCheckProperties properties) {
        return HealthCheckSettings
                .withLevel(properties.level())
                .withType(properties.type())
                .withDescription(trimToNull(properties.description()))
                .withDependentOn(trimToNull(properties.dependentOn()))
                .withLink(trimToNull(properties.link()))
                .build();
    }

}
