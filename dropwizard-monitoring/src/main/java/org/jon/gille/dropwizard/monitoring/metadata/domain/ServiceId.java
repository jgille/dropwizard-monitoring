package org.jon.gille.dropwizard.monitoring.metadata.domain;

import org.jon.gille.dropwizard.monitoring.ValueObject;

public class ServiceId extends ValueObject {

    private final String id;

    public ServiceId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
