package org.jon.gille.dropwizard.monitoring.health.domain

import com.codahale.metrics.health.HealthCheck
import spock.lang.Specification

class HealthCheckResultComparatorTest extends Specification {

    def "Comparison is by status first"() {
        given:
        def comparator = new HealthCheckResultComparator()
        def r1 = new HealthCheckResult("a", HealthCheckSettings.withLevel(Level.WARNING).build(),
                HealthCheck.Result.unhealthy(""))
        def r2 = new HealthCheckResult("b", HealthCheckSettings.withLevel(Level.CRITICAL).build(),
                HealthCheck.Result.unhealthy(""))

        when:
        def compare = comparator.compare(r1, r2)

        then:
        compare > 0
    }

    def "If two checks have the same status they are compared by name"() {
        given:
        def comparator = new HealthCheckResultComparator()
        def r1 = new HealthCheckResult("a", HealthCheckSettings.withLevel(Level.WARNING).build(),
                HealthCheck.Result.unhealthy(""))
        def r2 = new HealthCheckResult("b", HealthCheckSettings.withLevel(Level.WARNING).build(),
                HealthCheck.Result.unhealthy(""))

        when:
        def compare = comparator.compare(r1, r2)

        then:
        compare < 0
    }
}
