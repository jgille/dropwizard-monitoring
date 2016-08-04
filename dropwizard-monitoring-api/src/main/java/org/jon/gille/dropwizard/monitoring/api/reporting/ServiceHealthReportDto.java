package org.jon.gille.dropwizard.monitoring.api.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jon.gille.dropwizard.monitoring.api.Dto;
import org.jon.gille.dropwizard.monitoring.api.health.ServiceInstanceHealthDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.MetadataDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class ServiceHealthReportDto extends Dto {

    @NotBlank
    public final String id;

    @NotNull
    public final OffsetDateTime timestamp;

    @NotNull
    @Valid
    public final MetadataDto metadata;

    @NotNull
    @Valid
    public final ServiceInstanceHealthDto health;

    public ServiceHealthReportDto(@JsonProperty("id") String id,
                                  @JsonProperty("timestamp") OffsetDateTime timestamp,
                                  @JsonProperty("metadata") MetadataDto metadata,
                                  @JsonProperty("health") ServiceInstanceHealthDto health) {
        this.id = id;
        this.timestamp = timestamp;
        this.metadata = metadata;
        this.health = health;
    }
}
