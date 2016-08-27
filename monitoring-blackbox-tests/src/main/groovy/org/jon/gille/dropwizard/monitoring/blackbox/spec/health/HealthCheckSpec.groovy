package org.jon.gille.dropwizard.monitoring.blackbox.spec.health

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

class HealthCheckSpec extends Specification {

    @Shared
    def client

    def setupSpec() {
        client = new RESTClient('http://monitored-service:9066')
    }

    @SuppressWarnings(["GrUnresolvedAccess", "GrEqualsBetweenInconvertibleTypes"])
    def "The health check endpoint returns the expected response"() {
        when:
        def health = checkHealth()

        def healthy = health.healthy as List
        def unhealthy = health.unhealthy as List

        then:
        healthy.size == 3

        and:
        healthy[0] == [
                name: 'annotated',
                status: 'HEALTHY',
                type: 'EXTERNAL_DEPENDENCY',
                message: 'Search succeeded',
                dependent_on: 'google'
        ]

        and:
        healthy[1].name == 'cached'

        and:
        healthy[1].status == 'HEALTHY'

        and:
        healthy[2] == [
                name: 'deadlocks',
                status: 'HEALTHY'
        ]

        and:
        unhealthy == [
                [
                        name: 'broken',
                        status: 'CRITICAL',
                        type: 'SELF',
                        message: 'Fail!',
                        error: 'java.lang.RuntimeException: Fail!',
                        description: 'I will always fail'
                ]
        ]
    }

    def checkHealth() {
        client.get( path: '/service/health' ).data
    }
}
