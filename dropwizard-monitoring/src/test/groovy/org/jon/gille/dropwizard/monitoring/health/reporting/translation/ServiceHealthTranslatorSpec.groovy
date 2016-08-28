package org.jon.gille.dropwizard.monitoring.health.reporting.translation

import com.codahale.metrics.health.HealthCheck
import org.jon.gille.dropwizard.monitoring.api.health.HealthCheckResultDto
import org.jon.gille.dropwizard.monitoring.health.domain.*
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata
import spock.lang.Specification

import java.time.Instant
import java.time.ZoneOffset

class ServiceHealthTranslatorSpec extends Specification {

    def "A report object can be created from a service health instance"() {
        given:

        def serviceMetadata = ServiceMetadata.builder()
                .withServiceName("Service")
                .withServiceVersion("1.0")
                .withInstanceId("i1")
                .withHostAddress("127.0.0.1")
                .build()
        def instanceMetadata = serviceMetadata.instanceMetadata()

        def c1 = new HealthCheckResult(
                "c1", HealthCheckSettings.DEFAULT_SETTINGS, HealthCheck.Result.unhealthy("Nooo")
        )

        def c2 = new HealthCheckResult(
                "c2",
                HealthCheckSettings
                        .withLevel(Level.WARNING)
                        .withType("EXTERNAL_DEPENDENCY")
                        .withDescription("Some check")
                        .withDependentOn("google")
                        .withLink("www.google.com")
                        .build(),
                HealthCheck.Result.healthy("Yes")
        )

        def serviceHealth = new ServiceHealth(
                UUID.randomUUID(),
                Status.CRITICAL,
                [
                        c1,
                        c2
                ],
                Instant.now()
        )

        when:
        def report = ServiceHealthTranslator.createReport(serviceMetadata, serviceHealth)

        then:
        report.id == serviceHealth.id().toString()

        and:
        report.timestamp == serviceHealth.timestamp().atOffset(ZoneOffset.UTC)

        and:
        report.metadata.service.name == serviceMetadata.serviceName().name()

        and:
        report.metadata.service.version == serviceMetadata.serviceVersion().version()

        and:
        report.metadata.instance.id == instanceMetadata.instanceId().id()

        and:
        report.metadata.instance.host_address == instanceMetadata.hostAddress()

        and:
        def checks = report.health.health_checks
        checks.size() == 2
        def unhealthyCheck = checks[0]

        and:
        compareHealthCheckResult(unhealthyCheck, c1)

        and:
        def healthyCheck = checks[1]

        and:
        compareHealthCheckResult(healthyCheck, c2)
    }

    def compareHealthCheckResult(HealthCheckResultDto translated, HealthCheckResult source) {
        assert translated.name == source.name()
        assert translated.message == source.message().orElse(null)
        assert translated.status == source.status().name()
        assert translated.type == source.type().orElse(null)
        assert translated.description == source.description().orElse(null)
        assert translated.dependent_on == source.dependentOn().orElse(null)
        assert translated.link == source.link().orElse(null)
        true
    }
}
