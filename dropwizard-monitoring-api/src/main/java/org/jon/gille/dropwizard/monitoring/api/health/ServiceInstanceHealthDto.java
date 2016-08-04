package org.jon.gille.dropwizard.monitoring.api.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jon.gille.dropwizard.monitoring.api.Dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ServiceInstanceHealthDto extends Dto {

    @NotNull
    @Valid
    public final List<HealthCheckResultDto> unhealthy;

    @NotNull
    @Valid
    public final List<HealthCheckResultDto> healthy;

    public ServiceInstanceHealthDto(@JsonProperty("unhealthy") List<HealthCheckResultDto> unhealthy,
                                    @JsonProperty("healthy") List<HealthCheckResultDto> healthy) {
        this.unhealthy = unhealthy;
        this.healthy = healthy;
    }
}
