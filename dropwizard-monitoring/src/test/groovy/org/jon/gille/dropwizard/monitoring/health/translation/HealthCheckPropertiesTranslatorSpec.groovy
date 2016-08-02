package org.jon.gille.dropwizard.monitoring.health.translation

import org.jon.gille.dropwizard.monitoring.health.annotation.HealthCheckProperties
import org.jon.gille.dropwizard.monitoring.health.domain.Level
import org.jon.gille.dropwizard.monitoring.health.domain.Type
import org.mockito.Mockito
import spock.lang.Specification

import static org.mockito.Mockito.when

class HealthCheckPropertiesTranslatorSpec extends Specification {

    def "An annotation with only mandatory properties set can be translated"() {
        given:
        def level = Level.WARNING
        def type = Type.INFRASTRUCTURE
        HealthCheckProperties properties = Mockito.mock(HealthCheckProperties.class)
        when(properties.level()).thenReturn(level)
        when(properties.type()).thenReturn(type)
        when(properties.description()).thenReturn("")
        when(properties.dependentOn()).thenReturn("")
        when(properties.link()).thenReturn("")

        when:
        def settings = HealthCheckPropertiesTranslator.settingsFromAnnotation(properties)

        then:
        settings.level() == level
        settings.type() == type
        !settings.description().isPresent()
        !settings.dependentOn().isPresent()
        !settings.link().isPresent()
    }

    def "An annotation with all properties set can be translated"() {
        given:
        def level = Level.WARNING
        def type = Type.INFRASTRUCTURE
        def description = "Some description"
        def dependentOn = "some_service"
        def link = "www.google.com"
        HealthCheckProperties properties = Mockito.mock(HealthCheckProperties.class)
        when(properties.level()).thenReturn(level)
        when(properties.type()).thenReturn(type)
        when(properties.description()).thenReturn(description)
        when(properties.dependentOn()).thenReturn(dependentOn)
        when(properties.link()).thenReturn(link)

        when:
        def settings = HealthCheckPropertiesTranslator.settingsFromAnnotation(properties)

        then:
        settings.level() == level
        settings.type() == type
        settings.description() == Optional.of(description)
        settings.dependentOn() == Optional.of(dependentOn)
        settings.link() == Optional.of(link)
    }
}
