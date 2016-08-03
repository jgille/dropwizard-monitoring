package org.jon.gille.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetadataDto {

    public final ServiceMetadataDto service;
    public final InstanceMetadataDto instance;

    public MetadataDto(@JsonProperty("service") ServiceMetadataDto service,
                       @JsonProperty("instance") InstanceMetadataDto instance) {
        this.service = service;
        this.instance = instance;
    }
}
