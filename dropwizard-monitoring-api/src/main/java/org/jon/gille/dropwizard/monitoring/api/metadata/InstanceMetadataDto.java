package org.jon.gille.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.api.Dto;

public class InstanceMetadataDto extends Dto {

    @NotBlank
    public final String id;

    @NotBlank
    public final String host_address;

    public InstanceMetadataDto(@JsonProperty("id") String instanceId,
                               @JsonProperty("host_address") String hostAddress) {
        this.id = instanceId;
        this.host_address = hostAddress;
    }
}
