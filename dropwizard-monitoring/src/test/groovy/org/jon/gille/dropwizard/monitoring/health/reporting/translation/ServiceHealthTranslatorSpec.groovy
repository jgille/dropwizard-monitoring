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
                .withServiceId("sid")
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
                        .withType(Type.EXTERNAL_DEPENDENCY)
                        .withDescription("Some check")
                        .withDependentOn("google")
                        .withLink("www.google.com")
                        .build(),
                HealthCheck.Result.healthy("Yes")
        )

        def serviceHealth = new ServiceHealth(
                UUID.randomUUID(),
                serviceMetadata,
                [
                        c1,
                        c2
                ],
                Instant.now()
        )

        when:
        def report = ServiceHealthTranslator.createReport(serviceHealth)

        then:
        report.id == serviceHealth.id().toString()

        and:
        report.timestamp == serviceHealth.timestamp().atOffset(ZoneOffset.UTC)

        and:
        report.metadata.service.service_id == serviceMetadata.serviceId().id()

        and:
        report.metadata.service.service_name == serviceMetadata.serviceName().name()

        and:
        report.metadata.service.service_version == serviceMetadata.serviceVersion().version()

        and:
        report.metadata.instance.instance_id == instanceMetadata.instanceId().id()

        and:
        report.metadata.instance.host_address == instanceMetadata.hostAddress()

        and:
        report.health.unhealthy.size() == 1
        def unhealthyCheck = report.health.unhealthy[0]

        and:
        compareHealthCheckResult(unhealthyCheck, c1)

        and:
        report.health.healthy.size() == 1
        def healthyCheck = report.health.healthy[0]

        and:
        compareHealthCheckResult(healthyCheck, c2)

    }

    def compareHealthCheckResult(HealthCheckResultDto translated, HealthCheckResult source) {
        assert translated.name == source.name()
        assert translated.message == source.message().orElse(null)
        assert translated.status == source.status().name()
        assert translated.type == source.type().name()
        assert translated.description == source.description().orElse(null)
        assert translated.dependent_on == source.dependentOn().orElse(null)
        assert translated.link == source.link().orElse(null)
        true
    }
}
