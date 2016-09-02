package org.jgille.mumon.dropwizard.monitoring.blackbox.spec.metadata

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
        service.name == 'Sample App'

        and:
        service.version != null

        and:
        instance.id != null

        and:
        instance.host_address != null

        and:
        metadata.additional_metadata == [
                key: 'value',
                otherKey: 'otherValue'
        ]
    }

    def getMetadata() {
        client.get( path: '/service/metadata' ).data
    }
}
