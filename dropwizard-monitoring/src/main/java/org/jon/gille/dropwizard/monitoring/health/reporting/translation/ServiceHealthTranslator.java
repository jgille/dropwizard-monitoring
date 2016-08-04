package org.jon.gille.dropwizard.monitoring.health.reporting.translation;

import org.jon.gille.dropwizard.monitoring.api.health.HealthCheckResultDto;
import org.jon.gille.dropwizard.monitoring.api.health.ServiceInstanceHealthDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.InstanceMetadataDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.MetadataDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.ServiceMetadataDto;
import org.jon.gille.dropwizard.monitoring.api.reporting.ServiceHealthReportDto;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckResult;
import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jon.gille.dropwizard.monitoring.health.translation.api.HealthCheckResultTranslator;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ServiceHealthTranslator {

    public static ServiceHealthReportDto createReport(ServiceHealth serviceHealth) {
        MetadataDto metadataDto = translate(serviceHealth.serviceMetadata());
        ServiceInstanceHealthDto serviceInstanceHealthDto = translate(serviceHealth.executedChecks());

        return new ServiceHealthReportDto(
                serviceHealth.id().toString(),
                serviceHealth.timestamp().atOffset(ZoneOffset.UTC),
                metadataDto,
                serviceInstanceHealthDto);
    }

    private static MetadataDto translate(ServiceMetadata serviceMetadata) {
        return new MetadataDto(
                new ServiceMetadataDto(serviceMetadata.serviceId().id(),
                        serviceMetadata.serviceName().name(),
                        serviceMetadata.serviceVersion().version()),
                new InstanceMetadataDto(serviceMetadata.instanceMetadata().instanceId().id(),
                        serviceMetadata.instanceMetadata().hostAddress())
        );
    }

    private static ServiceInstanceHealthDto translate(List<HealthCheckResult> executedChecks) {
        Stream<HealthCheckResult> unhealthy = executedChecks.stream().filter(HealthCheckResult::isUnhealthy);
        Stream<HealthCheckResult> healthy = executedChecks.stream().filter(HealthCheckResult::isHealthy);

        return new ServiceInstanceHealthDto(translate(unhealthy), translate(healthy));
    }

    private static List<HealthCheckResultDto> translate(Stream<HealthCheckResult> checks) {
        return checks.map(HealthCheckResultTranslator::mapToDto).collect(toList());
    }
}
