package org.jon.gille.dropwizard.monitoring.health.domain

import spock.lang.Specification

class HealthCheckSettingsSpec extends Specification {

    def "It is possible to set all values"() {
        when:

        def level = Level.WARNING
        def type = "INFRASTRUCTURE"
        def description = "Some description"
        def dependentOn = "some_service"
        def link = "www.google.com"

        def settings =
                HealthCheckSettings
                        .withLevel(level)
                        .withType(type)
                        .withDescription(description)
                        .withDependentOn(dependentOn)
                        .withLink(link)
                        .build()

        then:
        settings.level() == level

        and:
        settings.type() == Optional.of(type)

        and:
        settings.description() == Optional.of(description)

        and:
        settings.dependentOn() == Optional.of(dependentOn)

        and:
        settings.link() == Optional.of(link)

    }

    def "It is possible to omit all optional values"() {
        when:

        def level = Level.WARNING

        def settings =
                HealthCheckSettings
                        .withLevel(level)
                        .build()

        then:
        settings.level() == level

        and:
        settings.type() == Optional.empty()

        and:
        settings.description() == Optional.empty()

        and:
        settings.dependentOn() == Optional.empty()

        and:
        settings.link() == Optional.empty()
    }

    def "Empty fields does not replace original ones when overriding settings"() {
        given:
        def originalSettings = HealthCheckSettings.withLevel(Level.CRITICAL)
                .withType("type")
                .withDescription("desc")
                .withDependentOn("dep")
                .withLink("link")
                .build()

        def overrideWith = HealthCheckSettings.withLevel(Level.WARNING).build()

        when:
        def merged = originalSettings.overridenWith(overrideWith)

        then:
        merged == HealthCheckSettings.withLevel(Level.WARNING)
                .withType("type")
                .withDescription("desc")
                .withDependentOn("dep")
                .withLink("link")
                .build()

    }

    def "All non empty fields replaces original ones when overriding settings"() {
        given:
        def originalSettings = HealthCheckSettings.withLevel(Level.CRITICAL)
                .withType("type")
                .withDescription("desc")
                .withDependentOn("dep")
                .withLink("link")
                .build()

        def overrideWith =  HealthCheckSettings.withLevel(Level.WARNING)
                .withType("type1")
                .withDescription("descciption")
                .withDependentOn("depOn")
                .withLink("link2")
                .build()

        when:
        def merged = originalSettings.overridenWith(overrideWith)

        then:
        merged == overrideWith

    }
}
