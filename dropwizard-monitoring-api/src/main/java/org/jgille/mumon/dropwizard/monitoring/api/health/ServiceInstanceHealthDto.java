package org.jgille.mumon.dropwizard.monitoring.api.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jgille.mumon.dropwizard.monitoring.api.Dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ServiceInstanceHealthDto extends Dto {

    @NotBlank
    public final String status;

    @NotNull
    @Valid
    public final List<HealthCheckResultDto> health_checks;

    public ServiceInstanceHealthDto(@JsonProperty("status") String status,
                                    @JsonProperty("health_checks") List<HealthCheckResultDto> health_checks) {
        this.status = status;
        this.health_checks = health_checks;
    }
}
