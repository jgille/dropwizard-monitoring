package org.jgille.mumon.dropwizard.monitoring.sample.app.health;

import com.codahale.metrics.health.HealthCheck;

public class UnhealthyHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        throw new RuntimeException("Fail!");
    }
}
