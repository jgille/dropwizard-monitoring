package org.jon.gille.dropwizard.monitoring.blackbox.spec.metadata

import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Specification

class MetadataSpec extends Specification {

    @Shared
    def client

    def setupSpec() {
        client = new RESTClient('http://monitored-service:9066')
    }

    def "The metadata endpoint returns the expected response"() {
        when:
        def metadata = getMetadata()

        then:
        metadata != null

        and:
        def service = metadata.service
        service != null

        and:
        def instance = metadata.instance
        instance != null

        and:
        service.service_name == 'Sample App'

        and:
        service.service_version != null

        and:
        instance.instance_id != null

        and:
        instance.host_address != null
    }

    def getMetadata() {
        client.get( path: '/service/metadata' ).data
    }
}
