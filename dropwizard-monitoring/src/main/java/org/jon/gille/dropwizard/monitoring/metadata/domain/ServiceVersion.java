package org.jon.gille.dropwizard.monitoring.metadata.domain;

import org.jon.gille.dropwizard.monitoring.ValueObject;

public class ServiceVersion extends ValueObject {

    private final String version;

    public ServiceVersion(String version) {
        this.version = version;
    }

    public String version() {
        return version;
    }
}
