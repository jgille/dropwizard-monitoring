package org.jon.gille.dropwizard.monitoring.sample.monitor;


import org.jon.gille.dropwizard.monitoring.api.reporting.ServiceHealthReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("monitor/services")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class InMemoryMonitorResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger receivedReports = new AtomicInteger();

    @POST
    public void onReport(@Valid ServiceHealthReportDto report) {
        logger.info("Received report with id {}", report.id);
        receivedReports.incrementAndGet();
    }

    @GET
    @Path("report_count")
    public Object numberOfReceivedReports() {
        return Collections.singletonMap("report_count", receivedReports.get());
    }
}
