package org.jon.gille.dropwizard.monitoring.blackbox.spec.health

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

class HealthCheckSpec extends Specification {

    @Shared
    def client

    def setupSpec() {
        client = new RESTClient('http://monitored-service:9066')
        client.handler.success = { resp, json ->
            [response: resp, body: json]
        }
        client.handler.failure = client.handler.success
    }

    def "The health check endpoint returns the expected result"() {
        when:
        def health = checkHealth()

        then:
        health.healthy.size == 2

        and:
        health.unhealthy.size == 1
    }

    def checkHealth() {
        def res = client.get(path: '/service/healthcheck')
        assert res.response.status == 200
        res.body
    }
}
