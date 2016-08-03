package org.jon.gille.dropwizard.monitoring.metadata.domain;

import java.net.Inet4Address;
import java.net.UnknownHostException;

public interface InstanceMetadataProvider {

    String instanceId();

    default String hostAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unexpected error when getting localhost address", e);
        }
    }
}
