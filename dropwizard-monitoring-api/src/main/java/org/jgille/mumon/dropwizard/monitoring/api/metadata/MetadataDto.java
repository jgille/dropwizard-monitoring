package org.jgille.mumon.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jgille.mumon.dropwizard.monitoring.api.Dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

public class MetadataDto extends Dto {

    @NotNull
    @Valid
    public final ServiceMetadataDto service;

    @NotNull
    @Valid
    public final InstanceMetadataDto instance;

    @NotNull
    public final Map<String, Object> additional_metadata;

    public MetadataDto(@JsonProperty("service") ServiceMetadataDto service,
                       @JsonProperty("instance") InstanceMetadataDto instance,
                       @JsonProperty("additional_metadata") Map<String, Object> additionalMetadata) {
        this.service = service;
        this.instance = instance;
        this.additional_metadata = additionalMetadata;
    }
}
