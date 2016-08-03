package org.jon.gille.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstanceMetadataDto {

    public final String instance_id;
    public final String host_address;

    public InstanceMetadataDto(@JsonProperty("instance_id") String instanceId,
                               @JsonProperty("host_address") String hostAddress) {
        this.instance_id = instanceId;
        this.host_address = hostAddress;
    }
}
