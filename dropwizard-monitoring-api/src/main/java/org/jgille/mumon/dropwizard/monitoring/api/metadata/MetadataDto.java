package org.jgille.mumon.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jgille.mumon.dropwizard.monitoring.api.Dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MetadataDto extends Dto {

    @NotNull
    @Valid
    public final ServiceMetadataDto service;

    @NotNull
    @Valid
    public final InstanceMetadataDto instance;

    public MetadataDto(@JsonProperty("service") ServiceMetadataDto service,
                       @JsonProperty("instance") InstanceMetadataDto instance) {
        this.service = service;
        this.instance = instance;
    }
}
