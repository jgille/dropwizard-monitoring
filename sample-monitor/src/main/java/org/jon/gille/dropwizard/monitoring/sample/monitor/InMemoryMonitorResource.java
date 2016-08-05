package org.jon.gille.dropwizard.monitoring.sample.monitor;


import org.jon.gille.dropwizard.monitoring.api.reporting.ServiceHealthReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("monitor/services")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class InMemoryMonitorResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger receivedReports = new AtomicInteger();

    private final LinkedList<ServiceHealthReportDto> reports = new LinkedList<>();

    private static final int MAX_SIZE = 1000;

    @POST
    public void onReport(@Valid ServiceHealthReportDto report) {
        logger.info("Received report with id {}", report.id);
        receivedReports.incrementAndGet();
        add(report);
    }

    @GET
    @Path("report_count")
    public Map<String, Integer> numberOfReceivedReports() {
        return Collections.singletonMap("report_count", receivedReports.get());
    }

    @GET
    @Path("{service_id}")
    public ServiceHealthReportDto currentHealth(@PathParam("service_id") String serviceId) {
        List<ServiceHealthReportDto> copy = copyReports();
        return copy.stream().filter(r -> serviceId.equals(r.metadata.service.service_id)).findFirst()
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    private synchronized List<ServiceHealthReportDto> copyReports() {
        return new ArrayList<>(reports);
    }

    private synchronized void add(ServiceHealthReportDto report) {
        if (reports.size() == MAX_SIZE) {
            reports.removeLast();
        }
        reports.addFirst(report);
    }


}
