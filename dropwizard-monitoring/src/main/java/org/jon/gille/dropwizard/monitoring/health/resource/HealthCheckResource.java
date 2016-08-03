package org.jon.gille.dropwizard.monitoring.health.resource;

import org.jon.gille.dropwizard.monitoring.api.health.HealthCheckResultDto;
import org.jon.gille.dropwizard.monitoring.api.health.ServiceInstanceHealthDto;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckService;
import org.jon.gille.dropwizard.monitoring.health.translation.api.HealthCheckResultTranslator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("healthcheck")
public class HealthCheckResource {

    private final HealthCheckService healthCheckService;

    // TODO: Make it possible to run health checks in parallel

    public HealthCheckResource(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GET
    public ServiceInstanceHealthDto runHealthChecks() {
        List<HealthCheckResult> results = healthCheckService.runHealthChecks();
        Stream<HealthCheckResult> unhealthy = results.stream().filter(HealthCheckResult::isUnhealthy);
        Stream<HealthCheckResult> healthy = results.stream().filter(HealthCheckResult::isHealthy);

        return new ServiceInstanceHealthDto(mapToDtos(unhealthy), mapToDtos(healthy));
    }

    private List<HealthCheckResultDto> mapToDtos(Stream<HealthCheckResult> checks) {
        return checks.map(HealthCheckResultTranslator::mapToDto).collect(toList());
    }

}
