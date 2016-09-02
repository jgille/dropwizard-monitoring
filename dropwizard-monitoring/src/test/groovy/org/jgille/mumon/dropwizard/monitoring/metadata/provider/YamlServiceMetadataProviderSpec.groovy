package org.jgille.mumon.dropwizard.monitoring.metadata.provider

import spock.lang.Specification

class YamlServiceMetadataProviderSpec extends Specification {

    def "Metadata in yml format can be read from a file on the classpath"() {
        given:
        YamlServiceMetadataProvider<String> provider =
                YamlServiceMetadataProvider.fromYamlFile("service-metadata/metadata.yml")

        when:
        def metadata = provider.metadata("")

        then:
        metadata == [
                key: 'Value',
                map: [
                        key: 'Value',
                        message: 'Hello',
                        tags: [1, 2, 3]
                ]
        ]
    }

    // TODO: Write tests that:
    // 1) Reads metadata from an absolute file name
    // 2) Reads broken yaml
}
