package org.jon.gille.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.api.Dto;

public class InstanceMetadataDto extends Dto {

    @NotBlank
    public final String instance_id;

    @NotBlank
    public final String host_address;

    public InstanceMetadataDto(@JsonProperty("instance_id") String instanceId,
                               @JsonProperty("host_address") String hostAddress) {
        this.instance_id = instanceId;
        this.host_address = hostAddress;
    }
}
