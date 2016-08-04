package org.jon.gille.dropwizard.monitoring.api.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.api.Dto;

public class ServiceMetadataDto extends Dto {

    @NotBlank
    public final String service_id;

    @NotBlank
    public final String service_name;

    @NotBlank
    public final String service_version;

    public ServiceMetadataDto(@JsonProperty("service_id") String serviceId,
                              @JsonProperty("service_name") String serviceName,
                              @JsonProperty("service_version") String serviceVersion) {
        this.service_id = serviceId;
        this.service_name = serviceName;
        this.service_version = serviceVersion;
    }
}
