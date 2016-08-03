package org.jon.gille.dropwizard.monitoring.bundle;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jon.gille.dropwizard.monitoring.health.domain.*;
import org.jon.gille.dropwizard.monitoring.health.resource.HealthCheckResource;
import org.jon.gille.dropwizard.monitoring.metadata.domain.InstanceMetadataProvider;
import org.jon.gille.dropwizard.monitoring.metadata.domain.LocalHostInstanceMetadataProvider;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;
import org.jon.gille.dropwizard.monitoring.metadata.resource.ServiceMetadataResource;

public class DropwizardMonitoringBundle<C extends Configuration> implements ConfiguredBundle<C> {

    private final String instanceId;
    private final String hostAddress;

    private HealthCheckService healthCheckService;

    public DropwizardMonitoringBundle() {
        this(new LocalHostInstanceMetadataProvider());
    }

    public DropwizardMonitoringBundle(InstanceMetadataProvider instanceMetadataProvider) {
        this.instanceId = instanceMetadataProvider.instanceId();
        this.hostAddress = instanceMetadataProvider.hostAddress();
    }

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        this.healthCheckService = new DelegatingHealthCheckService(environment.healthChecks());

        configureDefaultDropwizardChecks();

        String name = environment.getName();

        ServiceMetadata serviceMetadata = ServiceMetadata.builder()
                .withServiceId(ServiceManifestEntries.serviceId(name))
                .withServiceName(name)
                .withServiceVersion(ServiceManifestEntries.serviceVersion())
                .withInstanceId(instanceId)
                .withHostAddress(hostAddress)
                .build();

        registerResources(environment, "/service/*", serviceMetadata);
    }

    private void configureDefaultDropwizardChecks() {
        configureHealthCheck("deadlocks", HealthCheckSettings.withLevel(Level.CRITICAL).withType(Type.SELF) .build());
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

    private void registerResources(Environment environment, String resourcePath, ServiceMetadata serviceMetadata) {
        DropwizardResourceConfig resourceConfig = new DropwizardResourceConfig(environment.metrics());
        JerseyContainerHolder jerseyContainerHolder = new JerseyContainerHolder(new ServletContainer(resourceConfig));
        resourceConfig.register(new HealthCheckResource(healthCheckService));
        resourceConfig.register(new ServiceMetadataResource(serviceMetadata));
        environment.admin().addServlet("health check resources", jerseyContainerHolder.getContainer())
                .addMapping(resourcePath);
    }

}
