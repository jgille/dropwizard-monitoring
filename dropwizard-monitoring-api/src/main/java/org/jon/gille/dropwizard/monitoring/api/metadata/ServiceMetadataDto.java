package org.jon.gille.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.api.Dto;

public class ServiceMetadataDto extends Dto {

    @NotBlank
    public final String name;

    @NotBlank
    public final String version;

    public ServiceMetadataDto(@JsonProperty("name") String serviceName,
                              @JsonProperty("version") String serviceVersion) {
        this.name = serviceName;
        this.version = serviceVersion;
    }
}
