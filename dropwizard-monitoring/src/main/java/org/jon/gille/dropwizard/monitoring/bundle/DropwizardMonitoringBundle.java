package org.jon.gille.dropwizard.monitoring.bundle;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jon.gille.dropwizard.monitoring.health.domain.*;
import org.jon.gille.dropwizard.monitoring.health.resource.HealthCheckResource;
import org.jon.gille.dropwizard.monitoring.metadata.resource.ServiceMetadataResource;

public class DropwizardMonitoringBundle<C> implements ConfiguredBundle<C> {

    private HealthCheckService healthCheckService;

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        this.healthCheckService = new DelegatingHealthCheckService(environment.healthChecks());

        configureHealthCheck("deadlocks", HealthCheckSettings.withLevel(Level.CRITICAL).withType(Type.SELF) .build());

        registerResources(environment, "/service/*");
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    public void registerHealthCheck(String name, HealthCheck healthCheck) {
        healthCheckService.registerHealthCheck(name, healthCheck);
    }

    public void registerHealthCheck(String name, HealthCheck healthCheck, HealthCheckSettings settings) {
        healthCheckService.registerHealthCheck(name, healthCheck, settings);
    }

    public void configureHealthCheck(String name, HealthCheckSettings settings) {
        healthCheckService.configureHealthCheck(name, settings);
    }

    private void registerResources(Environment environment, String resourcePath) {
        DropwizardResourceConfig resourceConfig = new DropwizardResourceConfig(environment.metrics());
        JerseyContainerHolder jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(resourceConfig));
        resourceConfig.register(new HealthCheckResource(healthCheckService));
        resourceConfig.register(new ServiceMetadataResource());
        environment.admin().addServlet("health check resources", jerseyContainerHolder.getContainer())
                .addMapping(resourcePath);
    }

}
