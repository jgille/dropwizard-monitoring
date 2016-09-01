package org.jgille.mumon.dropwizard.monitoring.health.reporting

import com.codahale.metrics.health.HealthCheck
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckSettings
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckResult
import org.jgille.mumon.dropwizard.monitoring.health.domain.ServiceHealth
import org.jgille.mumon.dropwizard.monitoring.health.domain.Status
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.ServiceMetadata
import spock.lang.Specification

import java.time.Instant

class ConsoleHealthReporterSpec extends Specification {

    def "The console reporter prints service health information to the provided print stream"() {
        given:
        def bos = new ByteArrayOutputStream()
        def stream = new PrintStream(new BufferedOutputStream(bos))
        def reporter = new ConsoleHealthReporter(stream)

        def serviceMetadata = ServiceMetadata.builder()
                .withServiceName("My App").withServiceVersion("1.0")
                .withInstanceId("i1").withHostAddress("127.0.0.1").build()
        def executedChecks = [
                new HealthCheckResult("check", HealthCheckSettings.DEFAULT_SETTINGS,
                        HealthCheck.Result.healthy("it worked"))
        ]
        def timestamp = Instant.now()
        def id = UUID.randomUUID()

        def serviceHealth = new ServiceHealth(id, Status.HEALTHY, executedChecks, timestamp)

        when:
        reporter.report(serviceMetadata, serviceHealth)

        then:
        stream.flush()
        def logged = bos.toString("UTF-8")

        logged ==
                """
--- My App Service Health ---
0 unhealthy checks, 1 healthy checks

"""
    }

    def "The console reporter prints each unhealthy check to the provided print stream"() {
        given:
        def bos = new ByteArrayOutputStream()
        def stream = new PrintStream(new BufferedOutputStream(bos))
        def reporter = new ConsoleHealthReporter(stream)

        def serviceMetadata = ServiceMetadata.builder()
                .withServiceName("My App").withServiceVersion("1.0")
                .withInstanceId("i1").withHostAddress("127.0.0.1").build()
        def executedChecks = [
                new HealthCheckResult("check", HealthCheckSettings.DEFAULT_SETTINGS,
                        HealthCheck.Result.unhealthy("it didn't work"))
        ]
        def timestamp = Instant.now()
        def id = UUID.randomUUID()

        def serviceHealth = new ServiceHealth(id, Status.HEALTHY, executedChecks, timestamp)

        when:
        reporter.report(serviceMetadata, serviceHealth)

        then:
        stream.flush()
        def logged = bos.toString("UTF-8")

        logged ==
                """
--- My App Service Health ---
1 unhealthy checks, 0 healthy checks
------ Unhealthy checks ------
    * check - CRITICAL

"""
    }
}
