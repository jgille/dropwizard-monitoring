package org.jon.gille.dropwizard.monitoring.metadata.resource;

import org.jon.gille.dropwizard.monitoring.api.metadata.InstanceMetadataDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.MetadataDto;
import org.jon.gille.dropwizard.monitoring.api.metadata.ServiceMetadataDto;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("metadata")
public class ServiceMetadataResource {

    private final MetadataDto metadata;

    public ServiceMetadataResource(ServiceMetadata serviceMetadata) {
        this.metadata = translate(serviceMetadata);
    }

    @GET
    public MetadataDto metadata() {
        return metadata;
    }

    private MetadataDto translate(ServiceMetadata serviceMetadata) {
        return new MetadataDto(
                new ServiceMetadataDto(serviceMetadata.serviceName().name(),
                        serviceMetadata.serviceVersion().version()),
                new InstanceMetadataDto(serviceMetadata.instanceMetadata().instanceId().id(),
                        serviceMetadata.instanceMetadata().hostAddress())
        );
    }

}
