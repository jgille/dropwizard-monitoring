package org.jgille.mumon.dropwizard.monitoring.metadata.domain;

import org.jgille.mumon.dropwizard.monitoring.ValueObject;

public class InstanceMetadata extends ValueObject {

    private final InstanceId instanceId;
    private final String hostAddress;

    public InstanceMetadata(InstanceId instanceId, String hostAddress) {
        this.instanceId = instanceId;
        this.hostAddress = hostAddress;
    }

    public InstanceId instanceId() {
        return instanceId;
    }

    public String hostAddress() {
        return hostAddress;
    }
}
