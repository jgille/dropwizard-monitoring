package org.jon.gille.dropwizard.monitoring.health.translation.api

import com.codahale.metrics.health.HealthCheck
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings
import org.jon.gille.dropwizard.monitoring.health.domain.Level

import spock.lang.Specification

class HealthCheckResultTranslatorSpec extends Specification {

    def "A check with only mandatory values set can be translated"() {
        given:
        def check = new HealthCheckResult("c", HealthCheckSettings.DEFAULT_SETTINGS, HealthCheck.Result.healthy())

        when:
        def dto = HealthCheckResultTranslator.mapToDto(check)

        then:
        dto.name == check.name()

        and:
        dto.status == check.status().name()

        and:
        dto.type == null

        and:
        dto.message == null

        and:
        dto.error == null

        and:
        dto.description == null

        and:
        dto.dependent_on == null

        and:
        dto.link == null
    }

    def "A check with all values set can be translated"() {
        given:
        def message = "Yeay"
        def check = new HealthCheckResult("c",
                HealthCheckSettings.withLevel(Level.WARNING)
                .withType("EXTERNAL_DEPENDENCY")
                .withDescription("Some descriptio")
                .withDependentOn("some_service")
                .withLink("127.0.0.1")
                .build(),
                HealthCheck.Result.healthy(message))

        when:
        def dto = HealthCheckResultTranslator.mapToDto(check)

        then:
        dto.name == check.name()

        and:
        dto.status == check.status().name()

        and:
        dto.type == check.type().get()

        and:
        dto.message == message

        and:
        dto.description == check.description().get()

        and:
        dto.dependent_on == check.dependentOn().get()

        and:
        dto.link == check.link().get()
    }

    @SuppressWarnings("GrEqualsBetweenInconvertibleTypes")
    def "An exception is translated into an error string"() {
        given:
        def message = "Fail!"
        def check = new HealthCheckResult("c", HealthCheckSettings.DEFAULT_SETTINGS,
                HealthCheck.Result.unhealthy(new RuntimeException(message)))

        when:
        def dto = HealthCheckResultTranslator.mapToDto(check)

        then:
        dto.error == "java.lang.RuntimeException: $message"
    }
}
