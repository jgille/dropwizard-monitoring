package org.jgille.mumon.dropwizard.monitoring.health

import com.codahale.metrics.health.HealthCheck
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckDecorator
import spock.lang.Specification

class ChainedHealthCheckDecoratorSpec extends Specification {

    def "An empty chain does leads to no decorating"() {
        given:
        def result = HealthCheck.Result.healthy("a")
        HealthCheck healthCheck = { -> result }
        def decorator = ChainedHealthCheckDecorator.builder().build()

        when:
        def decorated = decorator.decorate(healthCheck)

        then:
        decorated == healthCheck
    }

    def "Decorators are applied in order"() {
        given:
        def result = HealthCheck.Result.healthy("a")
        HealthCheck healthCheck = { -> result }
        def decorator = ChainedHealthCheckDecorator.builder()
                .add(new SuffixMessageHealthCheckDecorator("b"))
                .add(new SuffixMessageHealthCheckDecorator("c"))
                .add(new SuffixMessageHealthCheckDecorator("d"))
                .build()

        when:
        def decorated = decorator.decorate(healthCheck)

        then:
        decorated.execute().message == "abcd"
    }

    class SuffixMessageHealthCheckDecorator implements HealthCheckDecorator {

        final String suffix

        SuffixMessageHealthCheckDecorator(String suffix) {
            this.suffix = suffix
        }

        @Override
        HealthCheck decorate(HealthCheck healthCheck) {
            return { -> HealthCheck.Result.healthy(healthCheck.execute().message + suffix) }
        }
    }
}
