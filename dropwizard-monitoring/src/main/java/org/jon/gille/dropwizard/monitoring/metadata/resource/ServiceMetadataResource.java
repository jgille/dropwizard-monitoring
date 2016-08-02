package org.jon.gille.dropwizard.monitoring.metadata.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("metadata")
public class ServiceMetadataResource {

    @GET
    // TODO: Implement me
    public Object metadata() {
        return new String[]{"Not implemented"};
    }

}
