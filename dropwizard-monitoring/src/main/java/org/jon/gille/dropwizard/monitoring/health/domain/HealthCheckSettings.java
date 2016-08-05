package org.jon.gille.dropwizard.monitoring.health.domain;

import org.jon.gille.dropwizard.monitoring.ValueObject;

import java.util.Optional;

public class HealthCheckSettings extends ValueObject {

    public static final HealthCheckSettings DEFAULT_SETTINGS =
            HealthCheckSettings.withLevel(Level.CRITICAL).build();

    private final Level level;

    private final Optional<String> type;

    private final Optional<String> description;

    private final Optional<String> dependentOn;

    private final Optional<String> link;

    public HealthCheckSettings(Builder builder) {
        this.level = builder.level;
        this.type = Optional.ofNullable(builder.type);
        this.description = Optional.ofNullable(builder.description);
        this.dependentOn = Optional.ofNullable(builder.dependentOn);
        this.link = Optional.ofNullable(builder.link);
    }

    public Level level() {
        return level;
    }

    public Optional<String> type() {
        return type;
    }

    public Optional<String> description() {
        return description;
    }

    public Optional<String> dependentOn() {
        return dependentOn;
    }

    public Optional<String> link() {
        return link;
    }

    public static Builder withLevel(Level level) {
        return new Builder(level);
    }

    public static class Builder {

        private final Level level;

        private String type;

        private String description;

        private String dependentOn;

        private String link;

        private Builder(Level level) {
            this.level = level;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withDependentOn(String dependentOn) {
            this.dependentOn = dependentOn;
            return this;
        }

        public Builder withLink(String link) {
            this.link = link;
            return this;
        }

        public HealthCheckSettings build() {
            return new HealthCheckSettings(this);
        }
    }
}
