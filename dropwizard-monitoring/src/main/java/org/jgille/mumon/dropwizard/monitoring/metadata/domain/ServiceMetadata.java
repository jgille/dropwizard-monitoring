package org.jgille.mumon.dropwizard.monitoring.metadata.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceMetadata {

    private final ServiceName serviceName;
    private final ServiceVersion serviceVersion;
    private final InstanceMetadata instanceMetadata;
    private final Map<String, Object> additionalMetadata;

    private ServiceMetadata(Builder builder) {
        this.serviceName = new ServiceName(builder.serviceName);
        this.serviceVersion = new ServiceVersion(builder.serviceVersion);
        this.instanceMetadata = new InstanceMetadata(new InstanceId(builder.instanceId), builder.hostAddress);
        this.additionalMetadata = new LinkedHashMap<>(builder.additionalMetadata);
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

    public Map<String, Object> additionalMetadata() {
        return Collections.unmodifiableMap(additionalMetadata);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String serviceName;
        private String serviceVersion;
        private String instanceId;
        private String hostAddress;
        private Map<String, Object> additionalMetadata = Collections.emptyMap();

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

        public Builder withAdditionalMetadata(Map<String, Object> additionalMetadata) {
            this.additionalMetadata = additionalMetadata;
            return this;
        }
    }
}
