package org.jgille.mumon.dropwizard.monitoring.health.reflection

import com.codahale.metrics.health.HealthCheck
import org.jgille.mumon.dropwizard.monitoring.health.annotation.Settings
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckSettings
import org.jgille.mumon.dropwizard.monitoring.health.domain.Level
import spock.lang.Specification

class ReflectiveHealthCheckSettingsExtractorSpec extends Specification {

    def "A health check without annotation gets default settings"() {
        given:
        def extractor = new ReflectiveHealthCheckSettingsExtractor()

        when:
        def settings = extractor.extractSettings(new NotAnnotatedHealthCheck())

        then:
        settings == HealthCheckSettings.DEFAULT_SETTINGS
    }

    def "A health check with annotation gets settings from the annotation properties"() {
        given:
        def extractor = new ReflectiveHealthCheckSettingsExtractor()

        when:
        def settings = extractor.extractSettings(new AnnotatedHealthCheck())

        then:
        settings.level() == Level.WARNING
        settings.type() == Optional.of("EXTERNAL_DEPENDENCY")
        settings.description() == Optional.of("Some check")
        settings.dependentOn() == Optional.of("some_service")
        settings.link() == Optional.of("www.google.com")
    }

    class NotAnnotatedHealthCheck extends HealthCheck {

        @Override
        protected HealthCheck.Result check() throws Exception {
            return HealthCheck.Result.healthy()
        }
    }

    @Settings(
            level = Level.WARNING,
            type = "EXTERNAL_DEPENDENCY",
            description = "Some check",
            dependentOn = "some_service",
            link = "www.google.com"
    )
    class AnnotatedHealthCheck extends HealthCheck {

        @Override
        protected HealthCheck.Result check() throws Exception {
            return HealthCheck.Result.healthy()
        }
    }
}
