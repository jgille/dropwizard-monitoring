package org.jgille.mumon.dropwizard.monitoring.health.translation

import org.jgille.mumon.dropwizard.monitoring.health.annotation.Settings
import org.jgille.mumon.dropwizard.monitoring.health.domain.Level
import org.mockito.Mockito
import spock.lang.Specification

import static org.mockito.Mockito.when

class HealthCheckSettingsTranslatorSpec extends Specification {

    def "An annotation with only mandatory properties set can be translated"() {
        given:
        def level = Level.WARNING
        Settings properties = Mockito.mock(Settings.class)
        when(properties.level()).thenReturn(level)
        when(properties.type()).thenReturn("")
        when(properties.description()).thenReturn("")
        when(properties.dependentOn()).thenReturn("")
        when(properties.link()).thenReturn("")

        when:
        def settings = HealthCheckSettingsTranslator.settingsFromAnnotation(properties)

        then:
        settings.level() == level
        !settings.type().isPresent()
        !settings.description().isPresent()
        !settings.dependentOn().isPresent()
        !settings.link().isPresent()
    }

    def "An annotation with all properties set can be translated"() {
        given:
        def level = Level.WARNING
        def type = "INFRASTRUCTURE"
        def description = "Some description"
        def dependentOn = "some_service"
        def link = "www.google.com"
        Settings properties = Mockito.mock(Settings.class)
        when(properties.level()).thenReturn(level)
        when(properties.type()).thenReturn(type)
        when(properties.description()).thenReturn(description)
        when(properties.dependentOn()).thenReturn(dependentOn)
        when(properties.link()).thenReturn(link)

        when:
        def settings = HealthCheckSettingsTranslator.settingsFromAnnotation(properties)

        then:
        settings.level() == level
        settings.type() == Optional.of(type)
        settings.description() == Optional.of(description)
        settings.dependentOn() == Optional.of(dependentOn)
        settings.link() == Optional.of(link)
    }
}
