package org.jon.gille.dropwizard.monitoring.api.reporting;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jon.gille.dropwizard.monitoring.api.Dto;
import org.jon.gille.dropwizard.monitoring.api.health.ServiceInstanceHealthDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.MetadataDto;

import java.time.OffsetDateTime;

public class ServiceHealthReportDto extends Dto {

    public final String id;
    public final OffsetDateTime timestamp;
    public final MetadataDto metadata;
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
