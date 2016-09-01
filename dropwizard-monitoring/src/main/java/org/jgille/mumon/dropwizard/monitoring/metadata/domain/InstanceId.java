package org.jgille.mumon.dropwizard.monitoring.metadata.domain;

import org.jgille.mumon.dropwizard.monitoring.ValueObject;

public class InstanceId extends ValueObject {

    private final String id;

    public InstanceId(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
