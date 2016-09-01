package org.jgille.mumon.dropwizard.monitoring.bundle;

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
import org.jgille.mumon.dropwizard.monitoring.config.HealthConfiguration;
import org.jgille.mumon.dropwizard.monitoring.config.MuMonConfiguration;
import org.jgille.mumon.dropwizard.monitoring.health.domain.DelegatingHealthCheckService;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckService;
import org.jgille.mumon.dropwizard.monitoring.health.domain.HealthCheckSettings;
import org.jgille.mumon.dropwizard.monitoring.health.domain.Level;
import org.jgille.mumon.dropwizard.monitoring.health.reporting.HealthReporterFactory;
import org.jgille.mumon.dropwizard.monitoring.health.reporting.ScheduledServiceHealthReporter;
import org.jgille.mumon.dropwizard.monitoring.health.reporting.ServiceHealthReporter;
import org.jgille.mumon.dropwizard.monitoring.health.resource.HealthCheckResource;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.InstanceMetadataProvider;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.LocalHostInstanceMetadataProvider;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.ServiceMetadata;
import org.jgille.mumon.dropwizard.monitoring.metadata.domain.ServiceMetadataProvider;
import org.jgille.mumon.dropwizard.monitoring.metadata.resource.ServiceMetadataResource;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

public class MuMonBundle<C extends MuMonConfiguration> implements ConfiguredBundle<C> {

    private final String instanceId;
    private final String hostAddress;

    private final List<ServiceMetadataProvider<C>> serviceMetadataProviders;
    private HealthCheckService healthCheckService;

    private MuMonBundle(InstanceMetadataProvider instanceMetadataProvider,
                        List<ServiceMetadataProvider<C>> serviceMetadataProviders) {
        this.instanceId = instanceMetadataProvider.instanceId();
        this.hostAddress = instanceMetadataProvider.hostAddress();
        this.serviceMetadataProviders = serviceMetadataProviders;
    }

    @Override
    public void run(C configuration, Environment environment) throws Exception {
        this.healthCheckService = new DelegatingHealthCheckService(environment.healthChecks());

        configureDefaultDropwizardChecks();

        String name = environment.getName();

        Map<String, Object> additionalMetadata = new LinkedHashMap<>();
        serviceMetadataProviders.forEach(p -> additionalMetadata.putAll(p.metadata(configuration)));

        ServiceMetadata serviceMetadata = ServiceMetadata.builder()
                .withServiceName(name)
                .withServiceVersion(ServiceManifestEntries.serviceVersion())
                .withInstanceId(instanceId)
                .withHostAddress(hostAddress)
                .withAdditionalMetadata(additionalMetadata)
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

    public static class Builder<C extends MuMonConfiguration> {

        private InstanceMetadataProvider instanceMetadataProvider = new LocalHostInstanceMetadataProvider();
        private final List<ServiceMetadataProvider<C>> serviceMetadataProviders = new ArrayList<>();

        public Builder<C> withInstanceMetadataProvider(InstanceMetadataProvider instanceMetadataProvider) {
            this.instanceMetadataProvider = instanceMetadataProvider;
            return this;
        }

        public Builder<C> addServiceMetadataProvider(ServiceMetadataProvider<C> serviceMetadataProvider) {
            serviceMetadataProviders.add(serviceMetadataProvider);
            return this;
        }

        public MuMonBundle<C> build() {
            return new MuMonBundle<>(instanceMetadataProvider, serviceMetadataProviders);
        }
    }

}
