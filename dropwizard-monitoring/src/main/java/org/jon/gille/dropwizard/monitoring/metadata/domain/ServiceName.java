package org.jon.gille.dropwizard.monitoring.metadata.domain;

import org.jon.gille.dropwizard.monitoring.ValueObject;

public class ServiceName extends ValueObject {

    private final String name;

    public ServiceName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
