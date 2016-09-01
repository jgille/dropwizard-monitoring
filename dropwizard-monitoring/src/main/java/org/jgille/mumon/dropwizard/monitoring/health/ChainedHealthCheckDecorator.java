package org.jgille.mumon.dropwizard.monitoring.health;

import com.codahale.metrics.health.HealthCheck;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckDecorator;

import java.util.ArrayList;
import java.util.List;

public class ChainedHealthCheckDecorator implements HealthCheckDecorator {

    private final List<HealthCheckDecorator> decoratorChain;

    public ChainedHealthCheckDecorator(Builder builder) {
        this.decoratorChain = new ArrayList<>(builder.decoratorChain);
    }

    @Override
    public HealthCheck decorate(HealthCheck healthCheck) {
        return decoratorChain.stream().reduce(healthCheck, (c, d) -> d.decorate(c), (c1, c2) -> c2);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<HealthCheckDecorator> decoratorChain = new ArrayList<>();

        private Builder() {
        }

        public Builder add(HealthCheckDecorator decorator) {
            decoratorChain.add(decorator);
            return this;
        }

        public HealthCheckDecorator build() {
            return new ChainedHealthCheckDecorator(this);
        }

    }

}
