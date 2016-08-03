package org.jon.gille.dropwizard.monitoring.metadata.domain;

public class ServiceMetadata {

    private final ServiceId serviceId;
    private final ServiceName serviceName;
    private final ServiceVersion serviceVersion;
    private final InstanceMetadata instanceMetadata;


    public ServiceMetadata(Builder builder) {
        this.serviceId = new ServiceId(builder.serviceId);
        this.serviceName = new ServiceName(builder.serviceName);
        this.serviceVersion = new ServiceVersion(builder.serviceVersion);
        this.instanceMetadata = new InstanceMetadata(new InstanceId(builder.instanceId), builder.hostAddress);
    }

    public ServiceId serviceId() {
        return serviceId;
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
        private String serviceId;
        private String serviceName;
        private String serviceVersion;
        private String instanceId;
        private String hostAddress;

        public Builder withServiceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

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
