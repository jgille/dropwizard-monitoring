package org.jon.gille.dropwizard.monitoring.blackbox.spec.reporting

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId

class ServiceHealthReportingSpec extends Specification {

    @Shared
    def monitorClient

    @Shared
    def serviceClient

    def setupSpec() {
        monitorClient = new RESTClient('http://monitor:8067')
        serviceClient = new RESTClient('http://monitored-service:9066')
    }

    def "The monitored service sends service health report via http post"() {
        given:
        def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, delay: 0.2)

        when:
        def initialReportCount = reportCount()

        then:
        conditions.eventually {
            def reportCount = reportCount()
            assert reportCount > initialReportCount
        }

    }

    def "The sent reports contain the correct service health information"() {
        given:
        def conditions = new PollingConditions(timeout: 10, initialDelay: 0.2, delay: 0.2)
        def metadata = getMetadata()
        def currentHealth = checkServiceHealth()

        when:
        def initialReportCount = reportCount()

        and:
        conditions.eventually {
            def reportCount = reportCount()
            assert reportCount > initialReportCount
        }

        and:
        def report = mostRecentReport(metadata.service.service_id)

        then:
        report.id != null

        and:
        def timestamp = OffsetDateTime.parse(report.timestamp)
        def durationSinceReportGeneration = Duration.between(timestamp, OffsetDateTime.now(ZoneId.of("UTC")))
        !durationSinceReportGeneration.isNegative()
        durationSinceReportGeneration.getSeconds() < 10

        and:
        report.metadata == metadata

        and:
        report.health == currentHealth
    }

    def reportCount() {
        monitorClient.get(path: '/monitor/services/report_count').data.report_count
    }

    def mostRecentReport(serviceId) {
        monitorClient.get(path: "/monitor/services/$serviceId").data
    }

    def getMetadata() {
        serviceClient.get(path: "/service/metadata").data
    }

    def checkServiceHealth() {
        serviceClient.get(path: "/service/healthcheck").data
    }
}
