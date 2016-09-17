package org.jgille.mumon.dropwizard.monitoring.api.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.jgille.mumon.dropwizard.monitoring.api.Dto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthCheckResultDto extends Dto {

    @NotBlank
    public final String name;

    @NotBlank
    public final String status;

    public final String description;

    public final String message;

    public final String error;

    public final String type;

    public final String dependent_on;

    public final String link;

    public HealthCheckResultDto(@JsonProperty("name") String name,
                                @JsonProperty("status") String status,
                                @JsonProperty("description") String description,
                                @JsonProperty("message") String message,
                                @JsonProperty("error") String error,
                                @JsonProperty("type") String type,
                                @JsonProperty("dependent_on") String dependentOn,
                                @JsonProperty("link") String link) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.message = message;
        this.error = error;
        this.type = type;
        this.dependent_on = dependentOn;
        this.link = link;
    }
}
