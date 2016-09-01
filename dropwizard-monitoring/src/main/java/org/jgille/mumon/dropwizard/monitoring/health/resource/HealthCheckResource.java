package org.jgille.mumon.dropwizard.monitoring.health.resource;

import org.jgille.mumon.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jgille.mumon.dropwizard.monitoring.api.health.HealthCheckResultDto;
import org.jgille.mumon.dropwizard.monitoring.api.health.ServiceInstanceHealthDto;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckService;
import org.jgille.mumon.dropwizard.monitoring.health.translation.api.HealthCheckResultTranslator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("health")
public class HealthCheckResource {

    private final HealthCheckService healthCheckService;

    // TODO: Make it possible to run health checks in parallel

    public HealthCheckResource(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GET
    public ServiceInstanceHealthDto runHealthChecks() {
        ServiceHealth serviceHealth = healthCheckService.runHealthChecks();
        return new ServiceInstanceHealthDto(serviceHealth.status().name(), mapToDtos(serviceHealth.executedChecks().stream()));
    }

    private List<HealthCheckResultDto> mapToDtos(Stream<HealthCheckResult> checks) {
        return checks.map(HealthCheckResultTranslator::mapToDto).collect(toList());
    }

}
