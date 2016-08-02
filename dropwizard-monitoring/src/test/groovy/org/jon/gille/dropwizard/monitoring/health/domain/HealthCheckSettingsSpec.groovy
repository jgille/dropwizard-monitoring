package org.jon.gille.dropwizard.monitoring.health.domain

import spock.lang.Specification

class HealthCheckSettingsSpec extends Specification {

    def "It is possible to set all values"() {
        when:

        def level = Level.WARNING
        def type = Type.INFRASTRUCTURE
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
        settings.type() == type

        and:
        settings.level() == level

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
        def type = Type.INFRASTRUCTURE

        def settings =
                HealthCheckSettings
                        .withLevel(level)
                        .withType(type)
                        .build()

        then:
        settings.type() == type

        and:
        settings.level() == level

        and:
        settings.description() == Optional.empty()

        and:
        settings.dependentOn() == Optional.empty()

        and:
        settings.link() == Optional.empty()

    }
}
