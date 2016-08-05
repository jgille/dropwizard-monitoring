package org.jon.gille.dropwizard.monitoring.bundle;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.collect.ImmutableList;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.jersey.setup.JerseyContainerHolder;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jon.gille.dropwizard.monitoring.config.DropwizardMonitoringConfiguration;
import org.jon.gille.dropwizard.monitoring.config.HealthConfiguration;
import org.jon.gille.dropwizard.monitoring.health.domain.DelegatingHealthCheckService;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckService;
import org.jon.gille.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jon.gille.dropwizard.monitoring.health.domain.Level;
import org.jon.gille.dropwizard.monitoring.health.reporting.HealthReporterFactory;
import org.jon.gille.dropwizard.monitoring.health.reporting.ScheduledServiceHealthReporter;
import org.jon.gille.dropwizard.monitoring.health.reporting.ServiceHealthReporter;
import org.jon.gille.dropwizard.monitoring.health.resource.HealthCheckResource;
import org.jon.gille.dropwizard.monitoring.metadata.domain.InstanceMetadataProvider;
import org.jon.gille.dropwizard.monitoring.metadata.domain.LocalHostInstanceMetadataProvider;
import org.jon.gille.dropwizard.monitoring.metadata.domain.ServiceMetadata;
import org.jon.gille.dropwizard.monitoring.metadata.resource.ServiceMetadataResource;

import java.util.concurrent.ScheduledExecutorService;

public class DropwizardMonitoringBundle<C extends DropwizardMonitoringConfiguration> implements ConfiguredBundle<C> {

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

        scheduleReporters(configuration, environment, serviceMetadata);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    private void scheduleReporters(C configuration, Environment environment, ServiceMetadata serviceMetadata) {
        HealthConfiguration health = configuration.getHealth();
        ImmutableList<HealthReporterFactory> reporters = health.getReporters();

        reporters.forEach(reporterFactory -> {
            Duration frequency = reporterFactory.getFrequency().orElse(health.getDefaultReportingFrequency());
            ScheduledExecutorService scheduler =
                    environment.lifecycle().scheduledExecutorService(reporterFactory.getName())
                    .threads(1).build();
            ServiceHealthReporter reporter = reporterFactory.build();
            ScheduledServiceHealthReporter scheduledReporter =
                    new ScheduledServiceHealthReporter(serviceMetadata, healthCheckService, scheduler,
                            frequency, reporter);
            environment.lifecycle().manage(scheduledReporter);
            if (reporter instanceof Managed) {
                environment.lifecycle().manage((Managed) reporter);
            }
        });
    }

    private void configureDefaultDropwizardChecks() {
        configureHealthCheck("deadlocks", HealthCheckSettings.withLevel(Level.CRITICAL).build());
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
