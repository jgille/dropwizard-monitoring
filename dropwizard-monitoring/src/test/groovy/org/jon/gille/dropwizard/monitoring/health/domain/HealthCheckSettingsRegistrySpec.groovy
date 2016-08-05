package org.jon.gille.dropwizard.monitoring.health.domain

import spock.lang.Specification

class HealthCheckSettingsRegistrySpec extends Specification {

    def "I get an empty Optional for an unknown health check"() {
        given:
        def registry = new HealthCheckSettingsRegistry()

        when:
        def settings = registry.settings("something")

        then:

        !settings.isPresent()
    }

    def "I get the correct settings for a known health check"() {
        given:

        def name = "somethings"
        def registered = HealthCheckSettings.DEFAULT_SETTINGS
        def registry = new HealthCheckSettingsRegistry()
        registry.register(name, registered)

        when:

        def settings = registry.settings(name)

        then:

        settings == Optional.of(registered)
    }

    def "I get the default settings for an unknown health check"() {
        given:

        def registry = new HealthCheckSettingsRegistry()

        when:

        def defaultSettings = HealthCheckSettings.DEFAULT_SETTINGS
        def settings = registry.settings("something", defaultSettings)

        then:

        settings == defaultSettings
    }

    def "Default settings is ignored for a known health check"() {
        given:

        def name = "somethings"
        def registered = HealthCheckSettings
                .withLevel(Level.WARNING)
                .withType("EXTERNAL_DEPENDENCY")
                .withDescription("some description")
                .withLink("www.google.com")
                .build()
        def registry = new HealthCheckSettingsRegistry()
        registry.register(name, registered)

        when:

        def settings = registry.settings(name, HealthCheckSettings.DEFAULT_SETTINGS)

        then:

        settings == registered
    }

}
