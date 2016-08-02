package org.jon.gille.dropwizard.monitoring.health.domain;

import java.util.Optional;

public class HealthCheckSettings extends ValueObject {

    public static final HealthCheckSettings DEFAULT_SETTINGS =
            HealthCheckSettings.withLevel(Level.CRITICAL).withType(Type.DEFAULT).build();

    private final Level level;

    private final Type type;

    private final Optional<String> description;

    private final Optional<String> dependentOn;

    private final Optional<String> link;

    public HealthCheckSettings(Builder builder) {
        this.level = builder.level;
        this.type = builder.type;
        this.description = Optional.ofNullable(builder.description);
        this.dependentOn = Optional.ofNullable(builder.dependentOn);
        this.link = Optional.ofNullable(builder.link);
    }

    public Level level() {
        return level;
    }

    public Type type() {
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

    public static LevelBuilder withLevel(Level level) {
        return new LevelBuilder(level);
    }

    public static class LevelBuilder {
        private final Level level;

        private LevelBuilder(Level level) {
            this.level = level;
        }

        public Builder withType(Type type) {
            return new Builder(level, type);
        }

    }

    public static class Builder {

        private final Level level;

        private final Type type;

        private String description;

        private String dependentOn;

        private String link;

        private Builder(Level level, Type type) {
            this.level = level;
            this.type = type;
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
