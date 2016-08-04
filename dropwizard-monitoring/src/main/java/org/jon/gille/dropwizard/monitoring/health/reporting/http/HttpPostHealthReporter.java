package org.jon.gille.dropwizard.monitoring.health.reporting.http;

import io.dropwizard.lifecycle.Managed;
import org.jon.gille.dropwizard.monitoring.api.reporting.ServiceHealthReportDto;
import org.jon.gille.dropwizard.monitoring.health.domain.ServiceHealth;
import org.jon.gille.dropwizard.monitoring.health.reporting.ServiceHealthReporter;
import org.jon.gille.dropwizard.monitoring.health.reporting.translation.ServiceHealthTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class HttpPostHealthReporter implements ServiceHealthReporter, Managed {

    private final Client client;
    private final String uri;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    HttpPostHealthReporter(Client client, String uri) {
        this.client = client;
        this.uri = uri;
    }

    @Override
    public void report(ServiceHealth serviceHealth) {
        ServiceHealthReportDto report = ServiceHealthTranslator.createReport(serviceHealth);
        post(report);
    }

    private void post(ServiceHealthReportDto report) {
        Entity<ServiceHealthReportDto> entity = Entity.entity(report, MediaType.APPLICATION_JSON_TYPE);
        Response response = client.target(uri).request().post(entity);

        logger.debug("Posted report with id {}, got http response code {}", report.id, response.getStatus());
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        client.close();
    }

}
