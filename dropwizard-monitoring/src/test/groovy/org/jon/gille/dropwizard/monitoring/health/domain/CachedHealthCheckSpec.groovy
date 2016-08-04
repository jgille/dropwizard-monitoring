package org.jon.gille.dropwizard.monitoring.health.domain

import com.codahale.metrics.health.HealthCheck
import spock.lang.Specification

import java.time.Duration

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class CachedHealthCheckSpec extends Specification {

    def "Cached result is returned within the ttl"() {
        when:
        def delegate = mock(HealthCheck.class)

        def result = HealthCheck.Result.healthy("Yes")
        when(delegate.execute()).thenReturn(result)
        def cachedHealthCheck = new CachedHealthCheck(delegate, Duration.ofSeconds(5))

        then:
        cachedHealthCheck.execute() == result

        and:
        cachedHealthCheck.execute() == result

        and:
        verifyOnlyExecutedOnce(delegate)
    }

    private static void verifyOnlyExecutedOnce(HealthCheck delegate) {
        verify(delegate, times(1)).execute()
    }
}
