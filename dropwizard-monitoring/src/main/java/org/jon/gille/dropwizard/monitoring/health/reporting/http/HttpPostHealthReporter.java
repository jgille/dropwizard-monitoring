package org.jon.gille.dropwizard.monitoring.health.reporting.http;

import io.dropwizard.lifecycle.Managed;
import org.jon.gille.dropwizard.monitoring.api.health.HealthCheckResultDto;
import org.jon.gille.dropwizard.monitoring.api.health.ServiceInstanceHealthDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.InstanceMetadataDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.MetadataDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.ServiceMetadataDto;
import org.jon.gille.dropwizard.monitoring.api.reporting.ServiceHealthReportDto;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jon.gille.dropwizard.monitoring.health.reporting.ServiceHealthReporter;
import org.jon.gille.dropwizard.monitoring.health.translation.api.HealthCheckResultTranslator;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class HttpPostHealthReporter implements ServiceHealthReporter, Managed {

    private final Client client;
    private final String uri;

    HttpPostHealthReporter(Client client, String uri) {
        this.client = client;
        this.uri = uri;
    }

    @Override
    public void report(ServiceHealth serviceHealth) {
        MetadataDto metadataDto = translate(serviceHealth.serviceMetadata());
        ServiceInstanceHealthDto serviceInstanceHealthDto = translate(serviceHealth.executedChecks());

        post(new ServiceHealthReportDto(
                        serviceHealth.id().toString(),
                        serviceHealth.timestamp().atOffset(ZoneOffset.UTC),
                        metadataDto,
                        serviceInstanceHealthDto)
        );
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        client.close();
    }

    private void post(ServiceHealthReportDto report) {
        Entity<ServiceHealthReportDto> entity = Entity.entity(report, MediaType.APPLICATION_JSON_TYPE);
        client.target(uri).request().post(entity);
    }

    private MetadataDto translate(ServiceMetadata serviceMetadata) {
        return new MetadataDto(
                new ServiceMetadataDto(serviceMetadata.serviceId().id(),
                        serviceMetadata.serviceName().name(),
                        serviceMetadata.serviceVersion().version()),
                new InstanceMetadataDto(serviceMetadata.instanceMetadata().instanceId().id(),
                        serviceMetadata.instanceMetadata().hostAddress())
        );
    }

    private ServiceInstanceHealthDto translate(List<HealthCheckResult> executedChecks) {
        Stream<HealthCheckResult> unhealthy = executedChecks.stream().filter(HealthCheckResult::isUnhealthy);
        Stream<HealthCheckResult> healthy = executedChecks.stream().filter(HealthCheckResult::isHealthy);

        return new ServiceInstanceHealthDto(translate(unhealthy), translate(healthy));
    }

    private List<HealthCheckResultDto> translate(Stream<HealthCheckResult> checks) {
        return checks.map(HealthCheckResultTranslator::mapToDto).collect(toList());
    }
}
