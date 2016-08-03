package org.jon.gille.dropwizard.monitoring.metadata.domain

import spock.lang.Specification

class LocalHostInstanceMetadataProviderSpec extends Specification {

    def "The instance id is the localhost name"() {
        given:
        def provider = new LocalHostInstanceMetadataProvider()

        when:
        def instanceId = provider.instanceId()

        then:
        instanceId == Inet4Address.localHost.hostName
    }

    def "The host address is the localhost address"() {
        given:
        def provider = new LocalHostInstanceMetadataProvider()

        when:
        def hostAddress = provider.hostAddress()

        then:
        hostAddress == Inet4Address.localHost.hostAddress
    }
}
