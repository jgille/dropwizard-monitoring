package org.jgille.mumon.dropwizard.monitoring.blackbox.spec.health

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

        then:
        health.status == 'CRITICAL'

        and:
        def checks = health.health_checks as List
        checks.size == 4

        and:
        checks[0] == [
                name: 'broken',
                status: 'CRITICAL',
                type: 'SELF',
                message: 'Fail!',
                error: 'java.lang.RuntimeException: Fail!',
                description: 'I will always fail'
        ]

        and:
        checks[1] == [
                name: 'annotated',
                status: 'HEALTHY',
                type: 'EXTERNAL_DEPENDENCY',
                message: 'Search succeeded',
                dependent_on: 'google'
        ]

        and:
        checks[2].name == 'cached'

        and:
        checks[2].status == 'HEALTHY'

        and:
        checks[3] == [
                name: 'deadlocks',
                status: 'HEALTHY'
        ]

    }

    def checkHealth() {
        client.get( path: '/service/health' ).data
    }
}
