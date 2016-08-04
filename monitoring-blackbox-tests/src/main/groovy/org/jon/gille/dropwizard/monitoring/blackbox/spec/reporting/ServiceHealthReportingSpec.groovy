package org.jon.gille.dropwizard.monitoring.blackbox.spec.reporting

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class ServiceHealthReportingSpec extends Specification {

    @Shared
    def monitorClient

    def setupSpec() {
        monitorClient = new RESTClient('http://monitor:8067')
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

    def reportCount() {
        monitorClient.get(path: '/monitor/services/report_count').data.report_count
    }
}
