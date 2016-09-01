package org.jgille.mumon.dropwizard.monitoring.metadata.domain;

import org.jgille.mumon.dropwizard.monitoring.ValueObject;

public class ServiceVersion extends ValueObject {

    private final String version;

    public ServiceVersion(String version) {
        this.version = version;
    }

    public String version() {
        return version;
    }
}
