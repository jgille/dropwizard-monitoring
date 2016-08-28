package org.jon.gille.dropwizard.monitoring.metadata.domain;

public class ServiceMetadata {

    private final ServiceName serviceName;
    private final ServiceVersion serviceVersion;
    private final InstanceMetadata instanceMetadata;

    private ServiceMetadata(Builder builder) {
        this.serviceName = new ServiceName(builder.serviceName);
        this.serviceVersion = new ServiceVersion(builder.serviceVersion);
        this.instanceMetadata = new InstanceMetadata(new InstanceId(builder.instanceId), builder.hostAddress);
    }

    public ServiceName serviceName() {
        return serviceName;
    }

    public ServiceVersion serviceVersion() {
        return serviceVersion;
    }

    public InstanceMetadata instanceMetadata() {
        return instanceMetadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String serviceName;
        private String serviceVersion;
        private String instanceId;
        private String hostAddress;

        public Builder withServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder withServiceVersion(String serviceVersion) {
            this.serviceVersion = serviceVersion;
            return this;
        }

        public Builder withInstanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public Builder withHostAddress(String hostAddress) {
            this.hostAddress = hostAddress;
            return this;
        }

        public ServiceMetadata build() {
            return new ServiceMetadata(this);
        }
    }
}
